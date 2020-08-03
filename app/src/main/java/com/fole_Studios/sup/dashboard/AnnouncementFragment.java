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
public class AnnouncementFragment extends Fragment
{

    private RecyclerView _recyclerview;
    private ArrayList<Announcement> _announcements;

    public AnnouncementFragment()
    {
        //Required empty constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View _view = inflater.inflate(R.layout.fragment_announcement, container, false);

        _recyclerview = _view.findViewById(R.id.ann_main_recyclerview);

        initRecyclerview();
        return _view;
    }

    private void initRecyclerview()
    {
        dataList();
        AnnouncementAdapter _adapter = new AnnouncementAdapter(_announcements, getContext());
        _recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        _recyclerview.setAdapter(_adapter);
        _adapter.notifyDataSetChanged();

    }

    private void dataList()
    {
        _announcements = new ArrayList<>();
        _announcements.add(new Announcement("EXAMS", "Exams will start on 17th August, 2020. First exam is CST04201 - Web Tech. Basics", "2d ago"));
        _announcements.add(new Announcement("CLASSES", "Classes are over - two weeks for studying before exams", "10:45 am"));
        _announcements.add(new Announcement("GENERAL", "Students with less than 65% of attendance will be reprimanded by the end of this semister", "6d ago"));

    }
}