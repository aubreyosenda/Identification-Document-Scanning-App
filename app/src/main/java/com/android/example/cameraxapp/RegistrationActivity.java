package com.android.example.cameraxapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.hbb20.CountryCodePicker;

public class RegistrationActivity extends AppCompatActivity {
    private TopBar topBar;
    private BottomBar bottomBar;

    private CountryCodePicker countryCodePicker;

    private RadioGroup radioGroup;
    private Button scanButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

//          Set the top bar Items
        topBar = findViewById(R.id.top_bar);

        topBar.setTitle("Select Document");
        topBar.setBackIconClickListener(view -> finish());
        topBar.setMenuIconClickListener(view -> {
            //Toast.makeText(this, "Menu Icon clicked", Toast.LENGTH_SHORT).show();
        });

//        Set the bottom bar items
        bottomBar = findViewById(R.id.bottom_bar);



        countryCodePicker = findViewById(R.id.ccp);
        radioGroup = findViewById(R.id.radio_group);
        scanButton = findViewById(R.id.button_add);
//        Buttons
        RadioButton radio_id_card = findViewById(R.id.radio_id_card);
        RadioButton radio_passport = findViewById(R.id.radio_passport);
        RadioButton radio_no_document = findViewById(R.id.radio_no_document);


        // Set default country (optional, as it's set in XML)
        countryCodePicker.setDefaultCountryUsingNameCode("KE");
        countryCodePicker.showFullName(true);
        countryCodePicker.setShowPhoneCode(false);

        String selectedCountry = countryCodePicker.getSelectedCountryName();


        // Handle Scan button click
        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!radio_id_card.isChecked() && !radio_passport.isChecked() && !radio_no_document.isChecked()){
                    Toast.makeText(RegistrationActivity.this, "Please select a document", Toast.LENGTH_SHORT).show();
                } else if (radio_no_document.isChecked()) {
//                    If the No document radio is checked, start verify phone activity
                    Intent noDocIntent = new Intent(RegistrationActivity.this, VerifyPhoneActivity.class);
                    noDocIntent.putExtra("selectedCountry", selectedCountry);
                    startActivity(noDocIntent);

                } else if (radio_id_card.isChecked() || radio_passport.isChecked()){
                    int selectedId = radioGroup.getCheckedRadioButtonId();
                    RadioButton selectedRadioButton = findViewById(selectedId);
                    String selectedDocument = selectedRadioButton.getText().toString();

                    // Start MainActivity1 and pass selected document type
                    Intent intent = new Intent(RegistrationActivity.this, CameraActivity.class);
                    intent.putExtra("selectedDocument", selectedDocument);
                    intent.putExtra("selectedCountry", selectedCountry);
                    startActivity(intent);
                }
            }
        });
    }
}
