package com.fole_Studios.sup.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fole_Studios.sup.R;
import com.fole_Studios.sup.models.EventFeatured;

import java.util.ArrayList;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder>
{
    private ArrayList<EventFeatured> _eventFeatured;

    public EventAdapter(ArrayList<EventFeatured> eventFeatured)
    {
        _eventFeatured = eventFeatured;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        LayoutInflater _layoutInflater = LayoutInflater.from(parent.getContext());
        View _eventListItem = _layoutInflater.inflate(R.layout.event_item, parent, false);

        return new ViewHolder(_eventListItem);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        holder._eventPhoto.setImageResource(_eventFeatured.get(position).getEventPhoto());
        holder._eventDate.setText(_eventFeatured.get(position).getEventDate());
        holder._eventMonth.setText(_eventFeatured.get(position).getEventMonth());
        holder._eventTitle.setText(_eventFeatured.get(position).getEventTitle());
    }

    @Override
    public int getItemCount()
    {
        return _eventFeatured.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        private ImageView _eventPhoto;
        private TextView _eventDate, _eventMonth, _eventTitle;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            _eventPhoto = itemView.findViewById(R.id.e_item_photo);
            _eventDate = itemView.findViewById(R.id.e_item_date);
            _eventMonth = itemView.findViewById(R.id.e_item_month);
            _eventTitle = itemView.findViewById(R.id.e_item_title);
        }
    }
}
