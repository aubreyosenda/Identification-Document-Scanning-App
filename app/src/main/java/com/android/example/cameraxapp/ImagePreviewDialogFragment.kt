package com.android.example.cameraxapp

import android.app.Dialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.yalantis.ucrop.UCrop
import java.io.File
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.TextRecognizer
import com.google.mlkit.vision.text.latin.TextRecognizerOptions

class ImagePreviewDialogFragment : DialogFragment() {

    companion object {
        private const val ARG_IMAGE_URI = "arg_image_uri"

        fun newInstance(imageUri: Uri): ImagePreviewDialogFragment {
            val fragment = ImagePreviewDialogFragment()
            val args = Bundle()
            args.putParcelable(ARG_IMAGE_URI, imageUri)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val imageUri = arguments?.getParcelable<Uri>(ARG_IMAGE_URI)
        if (imageUri == null) {
            dismiss()
            return super.onCreateDialog(savedInstanceState)
        }

        val builder = AlertDialog.Builder(requireContext())
        val inflater = requireActivity().layoutInflater
        val view = inflater.inflate(R.layout.dialog_image_preview, null)

        val imageView = view.findViewById<ImageView>(R.id.image_view)
        Glide.with(this).load(imageUri).into(imageView)

        val buttonUse = view.findViewById<Button>(R.id.button_use)
        val buttonCrop = view.findViewById<Button>(R.id.button_crop)
        val buttonRetake = view.findViewById<Button>(R.id.button_retake)

        buttonUse.setOnClickListener {
            Glide.with(this)
                .asBitmap()
                .load(imageUri)
                .into(object : CustomTarget<Bitmap>() {
                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                        extractTextFromImage(resource, object : TextRecognitionCallback {
                            override fun onTextRecognized(text: String) {
                                val intent = Intent(requireContext(), DisplayActivity::class.java)
                                intent.putExtra("extractedText", text)
                                startActivity(intent)
                                dismiss()
                            }

                            override fun onError(e: Exception) {
                                Toast.makeText(requireContext(), "Error recognizing text: ${e.message}", Toast.LENGTH_SHORT).show()
                            }
                        })
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {}
                })
        }

        buttonCrop.setOnClickListener {
            val destinationUri = Uri.fromFile(File(requireContext().cacheDir, "croppedImage.jpg"))
            UCrop.of(imageUri, destinationUri)
                .withAspectRatio(1f, 1f)
                .start(requireActivity(), this@ImagePreviewDialogFragment, UCrop.REQUEST_CROP)
        }

        buttonRetake.setOnClickListener { dismiss() }

        builder.setView(view)
        return builder.create()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == UCrop.REQUEST_CROP && resultCode == android.app.Activity.RESULT_OK) {
            val resultUri = UCrop.getOutput(data!!)
            resultUri?.let {
                val imageView = dialog?.findViewById<ImageView>(R.id.image_view)
                imageView?.setImageURI(resultUri)

                // Update the imageUri with the cropped image URI
                arguments?.putParcelable(ARG_IMAGE_URI, resultUri)
            }
        } else if (resultCode == UCrop.RESULT_ERROR) {
            val cropError = UCrop.getError(data!!)
            Toast.makeText(requireContext(), "Crop error: ${cropError?.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun extractTextFromImage(bitmap: Bitmap, callback: TextRecognitionCallback) {
        val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
        val image = InputImage.fromBitmap(bitmap, 0)

        recognizer.process(image)
            .addOnSuccessListener { visionText ->
                val resultText = StringBuilder()
                for (block in visionText.textBlocks) {
                    for (line in block.lines) {
                        for (element in line.elements) {
                            resultText.append(element.text).append("\n")
                        }
                    }
                }
                callback.onTextRecognized(resultText.toString())
            }
            .addOnFailureListener { e ->
                callback.onError(e)
            }
    }
}

interface TextRecognitionCallback {
    fun onTextRecognized(text: String)
    fun onError(e: Exception)
}
