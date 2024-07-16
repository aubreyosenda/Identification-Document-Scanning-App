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
//        Buttons
        RadioButton radio_id_card = findViewById(R.id.radio_id_card);
        RadioButton radio_passport = findViewById(R.id.radio_passport);
        RadioButton radio_driving_license = findViewById(R.id.radio_driving_license);


        // Set default country (optional, as it's set in XML)
        countryCodePicker.setDefaultCountryUsingNameCode("KE");
        countryCodePicker.showFullName(true);
        countryCodePicker.setShowPhoneCode(false);

        // Handle Scan button click
        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!radio_id_card.isChecked() && !radio_passport.isChecked() && !radio_driving_license.isChecked()){
                    Toast.makeText(GetStartedActivity.this, "Please select a document", Toast.LENGTH_SHORT).show();
                } else{
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
            }
        });
    }
}
