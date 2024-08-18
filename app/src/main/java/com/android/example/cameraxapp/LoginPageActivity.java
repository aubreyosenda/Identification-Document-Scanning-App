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

import com.android.example.cameraxapp.Interfaces.RetrofitApi;
import com.android.example.cameraxapp.Model.SecurityPersonelDetails;
import com.hbb20.CountryCodePicker;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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
        countryCodePicker = findViewById(R.id.ccp);
        phoneNumber = findViewById(R.id.phone_number_text);
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
                performLogin(phone, pwd);
            }
        });

        // Handle forgot password click
        forgotPassword.setOnClickListener(view -> {
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
        // Create a Retrofit instance
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RetrofitApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Create an instance of the RetrofitApi
        RetrofitApi retrofitApi = retrofit.create(RetrofitApi.class);

        // Call the login method
        Call<SecurityPersonelDetails> call = retrofitApi.login(phone, password);

        // Enqueue the call to execute asynchronously
        call.enqueue(new Callback<SecurityPersonelDetails>() {
            @Override
            public void onResponse(Call<SecurityPersonelDetails> call, Response<SecurityPersonelDetails> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    // Handle successful login response
                    SecurityPersonelDetails details = response.body();

                    // Save user details
                    saveLoginDetails(details);

                    // Navigate to MainActivity
                    Intent intent = new Intent(LoginPageActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    // Handle unsuccessful response
                    Toast.makeText(LoginPageActivity.this, "Login failed. Please check your credentials.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SecurityPersonelDetails> call, Throwable t) {
                // Handle failure in making the request
                progressBar.setVisibility(View.GONE);
                Toast.makeText(LoginPageActivity.this, "Login failed. Please try again.", Toast.LENGTH_SHORT).show();
                Log.e("LoginError", t.getMessage(), t);
            }
        });
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
