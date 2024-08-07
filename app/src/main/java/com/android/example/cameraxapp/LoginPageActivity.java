package com.android.example.cameraxapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.hbb20.CountryCodePicker;

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
        return true;
    }

    private void performLogin(String phone, String password) {
        // Mock login operation (replace with actual login logic)
        // For demonstration, we assume login is always successful
        boolean loginSuccess = true;

        if (loginSuccess) {
            progressBar.setVisibility(View.GONE);
            Intent intent = new Intent(LoginPageActivity.this, WelcomeActivity.class);
            startActivity(intent);
            finish();
        } else {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(this, "Login failed. Please try again.", Toast.LENGTH_SHORT).show();
        }
    }
}
