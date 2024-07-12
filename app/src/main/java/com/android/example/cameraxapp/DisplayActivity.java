package com.android.example.cameraxapp;


import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class DisplayActivity extends AppCompatActivity {

    private TextView textNameView, textIdNoView, textPhoneNoView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

        textNameView = findViewById(R.id.name_text);
        textIdNoView = findViewById(R.id.id_no_text);
        textPhoneNoView = findViewById(R.id.phone_number_text);

        Button buttonRetake = findViewById(R.id.button_retake);
        Button buttonCopy = findViewById(R.id.button_copy);

        // Get the extracted text passed from CameraActivity
        String extractedText = getIntent().getStringExtra("extractedText");

        if (extractedText != null) {
            String[] lines = extractedText.split("\n");  // Create a new array to store modified strings

            // remove all white spaces
            for (int i = 0; i < lines.length; i++) {
                lines[i] = lines[i].replaceAll("\\s", "");
            }

            textNameView.setText(getFullName(lines[lines.length - 1])); // Last line in the scan
            textIdNoView.setText(getIDNo(lines[lines.length - 2])); // Second last line in the scan
//            Log.d("Info", "Text begins here" + stringBuilder.toString());
        } else {
            Toast.makeText(this, "No text found. Please Retake image", Toast.LENGTH_SHORT).show();
            // Handle case where no text is passed
            finish();
        }


        // Set onClickListener for Retake button
        buttonRetake.setOnClickListener(v -> {
            Intent intent = new Intent(DisplayActivity.this, CameraActivity.class);
            startActivity(intent);
            finish();
        });
    }

//            textView.setText(stringBuilder.toString());

//            // Make the TextView editable
//            textView.setFocusable(true);
//            textView.setFocusableInTouchMode(true);
//            textView.setClickable(true);
//            textView.setCursorVisible(true); // Show the cursor



    public static String reverseString(String text) {
        return new StringBuilder(text).reverse().toString();
    }

    public static String getFullName(String newText){
        // System.out.println(newText);
        return newText.replaceAll("<", " ");
    }

    public static String getIDNo(String text) {
        String reversedText = reverseString(text);
        String reversedIdNo = reversedText.substring(4, 13);
        String dummyIdNo = reverseString(reversedIdNo);

        // Check if dummyIdNo starts with '0' or 'O'
        if (dummyIdNo.startsWith("0") || dummyIdNo.startsWith("O") || dummyIdNo.startsWith("o")) {
            // Discard the first character
            return dummyIdNo.substring(1);
        } else{
            return dummyIdNo;
        }
    }
}
