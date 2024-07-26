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

import androidx.appcompat.app.AppCompatActivity;

import com.hbb20.CountryCodePicker;

import java.util.Arrays;
import java.util.List;

public class VerifyPhoneActivity extends AppCompatActivity {
    CountryCodePicker countryCodePicker;
    EditText phoneInput;
    Button sendOtpBtn;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_phone);

        countryCodePicker = findViewById(R.id.ccp);
        phoneInput = findViewById(R.id.phone_number);
        sendOtpBtn = findViewById(R.id.verify_button);
        progressBar = findViewById(R.id.Progressbar);
        progressBar.setVisibility(View.GONE);

        countryCodePicker.registerCarrierNumberEditText(phoneInput);

//        Spinners
        Spinner organizationSpinner = findViewById(R.id.organization_spinner);
        List<String> organizations = Arrays.asList("Organization 1", "Organization 2", "Organization 3");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, organizations);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        organizationSpinner.setAdapter(adapter);

        organizationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedOrganization = parent.getItemAtPosition(position).toString();
                // Handle the selected organization
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle the case where no organization is selected
            }
        });

        Spinner floorSpinner = findViewById(R.id.floor_spinner);
        List<String> floors = Arrays.asList("Ground", "1st", "2nd", "3rd", "4th", "5th");

        ArrayAdapter<String> floorAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, floors);
        floorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        floorSpinner.setAdapter(floorAdapter);

        floorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedFloor = parent.getItemAtPosition(position).toString();
                // Handle the selected floor
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle the case where no floor is selected
            }
        });

        sendOtpBtn.setOnClickListener(v -> {
            if (!countryCodePicker.isValidFullNumber()) {
                phoneInput.setError("Phone number not valid");
                return;
            }

            Intent intent = new Intent(VerifyPhoneActivity.this, LoginOtpActivity.class);
            intent.putExtra("phone", countryCodePicker.getFullNumberWithPlus());
            startActivity(intent);
        });
    }
}