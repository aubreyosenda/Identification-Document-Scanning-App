package com.android.example.cameraxapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;


public class DisplayActivity extends AppCompatActivity {


    private ImageView imageView;
    private TextView textNameView, textIdNoView, textPhoneNoView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_display);
        imageView = findViewById(R.id.imageView);
        textNameView = findViewById(R.id.name_text);
        textIdNoView = findViewById(R.id.id_no_text);
        textPhoneNoView = findViewById(R.id.phone_number_text);


        // Get the image URI passed from MainActivity1
        Intent intent = getIntent();
        String imageUriString = intent.getStringExtra("imageUri");
        Uri imageUri = Uri.parse(imageUriString);

        // Load and display the image using Glide or any image loading library
        Glide.with(this)
                .asBitmap()
                .load(imageUri)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        imageView.setImageBitmap(resource); // Display in ImageView if needed
                        getTextFromImage(resource); // Pass the Bitmap to your OCR method
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                        // Handle clearing the ImageView if needed
                    }
                });

    }

    private void getTextFromImage(Bitmap bitmap) {
        TextRecognizer recognizer = new TextRecognizer.Builder(this).build();
        if (!recognizer.isOperational()) {
            Toast.makeText(this, "Error!", Toast.LENGTH_SHORT).show();
        } else {
            Frame frame = new Frame.Builder().setBitmap(bitmap).build();
            SparseArray<TextBlock> sparseArray = recognizer.detect(frame);
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < sparseArray.size(); i++) {
                TextBlock textBlock = sparseArray.valueAt(i);
                stringBuilder.append(textBlock.getValue());
                stringBuilder.append("\n");
            }

//            Get all text from stringBuilder
            String allText = stringBuilder.toString();
            String[] lines = allText.split("\n");  // Create a new array to store modified strings

            // remove all white spaces
            for (int i = 0; i < lines.length; i++) {
                lines[i] = lines[i].replaceAll("\\s", "");
            }

            textNameView.setText(getFullName(lines[lines.length-1])); // Last line in the scan
            textIdNoView.setText(getIDNo(lines[lines.length-2])); // Second last line in the scan
//            Log.d("Info", "Text begins here" + stringBuilder.toString());

//            textView.setText(stringBuilder.toString());

//            // Make the TextView editable
//            textView.setFocusable(true);
//            textView.setFocusableInTouchMode(true);
//            textView.setClickable(true);
//            textView.setCursorVisible(true); // Show the cursor

        }
    }

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
