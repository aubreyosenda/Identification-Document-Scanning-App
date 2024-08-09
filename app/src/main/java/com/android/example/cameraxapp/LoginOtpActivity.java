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

import com.android.example.cameraxapp.utils.AndroidUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class LoginOtpActivity extends AppCompatActivity {
    String PhoneNumber;
    Long timeoutseconds = 60l;
    EditText otpcode;
    String verification;
    PhoneAuthProvider.ForceResendingToken resendingToken;
    ProgressBar Progresbar;
    TextView resendotp;
    Button verify_button;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login_otp);

        otpcode = findViewById(R.id.otpcode);
        verify_button = findViewById(R.id.verify_button);
        Progresbar = findViewById(R.id.Progressbar);
        resendotp = findViewById(R.id.resendotp);


        PhoneNumber = getIntent().getExtras().getString("phone");
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
                                setInProgress(false);

                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                AndroidUtil.showToast(getApplicationContext(),"OTP verification failed");
                                setInProgress(false);

                            }

                            @Override
                            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                super.onCodeSent(s, forceResendingToken);
                                verification = s;
                                resendingToken = forceResendingToken;
                                AndroidUtil.showToast(getApplicationContext(),"OTP successfully");
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
                    Intent intent = new Intent(LoginOtpActivity.this, LoginUsernameActivity.class);
                    intent.putExtra("phone", PhoneNumber);
                    startActivity(intent);
                }else{
                    AndroidUtil.showToast(getApplicationContext(),"Otp verrification Failed");
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
            verify_button.setVisibility(View.VISIBLE);
        }
    }
}







