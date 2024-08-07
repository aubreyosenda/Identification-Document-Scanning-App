package com.android.example.cameraxapp

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.util.Size
import android.view.View
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import com.hbb20.CountryCodePicker
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class SelectDocumentActivityKt : AppCompatActivity() {
    private lateinit var topBar: TopBar
    private lateinit var bottomBar: BottomBar
    private lateinit var countryCodePicker: CountryCodePicker
    private lateinit var vehicleRadioGroup: RadioGroup
    private lateinit var vehicleYesRadioButton: RadioButton
    private lateinit var vehicleNoRadioButton: RadioButton
    private lateinit var captureButton: Button
    private lateinit var continueButton: Button
    private lateinit var vehicleCategorySpinner: Spinner
    private lateinit var vehicleSection: View
    private lateinit var cameraPreview: PreviewView
    private lateinit var documentRadioGroup: RadioGroup
    private lateinit var cameraExecutor: ExecutorService
    private var imageCapture: ImageCapture? = null
    private var extractedNumberPlate: String? = null
    private var selectedDocument: String? = null
    private var selectedCountry: String? = null
    private var vehicleCategory: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_document)

        initializeViews()
        setupListeners()
        setupCamera()
    }

    private fun initializeViews() {
        topBar = findViewById(R.id.top_bar)
        bottomBar = findViewById(R.id.bottom_bar)
        countryCodePicker = findViewById(R.id.ccp)
        vehicleRadioGroup = findViewById(R.id.vehicle_radio_group)
        vehicleYesRadioButton = findViewById(R.id.radio_vehicle_yes)
        vehicleNoRadioButton = findViewById(R.id.radio_vehicle_no)
        captureButton = findViewById(R.id.button_capture)
        continueButton = findViewById(R.id.button_add)
        vehicleSection = findViewById(R.id.vehicle_section)
        vehicleCategorySpinner = findViewById(R.id.vehicle_category_spinner)
        cameraPreview = findViewById(R.id.viewFinder)
        documentRadioGroup = findViewById(R.id.document_radio_group)

        topBar.setTitle("Select Document")
        topBar.setBackIconClickListener { finish() }

        countryCodePicker.setDefaultCountryUsingNameCode("KE")
        countryCodePicker.showFullName(true)
        countryCodePicker.setShowPhoneCode(false)

        vehicleNoRadioButton.isChecked = true
        vehicleSection.visibility = View.GONE
        documentRadioGroup.visibility = View.GONE
        continueButton.visibility = View.GONE
    }

    private fun setupListeners() {
        vehicleRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.radio_vehicle_yes -> {
                    vehicleSection.visibility = View.VISIBLE
                    if (allPermissionsGranted()) {
                        startCamera()
                    } else {
                        requestPermissions()
                    }
                }
                R.id.radio_vehicle_no -> {
                    vehicleSection.visibility = View.GONE
                    documentRadioGroup.visibility = View.VISIBLE
                    continueButton.visibility = View.VISIBLE
                }
            }
        }

        captureButton.setOnClickListener {
            takePhotoForVehicleNumberPlate()
        }

        continueButton.setOnClickListener {
            if (validateInputs()) {
                proceedToCameraActivity()
            }
        }
    }

    private fun setupCamera() {
        cameraExecutor = Executors.newSingleThreadExecutor()
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(cameraPreview.surfaceProvider)
                }

            val metrics = DisplayMetrics().also { cameraPreview.display.getRealMetrics(it) }
            val screenAspectRatio = aspectRatio(metrics.widthPixels, metrics.heightPixels)

            imageCapture = ImageCapture.Builder()
                .setTargetAspectRatio(screenAspectRatio)
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                .build()

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture)
            } catch (exc: Exception) {
                Log.e(TAG, "Use case binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(this))
    }

    private fun takePhotoForVehicleNumberPlate() {
        val imageCapture = imageCapture ?: return

        imageCapture.takePicture(
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageCapturedCallback() {
                override fun onCaptureSuccess(image: ImageProxy) {
                    val bitmap = imageProxyToBitmap(image)
                    image.close()
                    extractTextFromImageForVehicleNumberPlate(bitmap)
                }

                override fun onError(exc: ImageCaptureException) {
                    Log.e(TAG, "Photo capture failed: ${exc.message}", exc)
                }
            }
        )
    }

    private fun extractTextFromImageForVehicleNumberPlate(bitmap: Bitmap) {
        val image = InputImage.fromBitmap(bitmap, 0)
        val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

        recognizer.process(image)
            .addOnSuccessListener { visionText ->
                extractedNumberPlate = visionText.text
                showTextPopup(extractedNumberPlate ?: "No text extracted")
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Text extraction failed: ${e.message}", e)
                Toast.makeText(this, "Text extraction failed: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun showTextPopup(extractedText: String) {
        AlertDialog.Builder(this)
            .setTitle("Extracted Text")
            .setMessage(extractedText)
            .setPositiveButton("OK") { _, _ ->
                documentRadioGroup.visibility = View.VISIBLE
                continueButton.visibility = View.VISIBLE
            }
            .create()
            .show()
    }

    private fun validateInputs(): Boolean {
        if (countryCodePicker.selectedCountryName.isEmpty()) {
            Toast.makeText(this, "Please select a country", Toast.LENGTH_SHORT).show()
            return false
        }

        if (vehicleYesRadioButton.isChecked && extractedNumberPlate.isNullOrEmpty()) {
            Toast.makeText(this, "Please capture the vehicle number plate", Toast.LENGTH_SHORT).show()
            return false
        }

        if (documentRadioGroup.checkedRadioButtonId == -1) {
            Toast.makeText(this, "Please select a document type", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    private fun proceedToCameraActivity() {
        selectedCountry = countryCodePicker.selectedCountryName
        vehicleCategory = if (vehicleYesRadioButton.isChecked) vehicleCategorySpinner.selectedItem.toString() else null
        val vehicleType = if (vehicleYesRadioButton.isChecked) "Yes" else "No"

        selectedDocument = when (documentRadioGroup.checkedRadioButtonId) {
            R.id.radio_id_card -> "ID Card"
            R.id.radio_passport -> "Passport"
            R.id.radio_no_document -> "No Document"
            else -> null
        }

        val intent = Intent(this, CameraActivity::class.java).apply {
            putExtra("selectedDocument", selectedDocument)
            putExtra("selectedCountry", selectedCountry)
            putExtra("vehicleCategory", vehicleCategory)
            putExtra("vehicleType", vehicleType)
            putExtra("numberPlate", extractedNumberPlate)
        }
        startActivity(intent)
    }

    private fun imageProxyToBitmap(image: ImageProxy): Bitmap {
        val buffer = image.planes[0].buffer
        val bytes = ByteArray(buffer.remaining())
        buffer.get(bytes)
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
    }

    private fun aspectRatio(width: Int, height: Int): Int {
        val previewRatio = Math.max(width, height).toDouble() / Math.min(width, height)
        if (Math.abs(previewRatio - 4.0 / 3.0) <= Math.abs(previewRatio - 16.0 / 9.0)) {
            return AspectRatio.RATIO_4_3
        }
        return AspectRatio.RATIO_16_9
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermissions() {
        activityResultLauncher.launch(REQUIRED_PERMISSIONS)
    }

    private val activityResultLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            if (permissions.all { it.value }) {
                startCamera()
            } else {
                Toast.makeText(this, "Permissions not granted by the user.", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    companion object {
        private const val TAG = "SelectDocumentActivity"
        private val REQUIRED_PERMISSIONS = mutableListOf(
            Manifest.permission.CAMERA
        ).toTypedArray()
    }
}