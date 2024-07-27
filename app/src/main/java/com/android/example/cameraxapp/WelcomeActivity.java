package com.android.example.cameraxapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class WelcomeActivity extends AppCompatActivity {

    private TopBar topBar;
    private BottomBar bottomBar;
    Button button_register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        topBar = findViewById(R.id.top_bar);
        bottomBar = findViewById(R.id.bottom_bar);

        topBar.setTitle("Home");
        topBar.setBackIconClickListener(view -> finish());
        topBar.setMenuIconClickListener(view -> {
            Toast.makeText(this, "Menu clicked", Toast.LENGTH_SHORT).show();
        });


        button_register = findViewById(R.id.button_register);
        button_register.setOnClickListener(view -> {
            Intent i = new Intent(WelcomeActivity.this, SelectDocumentActivity.class);
            startActivity(i);
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}