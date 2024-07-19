package com.android.example.cameraxapp;


import android.util.Log;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReference;
import com.hbb20.CountryCodePicker;

public class DisplayActivity extends AppCompatActivity {

    private TextView textNameView, textIdNoView, textPhoneNoView, selectedDocumentView, selectedCountryView;
    private static final int REQUEST_PHONE_VERIFICATION = 1;
    private CountryCodePicker countryCodePicker;
    private EditText phoneInput;
    private Button sendOtpBtn, buttonRetake, buttonCopy, buttonVerifyPhone;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

//        Get the intent
        AtomicReference<Intent> intent = new AtomicReference<>(getIntent());

      // Initialize views
        countryCodePicker = findViewById(R.id.ccp);
        phoneInput = findViewById(R.id.phone_number_input);
        sendOtpBtn = findViewById(R.id.verify_button);
        progressBar = findViewById(R.id.Progressbar);
        progressBar.setVisibility(View.GONE);

        textNameView = findViewById(R.id.name_text);
        textDocNoView = findViewById(R.id.doc_no_text);
        textPhoneNoView = findViewById(R.id.phone_number_text);
        selectedDocumentView = findViewById(R.id.selected_document_text);
        selectedCountryView = findViewById(R.id.selected_country_text);

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

        } else {
            Toast.makeText(this, "No text found. Please Retake image", Toast.LENGTH_SHORT).show();
            // Handle case where no text is passed
            finish();
        }



        textNameView.setText("Name: " + (name != null ? name : "Not available"));
        textIdNoView.setText("ID No: " + (idNumber != null ? idNumber : "Not available"));
        selectedDocumentView.setText("Selected Document: " + selectedDocument);
        selectedCountryView.setText("Selected Country: " + selectedCountry);

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

        // Set onClickListener for Copy button
        buttonCopy.setOnClickListener(v -> {
            String copiedText = "Name: " + (name != null ? name : "Not available") +
                    "\nDoc No: " + (idNumber != null ? idNumber : "Not available") +
                    "\nSelected Document: " + selectedDocument +
                    "\nSelected Country: " + selectedCountry;
            copyToClipboard(copiedText);
            Toast.makeText(DisplayActivity.this, "Copied to clipboard", Toast.LENGTH_SHORT).show();
        });

        // Register phone number input with country code picker
        countryCodePicker.registerCarrierNumberEditText(phoneInput);

        // Set onClickListener for Verify Phone button
        sendOtpBtn.setOnClickListener(v -> {
            if (!countryCodePicker.isValidFullNumber()) {
                phoneInput.setError("Phone number not valid");
                return;
            }
            String fullPhoneNumber = countryCodePicker.getFullNumberWithPlus();
            Intent intent = new Intent(DisplayActivity.this, LoginOtpActivity.class);
            intent.putExtra("phone", fullPhoneNumber);
            intent.putExtra("name", name);
            intent.putExtra("idNumber", idNumber);
            intent.putExtra("selectedDocument", selectedDocument);
            intent.putExtra("selectedCountry", selectedCountry);
            startActivity(intent);
        });
    }


    public static String replaceSpecial(String newText){
        return newText.replaceAll("<", " ");

    private void copyToClipboard(String text) {
        android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Text", text);
        clipboard.setPrimaryClip(clip);
    }
}
