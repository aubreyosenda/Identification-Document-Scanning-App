package com.android.example.cameraxapp;

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

        // Initialize views
        countryCodePicker = findViewById(R.id.ccp);
        phoneInput = findViewById(R.id.phone_number_input);
        sendOtpBtn = findViewById(R.id.verify_button);
        progressBar = findViewById(R.id.Progressbar);
        progressBar.setVisibility(View.GONE);

        textNameView = findViewById(R.id.name_text);
        textIdNoView = findViewById(R.id.id_no_text);
        textPhoneNoView = findViewById(R.id.phone_number_text);
        selectedDocumentView = findViewById(R.id.selected_document_text);
        selectedCountryView = findViewById(R.id.selected_country_text);

        buttonRetake = findViewById(R.id.button_retake);
        buttonCopy = findViewById(R.id.button_copy);
       // buttonVerifyPhone = findViewById(R.id.button_verify_phone);

        // Get data from previous activity
        String name = getIntent().getStringExtra("name");
        String idNumber = getIntent().getStringExtra("idNumber");
        String selectedDocument = getIntent().getStringExtra("selectedDocument");
        String selectedCountry = getIntent().getStringExtra("selectedCountry");

        textNameView.setText("Name: " + (name != null ? name : "Not available"));
        textIdNoView.setText("ID No: " + (idNumber != null ? idNumber : "Not available"));
        selectedDocumentView.setText("Selected Document: " + selectedDocument);
        selectedCountryView.setText("Selected Country: " + selectedCountry);

        // Set onClickListener for Retake button
        buttonRetake.setOnClickListener(v -> {
            Intent intent = new Intent(DisplayActivity.this, CameraActivity.class);
            startActivity(intent);
            finish();
        });

        // Set onClickListener for Copy button
        buttonCopy.setOnClickListener(v -> {
            String copiedText = "Name: " + (name != null ? name : "Not available") +
                    "\nID No: " + (idNumber != null ? idNumber : "Not available") +
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

    private void copyToClipboard(String text) {
        android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Text", text);
        clipboard.setPrimaryClip(clip);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PHONE_VERIFICATION && resultCode == RESULT_OK) {
            if (data != null) {
                String phoneNumber = data.getStringExtra("phone");
                textPhoneNoView.setText("Phone Number: " + phoneNumber);
            }
        }
    }
}
