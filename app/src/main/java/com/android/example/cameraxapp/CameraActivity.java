package com.android.example.cameraxapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class CameraActivity extends AppCompatActivity {

    public static final String EXTRA_TITLE = "extra_title";
    private static final int REQUEST_CODE_SCAN = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        String title = getIntent().getStringExtra(EXTRA_TITLE);
        if (title != null) {
            setTitle(title);
        }

        findViewById(R.id.button_start_scan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the scanning process here
                // For simplicity, we'll just show a message indicating scanning started
                startScanning();
            }
        });
    }

    private void startScanning() {
        // In a real application, you would implement your scanning logic here
        // For demonstration purposes, we'll just show a message
        TextView textViewResult = findViewById(R.id.text_view_result);
        textViewResult.setText("Scanning " + getTitle() + "...");
    }
}
