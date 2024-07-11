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


        // Set onClickListener for Retake button
        buttonRetake.setOnClickListener(v -> {
            Intent intent = new Intent(DisplayActivity.this, CameraActivity.class);
            startActivity(intent);
            finish();
        });


//    private void getTextFromImage(Bitmap bitmap) {
//        TextRecognizer recognizer = new TextRecognizer.Builder(this).build();
//        if (!recognizer.isOperational()) {
//            Toast.makeText(this, "Error!", Toast.LENGTH_SHORT).show();
//        } else {
//            Frame frame = new Frame.Builder().setBitmap(bitmap).build();
//            SparseArray<TextBlock> sparseArray = recognizer.detect(frame);
//            StringBuilder stringBuilder = new StringBuilder();
//            for (int i = 0; i < sparseArray.size(); i++) {
//                TextBlock textBlock = sparseArray.valueAt(i);
//                stringBuilder.append(textBlock.getValue());
//                stringBuilder.append("\n");
//            }
//
////            Get all text from stringBuilder
//            String allText = stringBuilder.toString();
//            String[] lines = allText.split("\n");  // Create a new array to store modified strings
//
//            // remove all white spaces
//            for (int i = 0; i < lines.length; i++) {
//                lines[i] = lines[i].replaceAll("\\s", "");
//            }
//
//            textNameView.setText(getFullName(lines[lines.length-1])); // Last line in the scan
//            textIdNoView.setText(getIDNo(lines[lines.length-2])); // Second last line in the scan
////            Log.d("Info", "Text begins here" + stringBuilder.toString());
//
////            textView.setText(stringBuilder.toString());
//
////            // Make the TextView editable
////            textView.setFocusable(true);
////            textView.setFocusableInTouchMode(true);
////            textView.setClickable(true);
////            textView.setCursorVisible(true); // Show the cursor
//
//        }
//
//        // Set onClickListener for Copy Text button
//        buttonCopy.setOnClickListener(v -> {
//            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
//            ClipData clip = ClipData.newPlainText("Extracted Text", extractedText);
//            clipboard.setPrimaryClip(clip);
//            Toast.makeText(DisplayActivity.this, "Text copied to clipboard", Toast.LENGTH_SHORT).show();
//        });
//    }
//
//    public static String reverseString(String text) {
//        return new StringBuilder(text).reverse().toString();
//    }
//
//    public static String getFullName(String newText){
//        // System.out.println(newText);
//        return newText.replaceAll("<", " ");
//    }
//
//    public static String getIDNo(String text) {
//        String reversedText = reverseString(text);
//        String reversedIdNo = reversedText.substring(4, 13);
//        String dummyIdNo = reverseString(reversedIdNo);
//
//        // Check if dummyIdNo starts with '0' or 'O'
//        if (dummyIdNo.startsWith("0") || dummyIdNo.startsWith("O") || dummyIdNo.startsWith("o")) {
//            // Discard the first character
//            return dummyIdNo.substring(1);
//        } else{
//            return dummyIdNo;
//        }
//    }
    }
}
