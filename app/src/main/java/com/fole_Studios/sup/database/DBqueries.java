package com.fole_Studios.sup.database;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.fole_Studios.sup.MainActivity;
import com.fole_Studios.sup.R;
import com.fole_Studios.sup.adapters.EventAdapter;
import com.fole_Studios.sup.adapters.TimelineAdapter;
import com.fole_Studios.sup.models.Announcement;
import com.fole_Studios.sup.models.EventFeatured;
import com.fole_Studios.sup.models.Timeline;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.CollectionReference;
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

    //@method store user data into the firebase firestore.
    public static void setUserDataToDatabase(String username)
    {
        updateUserName(username);
        FirebaseFirestore _firestore = FirebaseFirestore.getInstance();

        Map<String, Object> _user = new HashMap<>();
        _user.put("username", _currentUser.getDisplayName());
        _user.put("user_reg_number", "");
        _user.put("user_bio", "");
        _user.put("user_course", "Computer Science");
        _user.put("university", "Mbeya University of Science and Technology - MUST");
        _user.put("region", "Mbeya");
        _user.put("university_year", "1");
        _user.put("is_verified", false);

        _firestore.collection("USERS").document(_currentUser.getUid()).set(_user);
    }

    //@method update authenticated username
    public static void updateUserName(String username)
    {
        //adding user name to this authenticated user.
        UserProfileChangeRequest _userUpdates = new UserProfileChangeRequest.Builder().setDisplayName(username).build();
        Objects.requireNonNull(_currentUser).updateProfile(_userUpdates);
    }

    //@method update user data in the firestore database
    public static void updateUserDataInDatabase(TextView username, TextView regNumber, TextView courseName, TextView userBio, final Context context)
    {

        Map<String, Object> _userUpdate = new HashMap<>();
        _userUpdate.put("user_reg_number", regNumber.getText().toString());
        _userUpdate.put("username", username.getText().toString());
        _userUpdate.put("user_course", courseName.getText().toString());//todo: this should be updated in the near future in order a user to enter course data.
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

    //@method retrieve user data from the firebase database
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
                    if(editProButton != null)
                    {
                        editProButton.setVisibility(View.INVISIBLE);
                    }

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

    //@method retrieve university events from the firebase database
    public static void getUniversityEvents(final Context context, final EventAdapter _adapter, final ArrayList<EventFeatured> _eventFeatured, final ProgressBar progressBar)
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

                    String _universityInitials = getUniversityInitials(task, _university);

                    if(_universityInitials != null)
                    {
                        //Find the correspond events within the given university.
                        _firestore.collection(_universityInitials).orderBy("date").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
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
                                    //todo: next update make sure you limit query size to 3 per user. You should not get all data from the database without pagination.
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
                        FancyToast.makeText(context, "Soon your university will be included!", FancyToast.LENGTH_SHORT, FancyToast.INFO, false);
                    }


                }
                else
                {
                    Log.e("UNIVERSITY FAILURE", Objects.requireNonNull(Objects.requireNonNull(task.getException()).getMessage()));
                }
            }
        });
    }

    //@method retrieve module exam timeline from thee firebase database
    public static void getModuleExamTimeline(final Context context, final ShimmerFrameLayout _placeholder, final RecyclerView _recyclerView, final ArrayList<Timeline> _timelines, final TimelineAdapter _adapter)
    {
        String _userId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        final FirebaseFirestore _firestore = FirebaseFirestore.getInstance();

        _firestore.collection("USERS").document(_userId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>()
        {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task)
            {
                if(task.isSuccessful())
                {
//                    _limitScroll = 3;
                    if(_firestore.collection(getUniAndYearAndCourseInitials(task)) != null)
                    {
                        final CollectionReference _moduleCollectionRef = _firestore.collection(getUniAndYearAndCourseInitials(task));
                        _moduleCollectionRef.orderBy("exam_day").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
                        {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task)
                            {
                                if(task.isSuccessful())
                                {
                                    _placeholder.setVisibility(View.INVISIBLE);
                                    _recyclerView.setVisibility(View.VISIBLE);
                                    for(DocumentSnapshot _queryDocumentSnapshot : Objects.requireNonNull(task.getResult()))
                                    {
                                        _timelines.add(new Timeline(Objects.requireNonNull(_queryDocumentSnapshot.get("exam_day") + " - " + _queryDocumentSnapshot.get("exam_time")).toString(), Objects.requireNonNull(_queryDocumentSnapshot.get("venue")).toString(), Objects.requireNonNull(_queryDocumentSnapshot.get("code")).toString() + " - " + Objects.requireNonNull(_queryDocumentSnapshot.get("name")).toString(), Objects.requireNonNull(_queryDocumentSnapshot.get("status")).toString()));
                                    }
                                    _adapter.notifyDataSetChanged();

//                                _lastVisible = task.getResult().getDocuments().get(task.getResult().size() - 1);

//                                //Performing page rescolling in order to update the data.
//                                RecyclerView.OnScrollListener _onScrollListener = new RecyclerView.OnScrollListener()
//                                {
//                                    @Override
//                                    public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState)
//                                    {
//                                        super.onScrollStateChanged(recyclerView, newState);
//                                        if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL)
//                                        {
//                                            _isScolling = true;
//                                        }
//                                    }
//
//                                    @Override
//                                    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy)
//                                    {
//                                        super.onScrolled(recyclerView, dx, dy);
//
//                                        LinearLayoutManager _linearLayoutManager = ((LinearLayoutManager) recyclerView.getLayoutManager());
//                                        int _firstVisibleItemPosition = Objects.requireNonNull(_linearLayoutManager).findFirstVisibleItemPosition();
//                                        int _visibleItemCount = _linearLayoutManager.getChildCount();
//                                        int _totalItemCount = _linearLayoutManager.getItemCount();
//
//                                        if(_isScolling && (_firstVisibleItemPosition + _visibleItemCount == _totalItemCount) &&  !_isLastItemReached)
//                                        {
//                                            _isScolling = false;
//
//                                            Query _newQuery = _moduleCollectionRef.orderBy("exam_day").startAfter(_lastVisible).limit(_limitScroll);
//                                            _newQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
//                                            {
//                                                @Override
//                                                public void onComplete(@NonNull Task<QuerySnapshot> task)
//                                                {
//                                                    if(task.isSuccessful())
//                                                    {
//                                                        for(DocumentSnapshot _documentSnapshot : Objects.requireNonNull(task.getResult()))
//                                                        {
//                                                            _timelines.add(new Timeline(Objects.requireNonNull(_documentSnapshot.get("exam_time")).toString(), Objects.requireNonNull(_documentSnapshot.get("venue")).toString(), Objects.requireNonNull(_documentSnapshot.get("code")).toString() + " - " + Objects.requireNonNull(_documentSnapshot.get("name")).toString(), Objects.requireNonNull(_documentSnapshot.get("status")).toString()));
//                                                        }
//                                                        _adapter.notifyDataSetChanged();
//                                                        _lastVisible = task.getResult().getDocuments().get(task.getResult().size() - 1);
//
//                                                        if(task.getResult().size() < _limitScroll)
//                                                        {
//                                                            _isLastItemReached = true;
//                                                        }
//                                                    }
//                                                }
//                                            });
//                                        }
//                                    }
//                                };
//
//                                //Here the recyclerview listenes if it has been touched in order to update the information.
//                                _recyclerView.addOnScrollListener(_onScrollListener);
                                }
                                else
                                {
                                    _recyclerView.setVisibility(View.INVISIBLE);
                                    _placeholder.setVisibility(View.VISIBLE);
                                    Log.i("FIREBASE ERROR", Objects.requireNonNull(Objects.requireNonNull(task.getException()).getMessage()));
                                }
                            }
                        });
                    }
                    else
                    {
                        FancyToast.makeText(context, "Check your CR to register your course!", FancyToast.LENGTH_SHORT, FancyToast.INFO, false);
                    }

                }
                else
                {
                    Log.i("FIREBASE ERROR", Objects.requireNonNull(Objects.requireNonNull(task.getException()).getMessage()));
                }

            }
        });
    }

    //@method retrieve user course information
    public static void getuserCourseInformation(final Context context, final TextView exam, final TextView project, final TextView module)
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

                    if(_univInitialsAndYear != null)
                    {
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
                        //todo: Implement fancy toast for user to get information information
                        FancyToast.makeText(context, "Soon your university will be included!", FancyToast.LENGTH_SHORT, FancyToast.INFO, false);
                    }

                }
                else
                {
                    Log.i("DASHBOARD FIREBASE", Objects.requireNonNull(Objects.requireNonNull(task.getException()).getMessage()));
                }
            }
        });
    }

    //@method logout user from the system
    public static void logoutUser()
    {
        _auth.signOut();
    }

    //@method display announcement/assignment notification
    public static void displayNotification(final Context context, final Task task, final ArrayList<Announcement> _announcements, final String _fieldName, final String _title, final String _textBody)
    {
        final String _univCourse = getUserUniversityCourse(task);
        final String _univInitialsAndYear = getUniInitialsAndYear(task);

        _firestore.collection(_univInitialsAndYear).document(_univCourse).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>()
        {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task)
            {
                if(task.isSuccessful())
                {
                    DocumentSnapshot _documentSnap = task.getResult();

                    long _annNumber = (long) _documentSnap.get(_fieldName);

                    if(_announcements.size() > _annNumber)
                    {

                        _annNumber = _announcements.size();

                        enableNotification(context, _title, _textBody);

                        _firestore.collection(_univInitialsAndYear).document(_univCourse).update(_fieldName, _annNumber);

                    }
                }
                else
                {
                    Log.i("DASH INFO QUERY", Objects.requireNonNull(Objects.requireNonNull(task.getException()).getMessage()));
                }
            }
        });
    }

    //@method retrieve university initials from the firebase database
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

    //@method return user university year from the database
    public static String getUserUniversityYear(Task task)
    {
        DocumentSnapshot _documentSnapshot = (DocumentSnapshot) task.getResult();
        String _universityYear = _documentSnapshot.get("university_year").toString();

        return _universityYear;
    }

    //@method return user university course
    public static String getUserUniversityCourse(Task task)
    {
        DocumentSnapshot _documentSnapshot = (DocumentSnapshot) task.getResult();
        String _universityCourseData = _documentSnapshot.get("user_course").toString();
//        int _universityCourseIndex = getCapsNameCourse(_universityCourseData).lastIndexOf(" ");
//        String _universityCourse = getCapsNameCourse(_universityCourseData).substring(0, _universityCourseIndex);

        return _universityCourseData;
    }

    //@method return university course into capital letters
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

    //@method return current time
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

    //@method return university and year embedded initials
    public static String getUniInitialsAndYear(Task task)
    {
        String[] _university = new String[1];
        String _univInitials = getUniversityInitials(task, _university);
        String _univYear = getUserUniversityYear(task);
        String _univCourse = getUserUniversityCourse(task);
        String _univInitialsAndYear = _univInitials + "-" + _univYear;

        return _univInitialsAndYear;
    }

    //@method return university, year and course embedded initials
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

    public static void enableNotification(Context context, String title, String text)
    {
        int _notificationId = 0;
        //initilieze and declare notification specifications
        NotificationCompat.Builder _builder = new NotificationCompat.Builder(context).setSmallIcon(R.drawable.noti_icon).setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.not_icon)).setContentTitle(title).setContentText(text).setAutoCancel(true).setDefaults(NotificationCompat.DEFAULT_ALL);

        //the actions in a notification are handled throuh intents
        Intent intent = new Intent(context, context.getClass());
        PendingIntent _pendingIntent = PendingIntent.getActivity(context, 0, intent, Intent.FLAG_ACTIVITY_NEW_TASK);
        _builder.setContentIntent(_pendingIntent);
        //set a tone when notification appears
        Uri _path = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        _builder.setSound(_path);

        //call notification manager so it can build and deliver the notificationn to the OS
        NotificationManager _notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        //Android 8 introduced a new requirements of setting the channelID property by using NotificationChannel.

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            String channelId = "YOUR_CHANNEL_ID";
            NotificationChannel _channel = new NotificationChannel(channelId, "Channel human readable title", NotificationManager.IMPORTANCE_DEFAULT);
            _notificationManager.createNotificationChannel(_channel);
            _builder.setChannelId(channelId);
        }

        _notificationManager.notify(_notificationId, _builder.build());
    }

    public static void enableFloatingButton(int fragmentId)
    {
        MainActivity.animateFab(fragmentId);
    }

    //Firebase methods
}
