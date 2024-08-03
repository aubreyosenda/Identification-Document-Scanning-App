package com.android.example.cameraxapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class  LoginPageActivity extends AppCompatActivity {

    EditText username, password;
    Button login;
    String user, pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login_page);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);



        login = findViewById(R.id.let_me_in);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user = username.getText().toString();
                pass = password.getText().toString();
                if (user.isEmpty() && pass.isEmpty()){
                    Toast.makeText(LoginPageActivity.this, "Both the fields should be filled", Toast.LENGTH_SHORT).show();
                }
                else if( user.isEmpty()){
                    Toast.makeText(LoginPageActivity.this, "Please provide the username", Toast.LENGTH_SHORT).show();
                }
                else if (pass.isEmpty()){
                    Toast.makeText(LoginPageActivity.this, "Please provide the password", Toast.LENGTH_SHORT).show();
                }
                else{
                    Intent intent = new Intent(LoginPageActivity.this, WelcomeActivity.class);
                    startActivity(intent);
                }

            }
        });


    }
}