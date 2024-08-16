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
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.android.example.cameraxapp.databinding.ActivityCameraBinding
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

class CameraActivity : AppCompatActivity() {
    private var selectedDocument: String? = null
    private var selectedCountry: String? = null
    private var vehicleNumberPlate: String? = null
    private lateinit var selectedDocumentText: TextView
    private lateinit var viewBinding: ActivityCameraBinding
    private var imageCapture: ImageCapture? = null
    private lateinit var cameraExecutor: ExecutorService
    private lateinit var imageView: ImageView
    private lateinit var preview: Preview


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)


//          Set the top bar Items
        var topBar = findViewById<TopBar>(R.id.top_bar)

        topBar.setTitle("Scan Document")
        topBar.setBackIconClickListener { view: View? -> finish() }
        topBar.setMenuIconClickListener { view: View? ->
            Toast.makeText(this, "Menu Icon clicked", Toast.LENGTH_SHORT).show()
        }


//        Set the bottom bar items
//        bottomBar = findViewById<BottomBar>(R.id.bottom_bar)

        // Initialize the views
        selectedDocumentText = findViewById(R.id.selected_document_text)
        imageView = findViewById(R.id.imageView)


        // Request camera permissions
        if (allPermissionsGranted()) {
            startCamera()
        } else {
            requestPermissions()
        }

        // Retrieve the selected document type and country name from intent
        val selectedDocument = intent.getStringExtra("selectedDocument")
        val selectedCountry = intent.getStringExtra("selectedCountry")
        val vehicleNumberPlate = intent.getStringExtra("vehicleNumberPlate")
        this.selectedDocument = selectedDocument
        this.selectedCountry = selectedCountry
        this.vehicleNumberPlate = vehicleNumberPlate


        // Display the selected document type and country name
        selectedDocumentText.text = "Selected Document: $selectedDocument\nSelected Country: $selectedCountry\n Vehicle Numberplate $vehicleNumberPlate"

        // Set up the listener for the image capture button
        viewBinding.imageCaptureButton.setOnClickListener { takePhoto() }

        cameraExecutor = Executors.newSingleThreadExecutor()
    }

    private fun takePhoto() {
        val imageCapture = imageCapture ?: run {
            Log.e(TAG, "ImageCapture is not initialized")
            return
        }

        imageCapture.takePicture(
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageCapturedCallback() {
                override fun onError(exc: ImageCaptureException) {
                    Log.e(TAG, "Photo capture failed: ${exc.message}", exc)
                }

                override fun onCaptureSuccess(image: ImageProxy) {
                    super.onCaptureSuccess(image)
                    val bitmap = imageProxyToBitmap(image)
                    image.close()
                    extractTextFromImage(bitmap)
                }
            }
        )
    }

    private fun extractTextFromImage(bitmap: Bitmap) {
        val image = InputImage.fromBitmap(bitmap, 0)
        val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

        recognizer.process(image)
            .addOnSuccessListener { visionText ->
                val resultText = visionText.text

                // Stop the camera by unbinding all use cases
                val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
                cameraProviderFuture.addListener({
                    try {
                        val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
                        cameraProvider.unbindAll()

                        // Start DisplayActivity with extracted text for processing
                        val intent = Intent(this@CameraActivity, DisplayActivity::class.java).apply {
                            putExtra("source", "CameraActivity")
                            putExtra("extractedText", resultText)
                            putExtra("selectedDocument", selectedDocument)
                            putExtra("selectedCountry", selectedCountry)
                            putExtra("vehicleNumberPlate", vehicleNumberPlate)
                        }
                        startActivity(intent)
                        finish()
                    } catch (exc: Exception) {
                        Log.e(TAG, "Use case unbinding failed", exc)
                    }
                }, ContextCompat.getMainExecutor(this))
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Text extraction failed: ${e.message}", e)
            }
    }



    private fun imageProxyToBitmap(image: ImageProxy): Bitmap {
        val buffer = image.planes[0].buffer
        val bytes = ByteArray(buffer.remaining())
        buffer.get(bytes)
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
    }


    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            try {
                val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

                // Preview
                val preview = Preview.Builder()
                    .build()
                    .also {
                        it.setSurfaceProvider(viewBinding.viewFinder.surfaceProvider)
                    }

                // Calculate aspect ratio
                val metrics = DisplayMetrics()
                val display = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    this.display
                } else {
                    @Suppress("DEPRECATION")
                    windowManager.defaultDisplay
                }
                display?.getRealMetrics(metrics)

                val screenAspectRatio = aspectRatio(metrics.widthPixels, metrics.heightPixels)

                // Image capture use case
                val previewWidth = viewBinding.viewFinder.width
                val previewHeight = viewBinding.viewFinder.height

                imageCapture = ImageCapture.Builder()
                    .setTargetResolution(Size(previewWidth, previewHeight))
                    .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                    .build()

                // Select back camera
                val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

                // Unbind use cases before rebinding
                cameraProvider.unbindAll()

                // Bind use cases to camera
                cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture)
            } catch (exc: Exception) {
                Log.e(TAG, "Use case binding failed", exc)
            }
        }, ContextCompat.getMainExecutor(this))
    }


    private fun aspectRatio(width: Int, height: Int): Int {
        val previewRatio = max(width, height).toDouble() / min(width, height)
        return if (abs(previewRatio - 4.0 / 3.0) <= abs(previewRatio - 16.0 / 9.0)) {
            AspectRatio.RATIO_4_3
        } else {
            AspectRatio.RATIO_16_9
        }
    }


    private fun requestPermissions() {
        activityResultLauncher.launch(REQUIRED_PERMISSIONS)
    }

    private val activityResultLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            var permissionGranted = true
            permissions.entries.forEach {
                if (it.key in REQUIRED_PERMISSIONS && !it.value)
                    permissionGranted = false
            }
            if (!permissionGranted) {
                Toast.makeText(baseContext, "Permission request denied", Toast.LENGTH_SHORT).show()
            } else {
                startCamera()
            }
        }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    companion object {
        private const val TAG = "CameraActivity"
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val RATIO_4_3_VALUE = 4.0 / 3.0
        private const val RATIO_16_9_VALUE = 16.0 / 9.0
    }
}


