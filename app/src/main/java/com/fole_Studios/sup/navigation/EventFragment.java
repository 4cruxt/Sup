package com.fole_Studios.sup.navigation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fole_Studios.sup.R;
import com.fole_Studios.sup.adapters.EventAdapter;
import com.fole_Studios.sup.models.EventFeatured;

import java.util.ArrayList;

import static com.fole_Studios.sup.database.DBqueries.enableFloatingButton;
import static com.fole_Studios.sup.database.DBqueries.getUniversityEvents;


/**
 * A simple {@link Fragment} subclass.
 */
public class EventFragment extends Fragment
{

    private static final int EVENT_FRAGMENT_ID = 4;
    private RecyclerView _recyclerView;
    private ProgressBar _progressBar;
    private EventAdapter _adapter;
    private ArrayList<EventFeatured> _eventFeatured = new ArrayList<>();

    public EventFragment()
    {
        //Required empty constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View _view = inflater.inflate(R.layout.fragment_event, container, false);

        _recyclerView = _view.findViewById(R.id.e_m_recyclerview);
        _progressBar = _view.findViewById(R.id.e_m_progress_bar);

        enableFloatingButton(EVENT_FRAGMENT_ID);
        initRecyclerview();

        return _view;
    }

    private void initRecyclerview()
    {
        _adapter = new EventAdapter(getContext(), _eventFeatured);
        getUniversityEvents(getContext(), _adapter, _eventFeatured, _progressBar);
        _recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        _recyclerView.setAdapter(_adapter);
    }

}