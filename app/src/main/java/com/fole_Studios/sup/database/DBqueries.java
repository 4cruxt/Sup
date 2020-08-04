package com.fole_Studios.sup.database;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.fole_Studios.sup.adapters.EventAdapter;
import com.fole_Studios.sup.models.EventFeatured;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Calendar;
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

    public static void getUniversityEvents(final EventAdapter _adapter, final ArrayList<EventFeatured> _eventFeatured, final ProgressBar progressBar)
    {
        String _userId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        final String[] _university = new String[1];

        _firestore.collection("USERS").document(_userId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>()
        {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task)
            {
                if(task.isSuccessful())
                {

                    //Find the correspond events within the given university.
                    _firestore.collection(getUniversityInitials(task, _university)).orderBy("date").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
                    {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task)
                        {
                            if(task.isSuccessful())
                            {
                                progressBar.setVisibility(View.INVISIBLE);
                                for(QueryDocumentSnapshot _documentSnapshot : Objects.requireNonNull(task.getResult()))
                                {
                                    _eventFeatured.add(new EventFeatured(Objects.requireNonNull(_documentSnapshot.get("poster")).toString(), Objects.requireNonNull(_documentSnapshot.get("name")).toString(), Objects.requireNonNull(_documentSnapshot.get("date")).toString().substring(0, 2), Objects.requireNonNull(_documentSnapshot.get("date")).toString().substring(3, 6)));
                                }

                                _adapter.notifyDataSetChanged();
                            }
                            else
                            {
                                progressBar.setVisibility(View.VISIBLE);
                                Log.e("FIREBASE QUERY ERROR", Objects.requireNonNull(Objects.requireNonNull(task.getException()).getMessage()));
                            }

                        }
                    });

                }
                else
                {
                    Log.e("UNIVERSITY FAILURE", Objects.requireNonNull(Objects.requireNonNull(task.getException()).getMessage()));
                }
            }
        });
    }

    public static String getUniversityInitials(Task task, String[] _university)
    {
        //Get initials from current user's university
        DocumentSnapshot _documentSnapshot = (DocumentSnapshot) task.getResult();
        String _universityName = Objects.requireNonNull(Objects.requireNonNull(_documentSnapshot).get("university")).toString();
        int _startIndex = _universityName.indexOf("-");
        int _endIndex = _universityName.length();
        _university[0] = _universityName.substring(_startIndex, _endIndex).replace("-", "").replaceAll(" ", "").toUpperCase();

        return _university[0];
    }

    public static String getUserUniversityYear(Task task)
    {
        DocumentSnapshot _documentSnapshot = (DocumentSnapshot) task.getResult();
        String _universityYear = _documentSnapshot.get("university_year").toString();

        return _universityYear;
    }

    public static String getUserUniversityCourse(Task task)
    {
        DocumentSnapshot _documentSnapshot = (DocumentSnapshot) task.getResult();
        String _universityCourseData = _documentSnapshot.get("user_course").toString();
        int _universityCourseIndex = getCapsNameCourse(_universityCourseData).lastIndexOf(" ");
        String _universityCourse = getCapsNameCourse(_universityCourseData).substring(0, _universityCourseIndex);

        return _universityCourse;
    }

    private static String getCapsNameCourse(String universityCourseData)
    {
        //Create a character array of the given university
        char[] _char = universityCourseData.toCharArray();

        for(int i = 0; i < universityCourseData.length(); i++)
        {
            //If first character of a word is found
            if(i == 0 && _char[i] != ' ' || _char[i] != ' ' && _char[i - 1] == ' ')
            {
                //If it is in lower-case
                if(_char[i] >= 'a' && _char[i] <= 'z')
                {
                    //Convert into Upper-case
                    _char[i] = (char) (_char[i] - 'a' + 'A');
                }
            }

            //If apart from first character
            //Any one is in Upper-case
            else if(_char[i] >= 'A' && _char[i] <= 'Z')
            {
                //Convert into Lower-Case
                _char[i] = (char) (_char[i] + 'a' - 'A');
            }
        }

        //Convert the char array to equivalent String
        String _uniCourse = new String(_char);
        return _uniCourse;
    }

    public static void getuserCourseInformation(final TextView exam, final TextView project, final TextView module)
    {
        String userId = _currentUser.getUid();

        _firestore.collection("USERS").document(userId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>()
        {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task)
            {
                if(task.isSuccessful())
                {
                    String _univCourse = getUserUniversityCourse(task);
                    String _univInitialsAndYear = getUniInitialsAndYear(task);


                    _firestore.collection(_univInitialsAndYear).document(_univCourse).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>()
                    {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task)
                        {
                            if(task.isSuccessful())
                            {
                                DocumentSnapshot _documentSnap = task.getResult();

                                exam.setText(Objects.requireNonNull(Objects.requireNonNull(_documentSnap).get("exams")).toString());
                                module.setText(Objects.requireNonNull(_documentSnap.get("modules")).toString());
                                project.setText(Objects.requireNonNull(_documentSnap.get("projects")).toString());
                            }
                            else
                            {
                                Log.i("DASH INFO QUERY", Objects.requireNonNull(Objects.requireNonNull(task.getException()).getMessage()));
                            }
                        }
                    });
                }
                else
                {
                    Log.i("DASHBOARD FIREBASE", Objects.requireNonNull(Objects.requireNonNull(task.getException()).getMessage()));
                }
            }
        });
    }

    public static String getCurrentTime()
    {
        Calendar _calendar = Calendar.getInstance();
        OffsetDateTime _offsetDateTime = OffsetDateTime.now();

        String[] _monthName = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sept", "Oct", "Nov", "Dec"};
        String[] _dayName = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};

        String _month = _monthName[_calendar.get(Calendar.MONTH)];

        String _day = getCapsNameCourse(String.valueOf(_offsetDateTime.getDayOfWeek())).substring(0, 3);

        int _year = _calendar.get(Calendar.YEAR);
        int _date = _calendar.get(Calendar.DATE);

        String _timeline = _day + ", " + _month + " " + _date + ", " + _year;

        return _timeline;
    }

    public static String getUniInitialsAndYear(Task task)
    {
        String[] _university = new String[1];
        String _univInitials = getUniversityInitials(task, _university);
        String _univYear = getUserUniversityYear(task);
        String _univCourse = getUserUniversityCourse(task);
        String _univInitialsAndYear = _univInitials + "-" + _univYear;

        return _univInitialsAndYear;
    }

    public static String getUniAndYearAndCourseInitials(Task task)
    {
        String _uniIniAndYearInitials = getUniInitialsAndYear(task);
        String _universityCourseName = getUserUniversityCourse(task);

        StringBuilder initials = new StringBuilder();
        for(String s : _universityCourseName.split(" "))
        {
            initials.append(s.charAt(0));
        }
        String _courseInitials = initials.toString();//Here we get university initials e.g Computer Science = CS
        //Concate the university and year initials with course initials
        String _uniAndYearAndCourseInitials = _uniIniAndYearInitials + "-" + _courseInitials;

        return _uniAndYearAndCourseInitials;
    }
    //Firebase methods
}
