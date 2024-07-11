package com.android.example.cameraxapp;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class DisplayActivity extends AppCompatActivity {

    private TextView textView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

        textView = findViewById(R.id.text);
        Button buttonRetake = findViewById(R.id.button_retake);
        Button buttonCopy = findViewById(R.id.button_copy);

        // Get the extracted text passed from CameraActivity
        String extractedText = getIntent().getStringExtra("extractedText");
        textView.setText(extractedText);

        // Set onClickListener for Retake button
        buttonRetake.setOnClickListener(v -> {
            Intent intent = new Intent(DisplayActivity.this, CameraActivity.class);
            startActivity(intent);
            finish();
        });

        // Set onClickListener for Copy Text button
        buttonCopy.setOnClickListener(v -> {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("Extracted Text", extractedText);
            clipboard.setPrimaryClip(clip);
            Toast.makeText(DisplayActivity.this, "Text copied to clipboard", Toast.LENGTH_SHORT).show();
        });
    }
}
