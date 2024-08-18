package com.android.example.cameraxapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.hbb20.CountryCodePicker;

public class PhoneNumberActivity extends AppCompatActivity {
    CountryCodePicker countryCodePicker;
    EditText phoneInput;
    Button sendOtpBtn;
    ProgressBar progressBar;

    // Variables to hold received data
    private String vehicleNumberPlate;
    private String selectedDocument;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_number);

        // Initialize views
        countryCodePicker = findViewById(R.id.ccp);
        phoneInput = findViewById(R.id.phone_number_text);
        sendOtpBtn = findViewById(R.id.verify_button);
        progressBar = findViewById(R.id.Progressbar);
        progressBar.setVisibility(View.GONE);

        // Get Intent extras
        Intent intent = getIntent();
        if (intent != null) {
            vehicleNumberPlate = intent.getStringExtra("vehicleNumberPlate");
            selectedDocument = intent.getStringExtra("selectedDocument");
        }

        // Register phone number input with the CountryCodePicker
        countryCodePicker.registerCarrierNumberEditText(phoneInput);

        // Handle Send OTP button click
        sendOtpBtn.setOnClickListener(v -> {
            if (!countryCodePicker.isValidFullNumber()) {
                phoneInput.setError("Phone number not valid");
                Log.e("Error in phoneNumber", String.valueOf(phoneInput));
                return;
            }

            Intent otpIntent = new Intent(PhoneNumberActivity.this, DisplayActivity.class);
            otpIntent.putExtra("source", "PhoneNumberActivity");
            otpIntent.putExtra("phoneNumber", countryCodePicker.getFullNumberWithPlus());
            otpIntent.putExtra("vehicleNumberPlate", vehicleNumberPlate);
            otpIntent.putExtra("selectedDocument", selectedDocument);
            startActivity(otpIntent);
        });
    }
}
