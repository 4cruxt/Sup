package com.fole_Studios.sup.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fole_Studios.sup.R;
import com.fole_Studios.sup.adapters.AnnouncementAdapter;
import com.fole_Studios.sup.models.Announcement;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

import static com.fole_Studios.sup.custom.TimeAgo.getTimeAgo;
import static com.fole_Studios.sup.database.DBqueries.displayNotification;
import static com.fole_Studios.sup.database.DBqueries.enableFloatingButton;
import static com.fole_Studios.sup.database.DBqueries.getUniAndYearAndCourseInitials;

/**
 * A simple {@link Fragment} subclass.
 */
public class AssignmentFragment extends Fragment
{
    public static final int ASSIGN_FRAGMENT_ID = 3;
    private RecyclerView _recyclerview;
    private ArrayList<Announcement> _assignments = new ArrayList<>();
    private AnnouncementAdapter _adapter;
    private ProgressBar _progressBar;

    public AssignmentFragment()
    {
        //Required empty constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View _view = inflater.inflate(R.layout.fragment_assignment, container, false);

        _recyclerview = _view.findViewById(R.id.assign_main_recyclerview);
        _progressBar = _view.findViewById(R.id.assign_main_progress_bar);

        enableFloatingButton(ASSIGN_FRAGMENT_ID);
        initRecyclerview();
        return _view;
    }

    private void initRecyclerview()
    {
        _adapter = new AnnouncementAdapter(_assignments, getContext());
        assignmentData();
        _recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        _recyclerview.setAdapter(_adapter);
        _adapter.notifyDataSetChanged();

    }

    //todo: Move this method to DBqueries.class level
    private void assignmentData()
    {
        final String _userId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        final FirebaseFirestore _firestore = FirebaseFirestore.getInstance();

        _firestore.collection("USERS").document(_userId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>()
        {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task)
            {
                if(task.isSuccessful())
                {
                    CollectionReference _collectionReference = _firestore.collection(getUniAndYearAndCourseInitials(task));
                    _collectionReference.orderBy("assignment_id").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
                    {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task)
                        {
                            if(task.isSuccessful())
                            {
                                _progressBar.setVisibility(View.INVISIBLE);

                                for(DocumentSnapshot _documentSnapshot : Objects.requireNonNull(task.getResult()))
                                {
                                    Date _createdDate = _documentSnapshot.getDate("created_at");
                                    long _mills = Objects.requireNonNull(_createdDate).getTime();
                                    String date = getTimeAgo(_mills);

                                    _assignments.add(new Announcement(Objects.requireNonNull(_documentSnapshot.get("assignment_type")).toString(), Objects.requireNonNull(_documentSnapshot.get("assignment")).toString(), date));
                                }
                                _adapter.notifyDataSetChanged();

                                _firestore.collection("USERS").document(_userId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>()
                                {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task)
                                    {
                                        displayNotification(getContext(), task, _assignments, "assignments", "Assignment", "New Assignment!");
                                    }
                                });
                            }
                        }
                    });
                }
            }
        });
    }
}