package com.android.example.cameraxapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.example.cameraxapp.utils.AndroidUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.hbb20.CountryCodePicker;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

public class PhonenumberOtpActivity extends AppCompatActivity {

    private TopBar topBar;
    private BottomBar bottomBar;
    String PhoneNumber;
    Long timeoutseconds = 60l;
    EditText otpcode;
    String verification;
    PhoneAuthProvider.ForceResendingToken resendingToken;
    ProgressBar Progresbar;
    TextView resendotp;
    Button verify_button;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    String country, countryCodePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phonenumber_otp);

        topBar = findViewById(R.id.top_bar);
        bottomBar = findViewById(R.id.bottom_bar);
        AtomicReference<Intent> intent = new AtomicReference<>(getIntent());
        country = intent.get().getStringExtra("selectedCountry");
        countryCodePicker = intent.get().getStringExtra("phoneNo");

//        Top bar events on the verify phone activity
        topBar.setTitle("Verify Phone");
        topBar.setBackIconClickListener(view -> finish());
        topBar.setMenuIconClickListener(view -> {
            //Toast.makeText(this, "Menu Icon clicked", Toast.LENGTH_SHORT).show();
        });

//        Set the bottom bar items
        bottomBar = findViewById(R.id.bottom_bar);

        otpcode = findViewById(R.id.otpcode);
        verify_button = findViewById(R.id.verify_button);
        Progresbar = findViewById(R.id.Progressbar);
        resendotp = findViewById(R.id.resendotp);


        PhoneNumber = getIntent().getExtras().getString("phoneNo");
        sendOtp(PhoneNumber,false);
        verify_button.setOnClickListener(v -> {
            String enterOtp = otpcode.getText().toString();
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verification,enterOtp);
            signIn(credential);
            setInProgress(true);

        });

        resendotp.setOnClickListener((v)->{
            sendOtp(PhoneNumber,true);
        });


    }

    // Force reCAPTCHA flow
    //FirebaseAuth.getFirebaseAuthSettings().forceRecaptchaFlowForTesting();
    void sendOtp(String phoneNumber, boolean isResend){
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
                                signIn(phoneAuthCredential);
                                verify_button.setVisibility(View.VISIBLE);
                                setInProgress(false);

                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                verify_button.setVisibility(View.INVISIBLE);
                                AndroidUtil.showToast(getApplicationContext(),"OTP verification failed" + e);
                                setInProgress(false);

                            }

                            @Override
                            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                super.onCodeSent(s, forceResendingToken);
                                verification = s;
                                resendingToken = forceResendingToken;
                                verify_button.setVisibility(View.VISIBLE);
                                AndroidUtil.showToast(getApplicationContext(),"OTP successfully "+ s);
                                setInProgress(false);
                            }
                        });
        if (isResend){
            PhoneAuthProvider.verifyPhoneNumber(builder.setForceResendingToken(resendingToken).build());
        }else {
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
                resendotp.setText("Resend OTP in "+timeoutseconds+" seconds");
                if (timeoutseconds<=0){
                    timeoutseconds = 60L;
                    timer.cancel();
                    runOnUiThread(()->{
                        resendotp.setEnabled(true);
                    });

                }

            }
        }, 0, 1000);
    }

    private void signIn(PhoneAuthCredential phoneAuthCredential) {
        //login and go next activity
        setInProgress(true);
        mAuth.signInWithCredential(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                setInProgress(false);
                if(task.isSuccessful()){
                    Intent intent = new Intent(PhonenumberOtpActivity.this, DisplayActivity.class);
                    intent.putExtra("source", "VerifyPhoneActivity");
                    intent.putExtra("phoneNo", countryCodePicker);
                    intent.putExtra("selectedCountry", country);
                    startActivity(intent);
                }else{
                    AndroidUtil.showToast(getApplicationContext(),"Otp verrification Failed");
                    verify_button.setVisibility(View.VISIBLE);
                }
            }

        });
    }

    void setInProgress(boolean inProgress){
        if (inProgress){
            Progresbar.setVisibility(View.VISIBLE);
            verify_button.setVisibility(View.GONE);
        } else {
            Progresbar.setVisibility(View.GONE);
            //verify_button.setVisibility(View.VISIBLE);
        }
    }
}