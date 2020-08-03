package com.fole_Studios.sup.authentication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.fole_Studios.sup.MainActivity;
import com.fole_Studios.sup.OnBoardActivity;
import com.fole_Studios.sup.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import in.aabhasjindal.otptextview.OTPListener;
import in.aabhasjindal.otptextview.OtpTextView;

import static com.fole_Studios.sup.database.DBqueries.setUserDataToDatabase;

public class AuthActivity extends AppCompatActivity
{
    public String _userName;
    private Button _otpButton;
    private EditText _otpPassengerName, _otpUserNumber;
    private OtpTextView _otpNumberText;
    private ProgressBar _progressBar;
    private FirebaseAuth _firebaseAuth;
    private FirebaseUser _currentUser;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks _callbacks;
    private String _phone;
    private String _otpNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        _otpPassengerName = findViewById(R.id.c_auth_opt_pss_name);
        _otpButton = findViewById(R.id._otp_verifier_button);
        _otpUserNumber = findViewById(R.id.c_auth_opt_phone_number);
        _otpNumberText = findViewById(R.id.c_auth_opt_number);
        _progressBar = findViewById(R.id.c_auth_opt_progressBar);

        _firebaseAuth = FirebaseAuth.getInstance();
        _currentUser = _firebaseAuth.getCurrentUser();

        otpVerifier();
        verificationPassed();
    }

    private void otpVerifier()
    {
        _otpNumberText.setOtpListener(new OTPListener()
        {
            @Override
            public void onInteractionListener()
            {
                //fired when user types something in the OTP
            }

            @Override
            public void onOTPComplete(String otp)
            {
                //fired when user has entered the OTP fully.
                _otpNumber = otp;
            }
        });
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        if(_currentUser != null)
        {
            sendUserToHome();
        }
    }

    private void sendUserToHome()
    {
        Intent mainIntent = new Intent(AuthActivity.this, MainActivity.class);
        mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
    }

    private void sendUserToWelcome()
    {

        Intent welcomeIntent = new Intent(AuthActivity.this, OnBoardActivity.class);
        welcomeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(welcomeIntent);
        finish();
    }

    private void verificationPassed()
    {
        _otpButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                _phone = _otpUserNumber.getText().toString();
                _userName = _otpPassengerName.getText().toString();

                if(_phone.length() != 13)
                {
                    FancyToast.makeText(AuthActivity.this, "Wrong number", FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                }
                else
                {
                    _otpButton.setVisibility(View.INVISIBLE);
                    _progressBar.setVisibility(View.VISIBLE);

                    PhoneAuthProvider.getInstance().verifyPhoneNumber(_phone, 60, TimeUnit.SECONDS, TaskExecutors.MAIN_THREAD, _callbacks);
                }
            }
        });

        _callbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks()
        {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential)
            {
                String code = phoneAuthCredential.getSmsCode();

                if(code != null)
                {
                    _otpNumberText.setVisibility(View.VISIBLE);
                    _otpNumberText.setOTP(code);

                    signInWithPhoneAuthCredential(phoneAuthCredential);
                }
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e)
            {
                Log.i("FIREBASE FAILURE", Objects.requireNonNull(e.getMessage()));
                FancyToast.makeText(AuthActivity.this, "Verification failed. Try again", FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                _progressBar.setVisibility(View.INVISIBLE);
                _otpButton.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCodeSent(@NonNull final String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken)
            {
                super.onCodeSent(s, forceResendingToken);
                _progressBar.setVisibility(View.INVISIBLE);
                _otpButton.setVisibility(View.VISIBLE);
                _otpNumberText.setVisibility(View.VISIBLE);
                _otpUserNumber.setText(_phone);

                _otpButton.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        if(_otpNumber.isEmpty())
                        {
                            FancyToast.makeText(AuthActivity.this, "Enter sent code", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
                        }
                        else
                        {
                            _otpButton.setVisibility(View.INVISIBLE);
                            _progressBar.setVisibility(View.VISIBLE);
                            PhoneAuthCredential _credential = PhoneAuthProvider.getCredential(s, _otpNumber);
                            signInWithPhoneAuthCredential(_credential);
                        }
                    }
                });

            }
        };
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential)
    {
        _firebaseAuth.signInWithCredential(credential).addOnCompleteListener(AuthActivity.this, new OnCompleteListener<AuthResult>()
        {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task)
            {
                if(task.isSuccessful())
                {

                    setUserDataToDatabase(_userName);
                    sendUserToWelcome();
                }
                else
                {
                    FancyToast.makeText(AuthActivity.this, "Enter sent code", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
                }

                _progressBar.setVisibility(View.INVISIBLE);
                _otpButton.setVisibility(View.VISIBLE);
            }
        });
    }

}