package com.fole_Studios.sup.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.fole_Studios.sup.R;
import com.fole_Studios.sup.models.Timeline;

import java.util.ArrayList;

//@SuppressWarnings("ALL")
public class TimelineAdapter extends RecyclerView.Adapter<TimelineAdapter.ViewHolder>
{
    private ArrayList<Timeline> _timelines;
    private Context _context;

    public TimelineAdapter(Context context, ArrayList<Timeline> timelines)
    {
        _timelines = timelines;
        _context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View _view = LayoutInflater.from(parent.getContext()).inflate(R.layout.timeline_item, parent, false);

        return new ViewHolder(_view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        holder._courseName.setText(_timelines.get(position).getCourseName());
        holder._startTime.setText(_timelines.get(position).getStartTime());
        holder._venueName.setText(_timelines.get(position).getVenueName());
        holder._activeLecture.setText(_timelines.get(position).getActiveLecture());

        String activeLecture = _timelines.get(position).getActiveLecture();

        if(activeLecture.equals("ONGOING LECTURE"))
        {
            holder._activeIcon.setColorFilter(ContextCompat.getColor(_context, R.color.colorLightGreen));
        }
        else
        {
            holder._activeIcon.setColorFilter(ContextCompat.getColor(_context, R.color.colorBackgroundWhite));
        }

    }

    @Override
    public int getItemCount()
    {
        return _timelines.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        private TextView _startTime, _venueName, _courseName, _activeLecture;
        private ImageView _activeIcon;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            _courseName = itemView.findViewById(R.id.t_item_course_name);
            _startTime = itemView.findViewById(R.id.t_item_time);
            _venueName = itemView.findViewById(R.id.t_item_venue);
            _activeLecture = itemView.findViewById(R.id.t_item_active_text);
            _activeIcon = itemView.findViewById(R.id.t_item_active_icon);
        }
    }
}
