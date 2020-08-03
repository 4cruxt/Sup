package com.fole_Studios.sup.database;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@SuppressWarnings("ALL")
public class DBqueries
{
    //Firebase instances
    public static FirebaseFirestore _firestore = FirebaseFirestore.getInstance();
    public static FirebaseAuth _auth = FirebaseAuth.getInstance();
    public static FirebaseUser _currentUser = _auth.getInstance().getCurrentUser();

    //Firebase instances

    //Firebase methods
    public static void setUserDataToDatabase(String username)
    {
        updateUserName(username);
        FirebaseFirestore _firestore = FirebaseFirestore.getInstance();

        Map<String, Object> _user = new HashMap<>();
        _user.put("username", _currentUser.getDisplayName());
        _user.put("user_reg_number", "");
        _user.put("user_bio", "");
        _user.put("user_course", "");
        _user.put("university", "Mbeya University of Science and Technology - MUST");
        _user.put("region", "Mbeya");
        _user.put("university_year", "1");
        _user.put("is_verified", false);

        _firestore.collection("USERS").document(_currentUser.getUid()).set(_user);
    }

    public static void updateUserName(String username)
    {
        //adding user name to this authenticated user.
        UserProfileChangeRequest _userUpdates = new UserProfileChangeRequest.Builder().setDisplayName(username).build();
        Objects.requireNonNull(_currentUser).updateProfile(_userUpdates);
    }

    public static void updateUserDataInDatabase(TextView username, TextView regNumber, TextView courseName, TextView userBio, final Context context)
    {

        Map<String, Object> _userUpdate = new HashMap<>();
        _userUpdate.put("user_reg_number", regNumber.getText().toString());
        _userUpdate.put("username", username.getText().toString());
        _userUpdate.put("user_course", courseName.getText().toString());
        _userUpdate.put("user_bio", userBio.getText().toString());

        updateUserName(username.getText().toString());

        _firestore.collection("USERS").document(_auth.getCurrentUser().getUid()).update(_userUpdate).addOnCompleteListener(new OnCompleteListener<Void>()
        {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {
                if(task.isSuccessful())
                {
                    FancyToast.makeText(context, "Profile updated", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show();
                }
                else
                {
                    FancyToast.makeText(context, "Error profile update", FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                }
            }
        });
    }

    public static void getUserDataFromDatabase(TextView username, final TextView userBio, final TextView userCourse, final TextView userReg, final Button editProButton, final ImageView verificationBadge)
    {
        //get username
        String _currentUsername = Objects.requireNonNull(_auth.getCurrentUser()).getDisplayName();
        username.setText(_currentUsername);
        String _userId = _auth.getCurrentUser().getUid();

        _firestore.collection("USERS").document(_userId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>()
        {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task)
            {
                if(task.isSuccessful())
                {
                    DocumentSnapshot _documentSnapshot = task.getResult();
                    if(Objects.requireNonNull(Objects.requireNonNull(_documentSnapshot).get("user_reg_number")).toString().length() > 0)
                    {

                        if(verificationBadge != null)
                        {
                            if(Boolean.parseBoolean(_documentSnapshot.get("is_verified").toString()))
                            {
                                verificationBadge.setVisibility(View.VISIBLE);
                            }
                            else
                            {
                                verificationBadge.setVisibility(View.INVISIBLE);
                            }
                        }
                        userReg.setText(Objects.requireNonNull(_documentSnapshot.get("user_reg_number")).toString());
                        userBio.setText(Objects.requireNonNull(_documentSnapshot.get("user_bio")).toString());
                        userCourse.setText(Objects.requireNonNull(_documentSnapshot.get("user_course")).toString());
                    }
                    else
                    {
                        if(editProButton != null)
                        {
                            editProButton.setVisibility(View.VISIBLE);
                        }
                        if(verificationBadge != null)
                        {
                            verificationBadge.setVisibility(View.INVISIBLE);
                        }
                    }
                }
                else
                {
                    Log.i("FIREBASE USER", Objects.requireNonNull(Objects.requireNonNull(task.getException()).getMessage()));
                }
            }
        });
    }


    //Firebase methods
}
