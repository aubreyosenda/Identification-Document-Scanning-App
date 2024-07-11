package com.android.example.cameraxapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import androidx.appcompat.app.AppCompatActivity;
import com.hbb20.CountryCodePicker;

public class GetStartedActivity extends AppCompatActivity {

    private CountryCodePicker countryCodePicker;
    private RadioGroup radioGroup;
    private Button scanButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_started);

        countryCodePicker = findViewById(R.id.ccp);
        radioGroup = findViewById(R.id.radio_group);
        scanButton = findViewById(R.id.button_add);

        // Set default country (optional, as it's set in XML)
        countryCodePicker.setDefaultCountryUsingNameCode("KE");
        countryCodePicker.showFullName(true);
        countryCodePicker.setShowPhoneCode(false);

        // Handle Scan button click
        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedId = radioGroup.getCheckedRadioButtonId();
                RadioButton selectedRadioButton = findViewById(selectedId);
                String selectedDocument = selectedRadioButton.getText().toString();
                String selectedCountry = countryCodePicker.getSelectedCountryName();

                // Start MainActivity1 and pass selected document type
                Intent intent = new Intent(GetStartedActivity.this, CameraActivity.class);
                intent.putExtra("selectedDocument", selectedDocument);
                intent.putExtra("selectedCountry", selectedCountry);
                startActivity(intent);
            }
        });
    }
}
