package com.android.example.cameraxapp;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.example.cameraxapp.Interfaces.RetrofitApi;
import com.android.example.cameraxapp.Model.Vistors;


import java.sql.SQLException;
import java.text.SimpleDateFormat;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicReference;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DisplayActivity extends AppCompatActivity {
    private TopBar topBar;
    private BottomBar bottomBar;

    private TextView textNameView, textDocNoView, textPhoneNoView, selectedDocumentView, selectedCountryView;
    private static final int REQUEST_PHONE_VERIFICATION = 1;

    private static final String TAG = "API Response";

    private ProgressBar progressBar;
    String selectedFloor;
    String selectedOrganization;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

//        Get the intent
        AtomicReference<Intent> intent = new AtomicReference<>(getIntent());


        //          Set the top bar Items
        topBar = findViewById(R.id.top_bar);
        topBar.setTitle("Visitor Details");
        topBar.setBackIconClickListener(view -> {
            intent.set(new Intent(DisplayActivity.this, SelectDocumentActivity.class));
            startActivity(intent.get());
            finish();
        });
        topBar.setMenuIconClickListener(view -> {
            Toast.makeText(this, "Menu Icon clicked", Toast.LENGTH_SHORT).show();
        });

//        Set the bottom bar items
        bottomBar = findViewById(R.id.bottom_bar);


      // Initialize views

        progressBar = findViewById(R.id.Progressbar);
        progressBar.setVisibility(View.GONE);

        textNameView = findViewById(R.id.name_text);
        textDocNoView = findViewById(R.id.doc_no_text);
        textPhoneNoView = findViewById(R.id.phone_number_text);
        selectedDocumentView = findViewById(R.id.selected_document_text);

//        Buttons
        Button buttonBack = findViewById(R.id.button_retake);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) ImageView iconBack = findViewById(R.id.back_icon);
        Button buttonSave = findViewById(R.id.save_data);

//  Check firing intent activity

        String source = intent.get().getStringExtra("source");
        if (source != null){
            switch (source) {
                case "CameraActivity":
                    String extractedText = intent.get().getStringExtra("extractedText"); // Get the extracted text passed from CameraActivity
                    String documentType = intent.get().getStringExtra("selectedDocument");
                    String country = intent.get().getStringExtra("selectedCountry");
                    String phoneNumber = intent.get().getStringExtra("phoneNo");

                    textPhoneNoView.setText(phoneNumber);
                    selectedDocumentView.setText(documentType);


                    if (extractedText != null) {
                        String[] lines = extractedText.split("\n");  // Create a new array to store modified strings

                        // remove all white spaces
                        for (int i = 0; i < lines.length; i++) {
                            lines[i] = lines[i].replaceAll("\\s", "");
                        }

                        if ("ID Card".equals(documentType)){
                            getIDCardDetails(lines);
                        } else if ("Passport".equals(documentType)) {
                            getPassportDetails(lines);
                        } else if ("Driving License".equals(documentType)) {
                            Toast.makeText(DisplayActivity.this, "Selected Doc is DL ", Toast.LENGTH_SHORT).show();
                        } else {
                            intent.set(new Intent(DisplayActivity.this, SelectDocumentActivity.class));
                            startActivity(intent.get());
                            finish();
                        }

                    } else {
                        Toast.makeText(this, "No text found. Please Retake image", Toast.LENGTH_SHORT).show();
                        // Handle case where no text is passed
                        finish();
                    }

                    break;

                case "VerifyPhoneActivity":
                    String phoneNo = intent.get().getStringExtra("phoneNo");

                    selectedDocumentView.setText("No Document");
                    textPhoneNoView.setText(phoneNo);

                    break;
            }
        }

//        Spinners
        Spinner organizationSpinner = findViewById(R.id.organization_spinner);
        List<String> organizations = Arrays.asList("Select Organization","Organization 1", "Organization 2", "Organization 3");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, organizations);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        organizationSpinner.setAdapter(adapter);

        organizationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedOrganization = parent.getItemAtPosition(position).toString();
                // Handle the selected organization
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle the case where no organization is selected
            }
        });

        Spinner floorSpinner = findViewById(R.id.floor_spinner);
        List<String> floors = Arrays.asList("Select Floor", "Ground", "1st", "2nd", "3rd", "4th", "5th");

        ArrayAdapter<String> floorAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, floors);
        floorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        floorSpinner.setAdapter(floorAdapter);

        floorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedFloor = parent.getItemAtPosition(position).toString();
                // Handle the selected floor
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle the case where no floor is selected
            }
        });

        // Set onClickListener for back buttons
        buttonBack.setOnClickListener(v -> {
            intent.set(new Intent(DisplayActivity.this, SelectDocumentActivity.class));
            startActivity(intent.get());
            finish();
        });

        iconBack.setOnClickListener(v -> {
            intent.set(new Intent(DisplayActivity.this, SelectDocumentActivity.class));
            startActivity(intent.get());
            finish();
        });

//        Save data to database
        buttonSave.setOnClickListener(v -> {

//            TODO
//            get the current time (sign in)
            PostData(selectedDocumentView.getText().toString(), textDocNoView.getText().toString(),
                    textNameView.getText().toString(), textPhoneNoView.getText().toString(), selectedFloor,
                    selectedOrganization, "Not added yet");
            // Get the current time
            String tableName = "Visitor_details";
            String signInTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
            String documentType = intent.get().getStringExtra("selectedDocument");
            String fullName = textNameView.getText().toString();
            String identificationNumber = textDocNoView.getText().toString();
            String mobileNumber = textPhoneNoView.getText().toString();
            String organization = organizationSpinner.getSelectedItem().toString();
            String floor = floorSpinner.getSelectedItem().toString();

            // Print the details to the log
            Log.d("DisplayActivity", "Current Time: " + signInTime);
            Log.d("DisplayActivity", "Document Type: " + documentType);
            Log.d("DisplayActivity", "Full Name: " + fullName);
            Log.d("DisplayActivity", "Identification Number: " + identificationNumber);
            Log.d("DisplayActivity", "Mobile Number: " + mobileNumber);
            Log.d("DisplayActivity", "Organization: " + organization);
            Log.d("DisplayActivity", "Floor: " + floor);

            // Check database connection and table existence
            if (DatabaseHelper.checkDatabaseConnection()) {
                if (DatabaseHelper.checkIfTableExists(tableName)) {
                    try {
                        DatabaseHelper.insertVisitorDetails(tableName, documentType, fullName, identificationNumber, mobileNumber, organization, floor);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    Toast.makeText(DisplayActivity.this, "Table Visitor_details does not exist.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(DisplayActivity.this, "Failed to connect to the database.", Toast.LENGTH_SHORT).show();
            }
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

        textNameView.setText(sb.toString().trim());
        sb.setLength(0);

        // Get the Passport number from the last Line
        String passNoField = text[text.length - 1].substring(0,8);
        textDocNoView.setText(passNoField);

//        Toast.makeText(DisplayActivity.this, "Finished", Toast.LENGTH_SHORT).show();
    }

    @SuppressLint("SetTextI18n")
    private void getIDCardDetails(String[] lines) {
//        Get ID Card name
        String nameField = lines[lines.length - 1]; //Get last line
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
        textNameView.setText(sb.toString().trim()); // Last line in the scan
        sb.setLength(0);

//        Get ID Card Number
        String iDNoLine = lines[lines.length - 2];
        String reversedText = reverseString(iDNoLine);
        String reversedIdNo = reversedText.substring(4, 13);
        String dummyIdNo = reverseString(reversedIdNo);

        // Check if dummyIdNo starts with '0' or 'O' or 'o'
        if (dummyIdNo.startsWith("0") || dummyIdNo.startsWith("O") || dummyIdNo.startsWith("o")) {
            // Discard the first character
            textDocNoView.setText(dummyIdNo.substring(1));
        } else{
            textDocNoView.setText(dummyIdNo);
        }
    }

    public static String reverseString(String text) {
        return new StringBuilder(text).reverse().toString();
    }

    public void PostData(String selectedDocumentView, String textDocNoView,
                    String textNameView, String textPhoneNoView, String selectedFloor,
    String selectedOrganization, String vehicle){
        progressBar.setVisibility(View.VISIBLE);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://fda9-41-203-219-167.ngrok-free.app/api/v1/visitor/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitApi retrofitAPI = retrofit.create(RetrofitApi.class);

        Vistors vistors =  new Vistors(selectedDocumentView, textDocNoView,
                textNameView, textPhoneNoView, selectedFloor,
                selectedOrganization, vehicle);

        Call<Vistors> vistorsCall = retrofitAPI.vistors(vistors);

        Log.v(TAG, "API " + vistorsCall.toString());


        vistorsCall.enqueue(new Callback<Vistors>() {
            @Override
            public void onResponse(Call<Vistors> call, Response<Vistors> response) {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(DisplayActivity.this, "Data inserted successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(DisplayActivity.this, SelectDocumentActivity.class);
                startActivity(intent);
            }

            @Override
            public void onFailure(Call<Vistors> call, Throwable t) {

                Toast.makeText(DisplayActivity.this, "Error message " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });


    }
}
