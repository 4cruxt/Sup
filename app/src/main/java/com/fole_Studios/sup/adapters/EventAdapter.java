package com.fole_Studios.sup.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.fole_Studios.sup.R;
import com.fole_Studios.sup.custom.GlideApp;
import com.fole_Studios.sup.models.EventFeatured;

import java.util.ArrayList;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder>
{
    private ArrayList<EventFeatured> _eventFeatured;
    private Context _context;

    public EventAdapter(Context context, ArrayList<EventFeatured> eventFeatured)
    {
        _context = context;
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
        holder.setImageUrl(_context, _eventFeatured.get(position).getEventPhoto());
        holder._eventDate.setText(_eventFeatured.get(position).getEventDate());
        holder._eventMonth.setText(_eventFeatured.get(position).getEventMonth());
        holder._eventTitle.setText(_eventFeatured.get(position).getEventTitle());
    }

//    @Override
//    public long getItemId(int position)
//    {
//        return position;
//    }

    @Override
    public int getItemCount()
    {
        return _eventFeatured.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        private ImageView _eventPhoto;
        private TextView _eventDate, _eventMonth, _eventTitle;
        private ConstraintLayout _eventContainer;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            _eventPhoto = itemView.findViewById(R.id.e_item_photo);
            _eventDate = itemView.findViewById(R.id.e_item_date);
            _eventMonth = itemView.findViewById(R.id.e_item_month);
            _eventTitle = itemView.findViewById(R.id.e_item_title);
            _eventContainer = itemView.findViewById(R.id.event_item_container);
        }

        private void setImageUrl(Context context, String imageUrl)
        {
            //todo: update placeholder
            GlideApp.with(context).load(imageUrl).placeholder(R.drawable.unfocused_button).into(_eventPhoto);
        }

    }
}
