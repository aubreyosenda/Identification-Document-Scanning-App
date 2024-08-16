package com.android.example.cameraxapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.example.cameraxapp.Model.SecurityPersonelDetails;
import com.hbb20.CountryCodePicker;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginPageActivity extends AppCompatActivity {

    private CountryCodePicker countryCodePicker;
    private EditText phoneNumber, password;
    private CheckBox togglePasswordVisibility;
    private Button letMeInButton;
    private ProgressBar progressBar;
    private TextView forgotPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        // Set the title of the top bar
        TopBar topBar = findViewById(R.id.top_bar);
        topBar.setTitle("Login");

        // Initialize UI components
        countryCodePicker = findViewById(R.id.country_code_picker);
        phoneNumber = findViewById(R.id.phone_number);
        password = findViewById(R.id.input_password);
        togglePasswordVisibility = findViewById(R.id.toggle_password_visibility);
        letMeInButton = findViewById(R.id.let_me_in);
        progressBar = findViewById(R.id.Progressbar);
        forgotPassword = findViewById(R.id.forgot_password);

        // Toggle password visibility
        togglePasswordVisibility.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                // Show password
                password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            } else {
                // Hide password
                password.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
        });

        // Handle login button click
        letMeInButton.setOnClickListener(view -> {
            String phone = phoneNumber.getText().toString().trim();
            String pwd = password.getText().toString().trim();

            if (validateInputs(phone, pwd)) {
                progressBar.setVisibility(View.VISIBLE);
                // Perform login operation (e.g., network request)
                performLogin(phone, pwd);
            }
        });

        // Handle forgot password click
        forgotPassword.setOnClickListener(view -> {
            // Handle forgot password functionality
            Toast.makeText(LoginPageActivity.this, "Forgot Password Clicked", Toast.LENGTH_SHORT).show();
        });
    }

    private boolean validateInputs(String phone, String password) {
        if (phone.isEmpty()) {
            Toast.makeText(this, "Please enter phone number", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (password.isEmpty()) {
            Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show();
            return false;
        }
        Log.d("Phone Number", phone);
        Log.d("Password", password);
        return true;
    }

    private void performLogin(String phone, String password) {
        // Define the URL for the login request
        String url = "https://e46a-41-203-219-167.ngrok-free.app/api/v1/company/personnel/login?phoneNumber=" + phone + "&nationalIdNumber=" + password;

        // Create a new thread to handle the network request
        new Thread(() -> {
            try {
                // Perform the network request
                URL loginUrl = new URL(url);
                HttpURLConnection connection = (HttpURLConnection) loginUrl.openConnection();
                connection.setRequestMethod("GET");

                // Read the response
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder responseBuilder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    responseBuilder.append(line);
                }
                reader.close();

                // Convert response to JSON
                String response = responseBuilder.toString();
                Log.d("LoginResponse", response);

                // Parse the JSON response
                JSONObject jsonResponse = new JSONObject(response);
                int id = jsonResponse.getInt("id");
                String fullName = jsonResponse.getString("fullName");
                long nationalIdNumber = jsonResponse.getLong("nationalIdNumber");
                String workIdNumber = jsonResponse.getString("workIdNumber");
                long phoneNumber = jsonResponse.getLong("phoneNumber");
                String buildingId = jsonResponse.getString("buildingId");
                String buildingName = jsonResponse.getString("buildingName");
                String companyID = jsonResponse.getString("companyId");
                String companyName = jsonResponse.getString("companyName");

                // Create a SecurityPersonelDetails object
                SecurityPersonelDetails details = new SecurityPersonelDetails(
                        id, fullName, nationalIdNumber, workIdNumber, phoneNumber, buildingId, buildingName, companyID, companyName
                );

                // Save user details
                runOnUiThread(() -> {
                    saveLoginDetails(details);
                    progressBar.setVisibility(View.GONE);
                    Intent intent = new Intent(LoginPageActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                });

            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(LoginPageActivity.this, "Login failed. Please try again.", Toast.LENGTH_SHORT).show();
                });
            }
        }).start();
    }


    private void saveLoginDetails(SecurityPersonelDetails details) {
        SharedPreferences sharedPreferences = getSharedPreferences("SecurityPersonnelPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("id", details.getId());
        editor.putString("fullName", details.getFullName());
        editor.putLong("nationalIdNumber", details.getNationalIdNumber());
        editor.putString("workIdNumber", details.getWorkIdNumber());
        editor.putLong("phoneNumber", details.getPhoneNumber());
        editor.putString("buildingId", details.getBuildingId());
        editor.putString("buildingName", details.getBuildingName());
        editor.putString("companyId", details.getCompanyId());
        editor.putString("companyName", details.getCompanyName());
        editor.apply();
    }

}
