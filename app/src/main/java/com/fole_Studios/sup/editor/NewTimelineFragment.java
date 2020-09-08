package com.fole_Studios.sup.editor;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fole_Studios.sup.R;
import com.fole_Studios.sup.adapters.NewTimelineAdapter;
import com.fole_Studios.sup.models.NewTimeline;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewTimelineFragment extends Fragment
{
    private String _day;
    private RecyclerView _recyclerView;
    private ArrayList<NewTimeline> _newTimeline;
    private NewTimelineAdapter _adapter;

    public NewTimelineFragment()
    {
        // Required empty public constructor
    }

    public NewTimelineFragment(String day)
    {
        _day = day;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View _view = inflater.inflate(R.layout.fragment_new_timeline, container, false);

        _recyclerView = _view.findViewById(R.id.timeline_new_recyclerview);

        initRecyclerview();

        return _view;
    }

    private void initRecyclerview()
    {
        dataList();
        _adapter = new NewTimelineAdapter(_newTimeline, _day);
        _recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        _recyclerView.setAdapter(_adapter);
        _adapter.notifyDataSetChanged();
    }

    private void dataList()
    {
        _newTimeline = new ArrayList<>();
        _newTimeline.add(new NewTimeline("", null, "", "", ""));
    }

}