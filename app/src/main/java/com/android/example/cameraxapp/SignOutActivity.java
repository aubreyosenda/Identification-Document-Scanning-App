package com.android.example.cameraxapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SignOutActivity extends AppCompatActivity {

    private TopBar topBar;
    private BottomBar bottomBar;
    private EditText searchBar;
    private Button backButton;
    private Button signOutButton;

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
            // Sign out logic
            Toast.makeText(SignOutActivity.this, "{Username} Signed out", Toast.LENGTH_SHORT).show();
            // Redirect to some other activity, if needed
            Intent intent = new Intent(SignOutActivity.this, WelcomeActivity.class);
            startActivity(intent);
            finish();
        });
    }
}
