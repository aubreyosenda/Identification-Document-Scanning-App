package com.android.example.cameraxapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

public class LoginUsernameActivity extends AppCompatActivity {

    EditText username;
    Button let_me_in;
    ProgressBar ProgressBar;
    String phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_username);

        username = findViewById(R.id.username);
        let_me_in = findViewById(R.id.let_me_in);
        ProgressBar = findViewById(R.id.Progressbar);
    }

    void setInProgress(boolean inProgress){
        if(inProgress){
            ProgressBar.setVisibility(View.VISIBLE);
            let_me_in.setVisibility(View.GONE);
        }else{
            ProgressBar.setVisibility(View.GONE);
            let_me_in.setVisibility(View.VISIBLE);
        }
    }
}