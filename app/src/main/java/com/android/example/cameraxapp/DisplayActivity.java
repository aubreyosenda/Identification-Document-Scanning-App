package com.android.example.cameraxapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.googlecode.tesseract.android.TessBaseAPI;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class DisplayActivity extends AppCompatActivity {


    private ImageView imageView;
    private TextView textView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_display);
        imageView = findViewById(R.id.imageView);
        textView = findViewById(R.id.text);

        // Copy Tesseract data files from assets on first run
        copyTesseractDataFiles();

        // Get the image URI passed from MainActivity1
        Intent intent = getIntent();
        String imageUriString = intent.getStringExtra("imageUri");
        Uri imageUri = Uri.parse(imageUriString);

        // Load and display the image using Glide or any image loading library
        Glide.with(this)
                .asBitmap()
                .load(imageUri)
                .into(imageView);

//        #*******************
//        try {
//            // Open the image from assets folder
//            InputStream inputStream = getAssets().open("Screenshot20.png");
//            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
//
//            // Set the image on the ImageView
//            imageView.setImageBitmap(bitmap);
//            imageView.setDrawingCacheEnabled(true);  // Enable drawing cache
//
//
//        } catch (IOException e) {
//            Log.e("Error", "Failed to load image from assets!", e);
//            // Handle potential exceptions (e.g., image not found)
//            Toast.makeText(this, "Failed to load image!", Toast.LENGTH_SHORT).show();
//        }

//                .into(new CustomTarget<Bitmap>() {
//
//                    public <GlideDrawable> void onResourceReady(@NonNull Bitmap image, @Nullable GlideDrawable placeholder) {
//                        imageView.setImageBitmap(image);
//
//                        // Perform OCR using the loaded image (replace with your TessBaseAPI logic)
//                        performOCR(image);
//                    }
//
//                    @Override
//                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
//                        // Handle image loading failure (e.g., display error message)
//                        Toast.makeText(DisplayActivity.this, "Failed to load image", Toast.LENGTH_SHORT).show();
//                    }
//
//                    @Override
//                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
//
//                    }
//
//                    @Override
//                    public void onLoadCleared(@Nullable Drawable placeholder) {
//
//                    }
//                });
        Button startButton = findViewById(R.id.start); // Replace "start" with actual button ID

        // ... (existing code for loading the image)

        // Set click listener for the Start button
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Perform OCR extraction here
                performOCR(imageView); // Get the image from ImageView
            }
        });
    }

    private <Asset> void copyTesseractDataFiles() {
        try {
//            String dataPath = new File(getFilesDir(), "tessdata").getParent(); // Get parent directory of tessdata
            File tessdataDir = new File(getFilesDir(), "tessdata");
            if (!tessdataDir.exists()) {
                if (!tessdataDir.mkdirs()) {
                    Log.e("Error", "Failed to create tessdata directory!");
                    return;
                }
            }
            String[] dataFiles = getAssets().list("tessdata");
            if (dataFiles == null || dataFiles.length == 0) {
                // Handle case where no data files are found in assets/tessdata
                return;
            }

            for (String filename : dataFiles) {
                Asset asset = (Asset) getAssets().open("tessdata/" + filename);
                File outFile = new File(tessdataDir, filename);
                FileOutputStream outputStream = new FileOutputStream(outFile);
                byte[] buffer = new byte[1024];
                int read;
                while ((read = ((InputStream) asset).read(buffer)) != -1) {
                    outputStream.write(buffer, 0, read);
                }
                ((InputStream) asset).close();
                outputStream.flush();
                outputStream.close();
            }
        } catch (IOException e) {
            Log.e("Error", "Failed to copy Tesseract data files!", e);
        }
    }

    @SuppressLint("SetTextI18n")
    private <TextLine> void performOCR(View view){
        Bitmap image = ((BitmapDrawable) imageView.getDrawable()).getBitmap();

        if (image != null) {
            // Create TessBaseAPI instance (this internally creates the native Tesseract instance)
            TessBaseAPI tess = new TessBaseAPI();


            // Given path must contain subdirectory `tessdata` where are `*.traineddata` language files
            String dataPath = new File(getFilesDir(), "tessdata").getParent();  // Use getAbsolutePath()
            Log.d("TAG", "Data Path: " + dataPath);

            // Initialize API for specified language
            // (can be called multiple times during Tesseract lifetime)

            try {
                if (!tess.init(dataPath, "eng")) { // could be multiple languages, like "eng+deu+fra"
                    // Error initializing Tesseract (wrong/inaccessible data path or not existing language file(s))
                    Toast.makeText(DisplayActivity.this, "Failed to initialize Tesseract ", Toast.LENGTH_SHORT).show();
                }

                Log.d("TAG", "Image Width: " + image.getWidth() + ", Height: " + image.getHeight());
                Log.d("TAG", "Image Config: " + image.getConfig());

                // Load the image (file path, Bitmap, Pix...)
                // (can be called multiple times during Tesseract lifetime)
                tess.setImage(image);

                // Start the recognition (if not done for this image yet) and retrieve the result
                // (can be called multiple times during Tesseract lifetime)
                String extractedText = tess.getUTF8Text().toString();
                Log.d("TAG", "Extracted Text: " + extractedText);

                // Update the text view with the extracted text
                if (extractedText != null && !extractedText.isEmpty()) {
                    textView.setText(extractedText);
                } else {
                    textView.setText("No text found");
                }
            } finally {
                if (tess != null) {
                    tess.recycle();
                }
            }
        } else {
//            Handle image not found exception
            Log.d("Error", "Image not found" + image);
        }
    }

//    // Example method for rotating the image
//    public void rotateImage(View view) {
//        // Implement your image rotation logic here
//        // Example: Rotate the image by 90 degrees clockwise
//        imageView.setRotation(imageView.getRotation() + 90);
//    }
//
//    // Example method for zooming the image
//    public void zoomImage(View view) {
//        // Implement your image zoom logic here
//        // Example: Scale the image by a factor
//        imageView.setScaleX(imageView.getScaleX() * 1.2f);
//        imageView.setScaleY(imageView.getScaleY() * 1.2f);
//    }

}
