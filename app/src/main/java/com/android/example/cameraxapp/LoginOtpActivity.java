package com.android.example.cameraxapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.example.cameraxapp.utils.AndroidUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class LoginOtpActivity extends AppCompatActivity {
    String PhoneNumber;
    Long timeoutseconds = 60L;
    EditText otpcode;
    String verification;
    PhoneAuthProvider.ForceResendingToken resendingToken;
    ProgressBar Progresbar;
    TextView resendotp;
    Button verify_button, openDatabaseActivityButton;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login_otp);

        // Initialize views
        otpcode = findViewById(R.id.otpcode);
        verify_button = findViewById(R.id.verify_button);
        openDatabaseActivityButton = findViewById(R.id.open_database_activity_button);
        Progresbar = findViewById(R.id.Progressbar);
        resendotp = findViewById(R.id.resendotp);

        // Retrieve user details from Intent
        String name = getIntent().getStringExtra("name");
        String idNumber = getIntent().getStringExtra("idNumber");
        String selectedDocument = getIntent().getStringExtra("selectedDocument");
        String selectedCountry = getIntent().getStringExtra("selectedCountry");
        PhoneNumber = getIntent().getStringExtra("phone");

        // Log the phone number
        Log.d("PhoneVerification", "Full Phone Number: " + PhoneNumber);

        // Reference TextViews using their IDs
        TextView nameTextView = findViewById(R.id.name_text);
        TextView idNumberTextView = findViewById(R.id.id_number_text);
        TextView documentTextView = findViewById(R.id.document_text);
        TextView countryTextView = findViewById(R.id.country_text);
        TextView phoneTextView = findViewById(R.id.phone_text); // For phone number

        // Set text for TextViews
        nameTextView.setText("Name: " + name);
        idNumberTextView.setText("ID No: " + idNumber);
        documentTextView.setText("Document: " + selectedDocument);
        countryTextView.setText("Country: " + selectedCountry);
        phoneTextView.setText("Phone: " + PhoneNumber); // Display phone number

        // Set up button listeners and other logic as before
        openDatabaseActivityButton.setOnClickListener(v -> {
            Intent intent = new Intent(LoginOtpActivity.this, DatabaseActivity.class);
            intent.putExtra("name", name);
            intent.putExtra("idNumber", idNumber);
            intent.putExtra("selectedDocument", selectedDocument);
            intent.putExtra("selectedCountry", selectedCountry);
            intent.putExtra("phone", PhoneNumber);
            intent.putExtra("signUpTime", getCurrentTimestamp());
            startActivity(intent);
        });

        sendOtp(PhoneNumber, false);

        verify_button.setOnClickListener(v -> {
            String enterOtp = otpcode.getText().toString();
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verification, enterOtp);
            signIn(credential, name, idNumber, selectedDocument, selectedCountry, PhoneNumber);
            setInProgress(true);
        });

        resendotp.setOnClickListener((v) -> {
            sendOtp(PhoneNumber, true);
        });
    }

    void sendOtp(String phoneNumber, boolean isResend) {
        startResendTimer();
        setInProgress(true);
        PhoneAuthOptions.Builder builder =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(PhoneNumber)
                        .setTimeout(timeoutseconds, TimeUnit.SECONDS)
                        .setActivity(this)
                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                // We need to pass all the required parameters to the signIn method
                                String name = getIntent().getStringExtra("name");
                                String idNumber = getIntent().getStringExtra("idNumber");
                                String selectedDocument = getIntent().getStringExtra("selectedDocument");
                                String selectedCountry = getIntent().getStringExtra("selectedCountry");
                                String phone = getIntent().getStringExtra("phone");

                                signIn(phoneAuthCredential, name, idNumber, selectedDocument, selectedCountry, phone);
                                setInProgress(false);
                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                AndroidUtil.showToast(getApplicationContext(), "OTP verification failed");
                                setInProgress(false);
                            }

                            @Override
                            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                super.onCodeSent(s, forceResendingToken);
                                verification = s;
                                resendingToken = forceResendingToken;
                                AndroidUtil.showToast(getApplicationContext(), "OTP sent successfully");
                                setInProgress(false);
                            }
                        });
        if (isResend) {
            PhoneAuthProvider.verifyPhoneNumber(builder.setForceResendingToken(resendingToken).build());
        } else {
            PhoneAuthProvider.verifyPhoneNumber(builder.build());
        }
    }

    private void startResendTimer() {
        resendotp.setEnabled(false);
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                timeoutseconds--;
                resendotp.setText("Resend OTP in " + timeoutseconds + " seconds");
                if (timeoutseconds <= 0) {
                    timeoutseconds = 60L;
                    timer.cancel();
                    runOnUiThread(() -> resendotp.setEnabled(true));
                }
            }
        }, 0, 1000);
    }

    private void signIn(PhoneAuthCredential phoneAuthCredential, String name, String idNumber, String document, String country, String phone) {
        setInProgress(true);
        mAuth.signInWithCredential(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                setInProgress(false);
                if (task.isSuccessful()) {
                    Intent intent = new Intent(LoginOtpActivity.this, DatabaseActivity.class);
                    intent.putExtra("name", name);
                    intent.putExtra("idNumber", idNumber);
                    intent.putExtra("selectedDocument", document);
                    intent.putExtra("selectedCountry", country);
                    intent.putExtra("phone", phone);
                    intent.putExtra("signUpTime", getCurrentTimestamp());
                    startActivity(intent);
                } else {
                    AndroidUtil.showToast(getApplicationContext(), "OTP verification failed");
                }
            }
        });
    }

    void setInProgress(boolean inProgress) {
        if (inProgress) {
            Progresbar.setVisibility(View.VISIBLE);
            verify_button.setVisibility(View.GONE);
        } else {
            Progresbar.setVisibility(View.GONE);
            verify_button.setVisibility(View.VISIBLE);
        }
    }

    private String getCurrentTimestamp() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
    }
}
