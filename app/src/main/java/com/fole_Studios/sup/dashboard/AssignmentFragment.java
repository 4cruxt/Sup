package com.fole_Studios.sup.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fole_Studios.sup.R;
import com.fole_Studios.sup.adapters.AnnouncementAdapter;
import com.fole_Studios.sup.models.Announcement;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class AssignmentFragment extends Fragment
{

    private RecyclerView _recyclerview;
    private ArrayList<Announcement> _assignments;

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

        initRecyclerview();
        return _view;
    }

    private void initRecyclerview()
    {
        dataList();
        AnnouncementAdapter _adapter = new AnnouncementAdapter(_assignments, getContext());
        _recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        _recyclerview.setAdapter(_adapter);
        _adapter.notifyDataSetChanged();

    }

    private void dataList()
    {
        _assignments = new ArrayList<>();
        _assignments.add(new Announcement("INDIVIDUAL", "Exams will start on 17th August, 2020. First exam is CST04201 - Web Tech. Basics", "2d ago"));
        _assignments.add(new Announcement("GROUP", "Classes are over - two weeks for studying before exams", "10:45 am"));

    }
}