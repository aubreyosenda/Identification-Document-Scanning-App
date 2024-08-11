package com.android.example.cameraxapp;


import static com.android.example.cameraxapp.Interfaces.RetrofitApi.BASE_URL;

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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.example.cameraxapp.Interfaces.RetrofitApi;
import com.android.example.cameraxapp.Model.Vistors;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

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
            intent.set(new Intent(DisplayActivity.this, RegistrationActivity.class));
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
                        } else {
                            intent.set(new Intent(DisplayActivity.this, RegistrationActivity.class));
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
            intent.set(new Intent(DisplayActivity.this, RegistrationActivity.class));
            startActivity(intent.get());
            finish();
        });

        iconBack.setOnClickListener(v -> {
            intent.set(new Intent(DisplayActivity.this, RegistrationActivity.class));
            startActivity(intent.get());
            finish();
        });

        //        Post data to API on Clicking "save button"
        buttonSave.setOnClickListener(v -> {

            PostData(selectedDocumentView.getText().toString(), textDocNoView.getText().toString(),
                    textNameView.getText().toString(), textPhoneNoView.getText().toString(), selectedFloor,
                    selectedOrganization, "Null");
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
                         String selectedOrganization, String vehicle) {

        progressBar.setVisibility(View.VISIBLE);

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                    @Override
                    public void log(String message) {
                        Log.d("OkHttp", message);
                    }
                }).setLevel(HttpLoggingInterceptor.Level.HEADERS)) // Log headers as well
                .build();

        // Add ScalarsConverterFactory to handle plain text responses
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(ScalarsConverterFactory.create()) // For plain text responses
                .addConverterFactory(GsonConverterFactory.create(gson)) // For JSON responses
                .build();

        RetrofitApi retrofitAPI = retrofit.create(RetrofitApi.class);

        Vistors vistors = new Vistors(selectedDocumentView, textDocNoView,
                textNameView, textPhoneNoView, selectedFloor,
                selectedOrganization, vehicle);

        Call<String> vistorsCall = retrofitAPI.registerVisitor(vistors);

        Log.v(TAG, "API " + vistorsCall.toString());

        vistorsCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                progressBar.setVisibility(View.INVISIBLE);
                if (response.isSuccessful()) {
                    String successMsg = response.body();
                    Log.v(TAG, "Full response body: " + successMsg);
                    showConfirmationDialog("Visitor Registered Successfully", true);
                } else {
                    try {
                        String errorBody = response.errorBody().string();
                        Log.e(TAG, "Error body: " + errorBody);
                        showConfirmationDialog("Registration failed: "+ errorBody, false);
                    } catch (Exception e) {
                        Log.e(TAG, "Error reading errorBody", e);
                        showConfirmationDialog("Registration failed: \n" + response.message(), false);
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                progressBar.setVisibility(View.INVISIBLE);
                Log.e(TAG, "Failure message: " + t.getMessage());
                showConfirmationDialog("Error: "+ t.getMessage(), false);
            }
        });
    }

    private void showConfirmationDialog(String message, boolean isSuccess) {
        new AlertDialog.Builder(this)
                .setTitle(isSuccess ? "Success" : "Failure")
                .setMessage(message)
                .setPositiveButton("OK", (dialog, which) -> {
                    if (isSuccess) {
                        Intent intent = new Intent(this, MainActivity.class);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(this, DisplayActivity.class);
                        startActivity(intent);
                    }
                    finish(); // Close the current activity
                })
                .setCancelable(false)
                .show();
    }
}
