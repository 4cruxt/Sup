package com.fole_Studios.sup.authentication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.chivorn.smartmaterialspinner.SmartMaterialSpinner;
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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.ArrayList;
import java.util.Arrays;
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
    private Button _newUserButton;
    private ArrayList<String> _universityList;
    private SmartMaterialSpinner<String> _universitySpinner;
    private ArrayList<String> _courseList;
    private SmartMaterialSpinner<String> _courseSpinner;
    private SmartMaterialSpinner<String> _yearSpinner;
    private ArrayList<String> _yearList;
    private FirebaseFirestore _firestore;
    private String _selectedUniversity;
    private String _selectedCourse;
    private String _selectedYear;

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
        _newUserButton = findViewById(R.id.c_auth_opt_new_user_button);
        _universitySpinner = findViewById(R.id.c_auth_university_spinner);
        _courseSpinner = findViewById(R.id.c_auth_course_spinner);
        _yearSpinner = findViewById(R.id.c_auth_year_spinner);

        _firebaseAuth = FirebaseAuth.getInstance();
        _currentUser = _firebaseAuth.getCurrentUser();

        _firestore = FirebaseFirestore.getInstance();

        _otpButton.setVisibility(View.INVISIBLE);

        otpVerifier();
        verificationPassed();
        userBehavior();
        setupSpinner();
    }

    private void userBehavior()
    {
        _newUserButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                _newUserButton.setVisibility(View.INVISIBLE);
                _otpPassengerName.setVisibility(View.VISIBLE);
            }
        });
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
                if(!otp.isEmpty())
                {
                    _otpNumber = otp;
                }
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
                    _otpNumberText.setOTP(code);
                    _otpButton.setVisibility(View.INVISIBLE);
                    _progressBar.setVisibility(View.VISIBLE);
                    signInWithPhoneAuthCredential(phoneAuthCredential);
                }
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e)
            {
                Log.i("FIREBASE FAILURE", Objects.requireNonNull(e.getMessage()));
                if(e.getMessage().contains("We have blocked all requests from this device due to unusual activity. Try again later"))
                {
                    FancyToast.makeText(AuthActivity.this, "Device broken due to unusual activity. try again later", FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                }
                else if(e.getMessage().contains("A network error (such as timeout, interrupted connection or unreachable host) has occurred"))
                {
                    FancyToast.makeText(AuthActivity.this, "Network Error!", FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                }
                else
                {
                    FancyToast.makeText(AuthActivity.this, "Verification failed. Try again", FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                }
                _progressBar.setVisibility(View.INVISIBLE);
                _otpButton.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCodeSent(@NonNull final String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken)
            {
                super.onCodeSent(s, forceResendingToken);
                _progressBar.setVisibility(View.INVISIBLE);
                _otpButton.setVisibility(View.VISIBLE);
                _otpUserNumber.setText(_phone);

                if(_otpNumberText.getVisibility() == View.VISIBLE && _otpNumber != null)
                {
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
                    //Sign in success, update UI with the signed-in user's information.
                    FirebaseUser _user = Objects.requireNonNull(task.getResult()).getUser();
                    long creationTimestamp = Objects.requireNonNull(Objects.requireNonNull(_user).getMetadata()).getCreationTimestamp();
                    long lastLoginTimestamp = Objects.requireNonNull(Objects.requireNonNull(_user).getMetadata()).getLastSignInTimestamp();

                    if(creationTimestamp == lastLoginTimestamp)
                    {
                        //Create a new user with account
                        setUserDataToDatabase(_userName);
                        sendUserToWelcome();
                    }
                    else
                    {
                        //User exists, just login
                        sendUserToHome();
                    }
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

    private void initSpinner(final SmartMaterialSpinner<String> spinner, final ArrayList<String> dataList)
    {


        spinner.setItem(dataList);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id)
            {
                //Display selected item.
                Toast.makeText(AuthActivity.this, dataList.get(position), Toast.LENGTH_SHORT).show();

                if(spinner.getId() == _universitySpinner.getId())
                {
                    _selectedUniversity = dataList.get(position);
                    _courseSpinner.setEnabled(true);
                    courseSpinner();
                }
                else if(spinner.getId() == _courseSpinner.getId())
                {
                    _selectedCourse = dataList.get(position);
                    _yearSpinner.setEnabled(true);
                }
                else if(spinner.getId() == _yearSpinner.getId())
                {
                    _selectedYear = dataList.get(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {

            }
        });
    }

    private void setupSpinner()
    {
        _courseSpinner.setEnabled(false);
        _yearSpinner.setEnabled(false);
        universitySpinner();
        yearSpinner();
    }

    private void yearSpinner()
    {
        yearDataList();
        initSpinner(_yearSpinner, _yearList);
    }

    private void yearDataList()
    {
        _yearList = new ArrayList<>();

        _yearList.add("Diploma - 1");
        _yearList.add("Diploma - 2");
        _yearList.add("Diploma - 3");
        _yearList.add("Bachelor - 1");
        _yearList.add("Bachelor - 2");
        _yearList.add("Bachelor - 3");
        _yearList.add("Bachelor - 4");
    }

    private void courseSpinner()
    {
        courseDataList();
        initSpinner(_courseSpinner, _courseList);
    }

    private void courseDataList()
    {
        _courseList = new ArrayList<>();

        if(_selectedUniversity != null)
        {
            int _startIndex = _selectedUniversity.indexOf("-");
            int _endIndex = _selectedUniversity.length();
            final String _uni = _selectedUniversity.substring(_startIndex, _endIndex).replace("-", "").replaceAll(" ", "").toUpperCase();


            _firestore.collection("COURSES").document(_uni).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>()
            {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task)
                {
                    if(task.isSuccessful())
                    {
                        DocumentSnapshot _documentSnapshot = task.getResult();
                        assert _documentSnapshot != null;
                        String _str = Objects.requireNonNull(Objects.requireNonNull(_documentSnapshot.get("courses")).toString().replace("[", "").replace("]", ""));
                        String[] _arr = _str.split(",");
                        _courseList.addAll(Arrays.asList(_arr));
                    }
                }
            });

        }

    }

    private void universitySpinner()
    {
        universityDataList();
        initSpinner(_universitySpinner, _universityList);
    }

    private void universityDataList()
    {
        _universityList = new ArrayList<>();

        _firestore.collection("COURSES").orderBy("universities").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
        {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task)
            {
                if(task.isSuccessful())
                {
                    for(QueryDocumentSnapshot _documentSnapshot : Objects.requireNonNull(task.getResult()))
                    {
                        String _str = Objects.requireNonNull(_documentSnapshot.get("universities")).toString().replace("[", "").replace("]", "");
                        String[] _arr = _str.split(",");

                        _universityList.addAll(Arrays.asList(_arr));
                    }
                }
            }
        });
    }

}