package com.android.example.cameraxapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.hbb20.CountryCodePicker;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class VerifyPhoneActivity extends AppCompatActivity {
    CountryCodePicker countryCodePicker;
    private TopBar topBar;
    private BottomBar bottomBar;
    EditText phoneInput;
    Button sendOtpBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_phone);

        topBar = findViewById(R.id.top_bar);
        bottomBar = findViewById(R.id.bottom_bar);

//        Top bar events on the verify phone activity
        topBar.setTitle("Verify Phone");
        topBar.setBackIconClickListener(view -> finish());
        topBar.setMenuIconClickListener(view -> {
            Toast.makeText(this, "Menu Icon clicked", Toast.LENGTH_SHORT).show();
        });

//        Set the bottom bar items
        bottomBar = findViewById(R.id.bottom_bar);

//        Phone Number Input
        phoneInput = findViewById(R.id.phone_number);
        countryCodePicker = findViewById(R.id.ccp);
        countryCodePicker.registerCarrierNumberEditText(phoneInput);

        sendOtpBtn = findViewById(R.id.verify_button);

        //        Get the intent
        AtomicReference<Intent> intent = new AtomicReference<>(getIntent());
        String country = intent.get().getStringExtra("selectedCountry");


        sendOtpBtn.setOnClickListener(v -> {
            if (!countryCodePicker.isValidFullNumber()) {
                phoneInput.setError("Phone number not valid");
                return;
            }

            Intent phoneCountryIntent = new Intent(VerifyPhoneActivity.this, DisplayActivity.class);
            phoneCountryIntent.putExtra("source", "VerifyPhoneActivity");
            phoneCountryIntent.putExtra("phoneNo", countryCodePicker.getFullNumberWithPlus());
            phoneCountryIntent.putExtra("selectedCountry", country);
            startActivity(phoneCountryIntent);
        });
    }
}