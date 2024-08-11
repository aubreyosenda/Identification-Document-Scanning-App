package com.android.example.cameraxapp;

import static com.android.example.cameraxapp.Interfaces.RetrofitApi.BASE_URL;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.example.cameraxapp.Interfaces.RetrofitApi;
import com.android.example.cameraxapp.Model.SignOutRequest;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class SignOutActivity extends AppCompatActivity {

    private TopBar topBar;
    private BottomBar bottomBar;
    private EditText searchBar;
    private Button backButton;
    private Button signOutButton;
    private static final String TAG = "API Response";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signout);

        // Initialize the top bar
        topBar = findViewById(R.id.top_bar);
        topBar.setTitle("Sign Out");
        topBar.setBackIconClickListener(view -> finish());
        topBar.setMenuIconClickListener(view -> {
            Toast.makeText(this, "Menu Icon clicked", Toast.LENGTH_SHORT).show();
        });

        // Initialize the search bar
        searchBar = findViewById(R.id.search_bar);

        // Initialize the buttons
        backButton = findViewById(R.id.button_back);
        signOutButton = findViewById(R.id.button_signout);

        // Set the bottom bar items
        bottomBar = findViewById(R.id.bottom_bar);

        // Set button listeners
        backButton.setOnClickListener(view -> finish());

        signOutButton.setOnClickListener(view -> {
            String documentNo = searchBar.getText().toString();
            if (documentNo.isEmpty()) {
                Toast.makeText(SignOutActivity.this, "Please enter the Document Number", Toast.LENGTH_SHORT).show();
            } else {
                signOutVisitor(documentNo);
            }
        });
    }

    private void signOutVisitor(String documentNo) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitApi retrofitAPI = retrofit.create(RetrofitApi.class);

        SignOutRequest signOutRequest = new SignOutRequest();
        signOutRequest.setDocumentNo(documentNo);
        signOutRequest.setSignedOutBy("Androi");

        Call<String> signOutCall = retrofitAPI.signOutVisitor(signOutRequest);

        Log.v(TAG, "API " + signOutCall.toString());

        signOutCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    showConfirmationDialog("Sign out successful", true);
                } else {
                    showConfirmationDialog("Sign out failed: " + response.message(), false);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e(TAG, "Error: " + t.getMessage());
                showConfirmationDialog("An error Occured: "+ t.getMessage(), false);
            }
        });
    }

    private void showConfirmationDialog(String message, boolean isSuccess) {
        new AlertDialog.Builder(this)
                .setTitle(isSuccess ? "Success" : "Failure")
                .setMessage(message)
                .setPositiveButton("OK", (dialog, which) -> {
                    if (isSuccess) {
                        Intent intent = new Intent(SignOutActivity.this, MainActivity.class);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(SignOutActivity.this, DisplayActivity.class);
                        startActivity(intent);
                    }
                    finish(); // Close the current activity
                })
                .setCancelable(false)
                .show();
    }

}
