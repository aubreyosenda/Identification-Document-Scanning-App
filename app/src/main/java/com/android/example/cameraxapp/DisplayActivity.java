package com.android.example.cameraxapp;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReference;

public class DisplayActivity extends AppCompatActivity {

    private TextView textNameView, textDocNoView, textPhoneNoView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

//        Get the intent
        AtomicReference<Intent> intent = new AtomicReference<>(getIntent());

        textNameView = findViewById(R.id.name_text);
        textDocNoView = findViewById(R.id.doc_no_text);
        textPhoneNoView = findViewById(R.id.phone_number_text);

        Button buttonRetake = findViewById(R.id.button_retake);
        Button buttonCopy = findViewById(R.id.button_copy);


        String extractedText = intent.get().getStringExtra("extractedText"); // Get the extracted text passed from CameraActivity
        String selectedDocument = intent.get().getStringExtra("selectedDocument");

        if (extractedText != null) {
            String[] lines = extractedText.split("\n");  // Create a new array to store modified strings

            // remove all white spaces
            for (int i = 0; i < lines.length; i++) {
                lines[i] = lines[i].replaceAll("\\s", "");
            }

            if ("ID Card".equals(selectedDocument)){
                getIDCardDetails(lines);
            } else if ("Passport".equals(selectedDocument)) {
                getPassportDetails(lines);
            } else if ("Driving License".equals(selectedDocument)) {
                Toast.makeText(DisplayActivity.this, "Selected Doc is DL ", Toast.LENGTH_SHORT).show();
            } else {
                intent.set(new Intent(DisplayActivity.this, GetStartedActivity.class));
                startActivity(intent.get());
                finish();
            }

//            Log.d("Info", "Text begins here" + stringBuilder.toString());
        } else {
            Toast.makeText(this, "No text found. Please Retake image", Toast.LENGTH_SHORT).show();
            // Handle case where no text is passed
            finish();
        }


        // Set onClickListener for Retake button
        buttonRetake.setOnClickListener(v -> {
            intent.set(new Intent(DisplayActivity.this, GetStartedActivity.class));
            startActivity(intent.get());
            finish();
        });
    }

    @SuppressLint("SetTextI18n")
    private void getPassportDetails(String[] text) {
//        Get Passport name
        String nameField = text[text.length - 2].substring(5); //Get second last line
        String[] nameArr = nameField.split("<");
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < nameArr.length; i++) {
            if (nameArr[i].length() > 2) {
                sb.append(nameArr[i]).append(" ");
            }
            if (i == 3) {
                break;
            }
        }

        String fullPassportName = sb.toString().trim();
        textNameView.setText(fullPassportName);
        sb.setLength(0);

        // Get the Passport number from the last Line
        String passNoField = text[text.length - 1].substring(0,8);
        textDocNoView.setText(passNoField);


//        Toast.makeText(DisplayActivity.this, "Finished", Toast.LENGTH_SHORT).show();

    }

    private void getIDCardDetails(String[] lines) {
        textNameView.setText(replaceSpecial(lines[lines.length - 1])); // Last line in the scan
        textDocNoView.setText(getIDNo(lines[lines.length - 2])); // Second last line in the scan
    }


    public static String reverseString(String text) {
        return new StringBuilder(text).reverse().toString();
    }

    public static String replaceSpecial(String newText){
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
