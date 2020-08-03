package com.fole_Studios.sup.navigation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fole_Studios.sup.R;
import com.fole_Studios.sup.adapters.EventAdapter;
import com.fole_Studios.sup.models.EventFeatured;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class EventFragment extends Fragment
{

    private ArrayList<EventFeatured> _events;
    private EventAdapter _adapter;
    private RecyclerView _recyclerView;

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

        initRecyclerview();

        return _view;
    }

    private void initRecyclerview()
    {
        dataList();
        _adapter = new EventAdapter(_events);
        _recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        _recyclerView.setAdapter(_adapter);
        _adapter.notifyDataSetChanged();
    }

    private void dataList()
    {
        _events = new ArrayList<>();

        _events.add(new EventFeatured(R.drawable.event_a, "Animation Pre Launch", "01", "SEPT"));
        _events.add(new EventFeatured(R.drawable.event_b, "Release of Tanzanian Animation", "12", "DEC"));
        _events.add(new EventFeatured(R.drawable.event_c, "Official launch of animation studio", "27", "FEB"));

    }

}