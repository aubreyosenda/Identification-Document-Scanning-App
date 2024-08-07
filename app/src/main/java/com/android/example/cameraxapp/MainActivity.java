package com.android.example.cameraxapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.android.example.cameraxapp.Model.UserModel;
import com.android.example.cameraxapp.utils.FirebaseUtil;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentReference;

import java.time.LocalTime;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private ImageButton searchButton;

    private TopBar topBar;
    private BottomBar bottomBar;
    private Button buttonRegister, buttonSignOut;
    private ImageView profileImage;
    private TextView greetingText, welcomeUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        initializeViews();
        setListeners();
        setGreetingMessage();
        loadUserProfile();
    }

    private void initializeViews() {
        topBar = findViewById(R.id.top_bar);
        bottomBar = findViewById(R.id.bottom_bar);
        buttonRegister = findViewById(R.id.button_register);
        buttonSignOut = findViewById(R.id.button_signout);
        profileImage = findViewById(R.id.profileImage);
        greetingText = findViewById(R.id.greetingText);
        welcomeUsername = findViewById(R.id.welcome_username);
    }

    private void setListeners() {
        profileImage.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
            startActivity(intent);
        });

        topBar.setTitle("Home");
        topBar.setBackIconClickListener(view -> finish());
        topBar.setMenuIconClickListener(view -> {
            Toast.makeText(this, "Menu clicked", Toast.LENGTH_SHORT).show();
        });

        buttonRegister.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, SelectDocumentActivityKt.class);
            startActivity(intent);
        });

        buttonSignOut.setOnClickListener(v -> {
            Intent signOutIntent = new Intent(MainActivity.this, SignOutActivity.class);
            startActivity(signOutIntent);
        });
    }

    private void setGreetingMessage() {
        String greeting = getGreetingMessage();
        greetingText.setText(greeting);
    }

    private String getGreetingMessage() {
        int hour = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            LocalTime now = LocalTime.now();
            hour = now.getHour();
        } else {
            // For devices below API level 26
            // Fallback to using java.util.Calendar
            java.util.Calendar calendar = java.util.Calendar.getInstance();
            hour = calendar.get(java.util.Calendar.HOUR_OF_DAY);
        }

        if (hour >= 0 && hour < 12) {
            return "Good Morning,";
        } else if (hour >= 12 && hour < 16) {
            return "Good Afternoon,";
        } else if (hour >= 16 && hour < 19) {
            return "Good Evening,";
        } else {
            return "Good Night,";
        }
    }


    private void loadUserProfile() {
        DocumentReference userRef = FirebaseUtil.currentUserDetails();
        if (userRef != null) {
            userRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful() && task.getResult() != null) {
                    UserModel userModel = task.getResult().toObject(UserModel.class);
                    if (userModel != null) {
                        welcomeUsername.setText(userModel.getUsername());
                        // Load profile image using Glide
                        if (userModel.getProfileImageUri() != null) {
                            Glide.with(MainActivity.this)
                                    .load(userModel.getProfileImageUri())
                                    .into(profileImage);
                        }
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Failed to load user profile", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(MainActivity.this, "User reference is null", Toast.LENGTH_SHORT).show();
        }
    }
}
