package com.fole_Studios.sup.navigation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

import java.util.ArrayList;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class DashboardFragment extends Fragment
{

    private ArrayList<Timeline> _timelines;
    private RecyclerView _recyclerView;

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
        CardView _annCard = _view.findViewById(R.id.d_m_ann_card);
        CardView _assignCard = _view.findViewById(R.id.d_m_assign_card);

        initRecyclerview();

        enableFragment(_annCard, _assignCard);


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
        TimelineAdapter _adapter = new TimelineAdapter(getContext(), _timelines);
        _recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        _recyclerView.setAdapter(_adapter);
        _adapter.notifyDataSetChanged();

    }


    private void dataList()
    {
        _timelines = new ArrayList<>();

        _timelines.add(new Timeline("13:25", "LAB 02", "CST04204 - Mobile Application Basics", "ONGOING LECTURE"));
        _timelines.add(new Timeline("7:30", "LAB 03", "CST04203 - Comp. Networking Basics", "UPCOMING LECTURE"));
        _timelines.add(new Timeline("7:30", "LAB 01", "CST04205 - Multimedia Technologies", "UPCOMING LECTURE"));

    }
}