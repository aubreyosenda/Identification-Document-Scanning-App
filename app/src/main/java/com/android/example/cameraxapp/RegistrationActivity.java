package com.android.example.cameraxapp;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.hbb20.CountryCodePicker;

public class RegistrationActivity extends AppCompatActivity {
    private TopBar topBar;
    private BottomBar bottomBar;
    private CountryCodePicker countryCodePicker;
    private RadioGroup radioGroup;
    private Button continueButton;
    private Button registerVehicleButton;
    private EditText vehicleNumberPlateInput;
    private ImageButton closeVehicleInputButton;
    private LinearLayout vehicleInputLayout;
    private ConstraintLayout mainLayout;

    // Method to validate the vehicle number plate input
    private boolean isValidNumberPlate(String numberPlate) {
        String regex = "^(?![\\s-])[A-Za-z0-9\\s-]{6,}(?<![\\s-])$";
        return numberPlate.matches(regex);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        // Set the top bar Items
        topBar = findViewById(R.id.top_bar);
        topBar.setTitle("Register Visitor");
        topBar.setBackIconClickListener(view -> finish());
        topBar.setMenuIconClickListener(view -> {
            // Handle menu icon click
        });

        // Set the bottom bar items
        bottomBar = findViewById(R.id.bottom_bar);

        // Initialize views
        countryCodePicker = findViewById(R.id.ccp);
        radioGroup = findViewById(R.id.radio_group);
        continueButton = findViewById(R.id.continue_button);
        registerVehicleButton = findViewById(R.id.register_vehicle_button);
        vehicleInputLayout = findViewById(R.id.vehicle_input_layout);
        vehicleNumberPlateInput = findViewById(R.id.vehicle_number_plate_input);
        closeVehicleInputButton = findViewById(R.id.close_vehicle_input_button);
        mainLayout = findViewById(R.id.main);

        // Buttons
        RadioButton radio_id_card = findViewById(R.id.radio_id_card);
        RadioButton radio_passport = findViewById(R.id.radio_passport);
        RadioButton radio_no_document = findViewById(R.id.radio_no_document);

        // Set default country (optional, as it's set in XML)
        countryCodePicker.setDefaultCountryUsingNameCode("KE");
        countryCodePicker.showFullName(true);
        countryCodePicker.setShowPhoneCode(false);

        // Initially hide the vehicle number plate input layout
        vehicleInputLayout.setVisibility(View.GONE);

        // Handle Register Vehicle button click
        registerVehicleButton.setOnClickListener(v -> {
            registerVehicleButton.setVisibility(View.GONE);
            vehicleInputLayout.setVisibility(View.VISIBLE);
        });

        // Handle Close button click
        closeVehicleInputButton.setOnClickListener(v -> {
            String numberPlate = vehicleNumberPlateInput.getText().toString().trim();

            if (!numberPlate.isEmpty()) {
                new AlertDialog.Builder(RegistrationActivity.this)
                        .setTitle("Discard Input")
                        .setMessage("You have unsaved input. Do you want to discard it?")
                        .setPositiveButton("Discard", (dialog, which) -> {
                            vehicleNumberPlateInput.setText("");
                            vehicleInputLayout.setVisibility(View.GONE);
                            registerVehicleButton.setVisibility(View.VISIBLE);
                        })
                        .setNegativeButton("Cancel", null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            } else {
                vehicleInputLayout.setVisibility(View.GONE);
                registerVehicleButton.setVisibility(View.VISIBLE);
            }
        });

        // Close input field when clicking outside if empty
        mainLayout.setOnTouchListener((v, event) -> {
            String numberPlate = vehicleNumberPlateInput.getText().toString().trim();
            if (vehicleInputLayout.getVisibility() == View.VISIBLE && numberPlate.isEmpty()) {
                vehicleInputLayout.setVisibility(View.GONE);
                registerVehicleButton.setVisibility(View.VISIBLE);
            }
            return false;
        });

        // Handle Continue button click or any submission action
        // Handle Continue button click or any submission action
        continueButton.setOnClickListener(v -> {
            String numberPlate = vehicleNumberPlateInput.getText().toString().trim();

            if (!isValidNumberPlate(numberPlate) && !numberPlate.isEmpty()) {
                Toast.makeText(RegistrationActivity.this, "Invalid vehicle number plate. Please ensure it is at least 6 characters, does not start or end with special characters, and only includes letters, numbers, spaces, or hyphens.", Toast.LENGTH_LONG).show();
            } else if (radioGroup.getCheckedRadioButtonId() == -1) {
                // No document selected
                Toast.makeText(RegistrationActivity.this, "Please select a document", Toast.LENGTH_SHORT).show();
            } else {
                // Determine which activity to start based on document selection
                Intent intent;
                String selectedDocument = null;

                if (radio_id_card.isChecked() || radio_passport.isChecked()) {
                    // If ID card or Passport is selected, go to CameraActivity
                    intent = new Intent(RegistrationActivity.this, CameraActivity.class);
                    if (radio_id_card.isChecked() || radio_passport.isChecked()) {
                        int selectedId = radioGroup.getCheckedRadioButtonId();
                        RadioButton selectedRadioButton = findViewById(selectedId);
                        selectedDocument = selectedRadioButton.getText().toString();
                    }
                    intent.putExtra("vehicleNumberPlate", numberPlate.isEmpty() ? null : numberPlate);
                    intent.putExtra("selectedDocument", selectedDocument);
                } else {
                    // If no document is selected, go to PhoneNumberActivity
                    intent = new Intent(RegistrationActivity.this, PhoneNumberActivity.class);
                    intent.putExtra("vehicleNumberPlate", numberPlate.isEmpty() ? null : numberPlate);
                    intent.putExtra("selectedDocument", selectedDocument);

                }

                startActivity(intent);
            }
        });
    }
}
