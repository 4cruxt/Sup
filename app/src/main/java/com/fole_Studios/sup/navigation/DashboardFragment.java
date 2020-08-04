package com.fole_Studios.sup.navigation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.fole_Studios.sup.R;
import com.fole_Studios.sup.adapters.TimelineAdapter;
import com.fole_Studios.sup.dashboard.AnnouncementFragment;
import com.fole_Studios.sup.dashboard.AssignmentFragment;
import com.fole_Studios.sup.models.Timeline;

import java.util.ArrayList;
import java.util.Objects;

import static com.fole_Studios.sup.database.DBqueries.getCurrentTime;
import static com.fole_Studios.sup.database.DBqueries.getModuleTimeline;
import static com.fole_Studios.sup.database.DBqueries.getuserCourseInformation;

/**
 * A simple {@link Fragment} subclass.
 */
public class DashboardFragment extends Fragment
{

    private ArrayList<Timeline> _timelines = new ArrayList<>();
    private RecyclerView _recyclerView;
    private TextView _exam, _project, _module, _timelineTime;
    private TimelineAdapter _adapter;
    private ShimmerFrameLayout _placeholder;
//    private boolean _isScolling = false;
//    private boolean _isLastItemReached = false;
//    private DocumentSnapshot _lastVisible;
//    private int _limitScroll;

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
        _placeholder = _view.findViewById(R.id.timeline_placeholder);

        CardView _annCard = _view.findViewById(R.id.d_m_ann_card);
        CardView _assignCard = _view.findViewById(R.id.d_m_assign_card);

        initRecyclerview();

        enableFragment(_annCard, _assignCard);
        displayCourseInfo();
        currentTime();


        return _view;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        _placeholder.startShimmer();
    }

    @Override
    public void onPause()
    {
        super.onPause();
        _placeholder.stopShimmer();
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
        _adapter = new TimelineAdapter(getContext(), _timelines);
        moduleTimeline();
        _recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        _recyclerView.setAdapter(_adapter);

    }

    private void displayCourseInfo()
    {
        getuserCourseInformation(_exam, _project, _module);

    }

    private void currentTime()
    {
        _timelineTime.setText(getCurrentTime());
    }

    //todo: In the next updates this database query method should be taken into the respective class i.e DBqueries.class
    private void moduleTimeline()
    {
        getModuleTimeline(_placeholder, _recyclerView, _timelines, _adapter);
    }

}