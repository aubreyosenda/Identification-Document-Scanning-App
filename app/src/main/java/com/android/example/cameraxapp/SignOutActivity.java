package com.android.example.cameraxapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.example.cameraxapp.Interfaces.RetrofitApi;
import com.android.example.cameraxapp.Model.SignOutRequest;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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
        // Show a loading indicator, if needed
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://1457-102-219-208-45.ngrok-free.app/api/v1/visitor/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitApi retrofitAPI = retrofit.create(RetrofitApi.class);

        SignOutRequest signOutRequest = new SignOutRequest();
        signOutRequest.setDocumentNo(documentNo);
        signOutRequest.setSignedOutBy("AndroidApp"); // You can set this based on user context if needed

        Call<String> signOutCall = retrofitAPI.signOutVisitor(signOutRequest);

        Log.v(TAG, "API " + signOutCall.toString());

        signOutCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(SignOutActivity.this, "Signed out successfully", Toast.LENGTH_SHORT).show();
                    // Redirect to some other activity, if needed
                    Intent intent = new Intent(SignOutActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(SignOutActivity.this, "Sign out failed: " + response.message(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(SignOutActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
