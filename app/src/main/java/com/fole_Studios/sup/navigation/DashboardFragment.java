package com.fole_Studios.sup.navigation;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fole_Studios.sup.R;
import com.fole_Studios.sup.adapters.TimelineAdapter;
import com.fole_Studios.sup.dashboard.AnnouncementFragment;
import com.fole_Studios.sup.dashboard.AssignmentFragment;
import com.fole_Studios.sup.models.Timeline;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Objects;

import static com.fole_Studios.sup.database.DBqueries.getCurrentTime;
import static com.fole_Studios.sup.database.DBqueries.getUniAndYearAndCourseInitials;
import static com.fole_Studios.sup.database.DBqueries.getuserCourseInformation;

/**
 * A simple {@link Fragment} subclass.
 */
public class DashboardFragment extends Fragment
{

    private ArrayList<Timeline> _timelines;
    private RecyclerView _recyclerView;
    private TextView _exam, _project, _module, _timelineTime;
    private TimelineAdapter _adapter;

    public DashboardFragment()
    {
        //Required constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View _view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        _recyclerView = _view.findViewById(R.id.d_m_recyclerview);
        _exam = _view.findViewById(R.id.d_m_h_exam);
        _module = _view.findViewById(R.id.d_m_h_module);
        _project = _view.findViewById(R.id.d_m_h_project);
        _timelineTime = _view.findViewById(R.id.d_m_timeline_date);

        CardView _annCard = _view.findViewById(R.id.d_m_ann_card);
        CardView _assignCard = _view.findViewById(R.id.d_m_assign_card);

        initRecyclerview();

        enableFragment(_annCard, _assignCard);
        displayCourseInfo();
        currentTime();


        return _view;
    }

    private void enableFragment(CardView annCard, CardView assignCard)
    {
        annCard.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                AnnouncementFragment _announcementFragment = new AnnouncementFragment();
                openFragment(_announcementFragment);

            }
        });


        assignCard.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                AssignmentFragment _assignmentFragment = new AssignmentFragment();
                openFragment(_assignmentFragment);
            }
        });
    }

    private void openFragment(Fragment fragment)
    {
        FragmentTransaction _transaction = Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction();
        _transaction.replace(R.id.main_fragment_container, fragment);
        _transaction.commit();
    }

    private void initRecyclerview()
    {
        dataList();
        _adapter = new TimelineAdapter(getContext(), _timelines);
        _recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        _recyclerView.setAdapter(_adapter);

    }

    private void displayCourseInfo()
    {
        getuserCourseInformation(_exam, _project, _module);

    }

    private void dataList()
    {
        _timelines = new ArrayList<>();
        moduleTimeline();

    }

    private void currentTime()
    {
        _timelineTime.setText(getCurrentTime());
    }

    private void moduleTimeline()
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
                    _firestore.collection(getUniAndYearAndCourseInitials(task)).orderBy("exam_day").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
                    {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task)
                        {
                            if(task.isSuccessful())
                            {
                                for(QueryDocumentSnapshot _queryDocumentSnapshot : Objects.requireNonNull(task.getResult()))
                                {
                                    _timelines.add(new Timeline(Objects.requireNonNull(_queryDocumentSnapshot.get("exam_time")).toString(), Objects.requireNonNull(_queryDocumentSnapshot.get("venue")).toString(), Objects.requireNonNull(_queryDocumentSnapshot.get("code")).toString() + " - " + Objects.requireNonNull(_queryDocumentSnapshot.get("name")).toString(), Objects.requireNonNull(_queryDocumentSnapshot.get("status")).toString()));
                                }
                                _adapter.notifyDataSetChanged();
                            }
                            else
                            {
                                Log.i("FIREBASE ERROR", Objects.requireNonNull(Objects.requireNonNull(task.getException()).getMessage()));
                            }
                        }
                    });
                }
                else
                {
                    Log.i("FIREBASE ERROR", Objects.requireNonNull(Objects.requireNonNull(task.getException()).getMessage()));
                }

            }
        });

    }
}