package com.android.example.cameraxapp;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class test1 extends AppCompatActivity {

    public static final String EXTRA_TITLE = "extra_title";
    private static final int REQUEST_CODE = 22;

    Button btnCapture;
    ImageView imageView;
    FrameLayout frameLayoutScan;

    private ActivityResultLauncher<Intent> activityResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        btnCapture = findViewById(R.id.btncamera_id);
        imageView = findViewById(R.id.image);
        frameLayoutScan = findViewById(R.id.frame_layout_scan);

        btnCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                activityResultLauncher.launch(cameraIntent);
            }
        });

        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == RESULT_OK) {
                            Bundle extras = result.getData().getExtras();
                            if (extras != null) {
                                Bitmap imageBitmap = (Bitmap) extras.get("data");
                                if (imageBitmap != null) {
                                    Uri imageUri = saveImage(imageBitmap);
                                    imageView.setImageBitmap(imageBitmap);
                                } else {
                                    Log.e("CameraActivity", "Image bitmap is null");
                                }
                            } else {
                                Log.e("CameraActivity", "No extras returned from camera");
                            }
                        } else {
                            Log.e("CameraActivity", "Result code is not OK: " + result.getResultCode());
                        }
                    }
                });

        String title = getIntent().getStringExtra(EXTRA_TITLE);
        if (title != null) {
            setTitle(title);
            if (title.equals(getString(R.string.scan_id)) || title.equals(getString(R.string.scan_passport)) || title.equals(getString(R.string.scan_driving_licence))) {
                startScanning();
            }
        }
    }

    private void startScanning() {
        // In a real application, implement your scanning logic here
        TextView textViewResult = findViewById(R.id.text_view_result);
        textViewResult.setText("Scanning " + getTitle() + "...");

        // Example of changing the size of the FrameLayout based on the document type
        if (getTitle().equals(getString(R.string.scan_id))) {
            frameLayoutScan.setLayoutParams(new FrameLayout.LayoutParams(600, 400)); // Example size for ID
        } else if (getTitle().equals(getString(R.string.scan_passport))) {
            frameLayoutScan.setLayoutParams(new FrameLayout.LayoutParams(800, 1200)); // Example size for Passport
        } else if (getTitle().equals(getString(R.string.scan_driving_licence))) {
            frameLayoutScan.setLayoutParams(new FrameLayout.LayoutParams(700, 500)); // Example size for Driving Licence
        }
    }

    private Uri saveImage(Bitmap imageBitmap) {
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File imageFile = null;
        try {
            imageFile = File.createTempFile(
                    generateFileName(),  /* prefix */
                    ".jpg",         /* suffix */
                    storageDir      /* directory */
            );
            FileOutputStream fos = new FileOutputStream(imageFile);
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.close();
        } catch (IOException e) {
            Log.e("CameraActivity", "Error saving image: " + e.getMessage());
        }

        if (imageFile != null) {
            return FileProvider.getUriForFile(this,
                    "com.example.ocr.fileprovider",
                    imageFile);
        }
        return null;
    }

    private String generateFileName() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        return "JPEG_" + timeStamp + "_";
    }
}
