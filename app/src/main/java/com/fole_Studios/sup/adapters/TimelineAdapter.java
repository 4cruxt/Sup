package com.fole_Studios.sup.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

import static com.fole_Studios.sup.custom.TimeAgo.getRemainingTime;

//@SuppressWarnings("ALL")
public class TimelineAdapter extends RecyclerView.Adapter<TimelineAdapter.ViewHolder>
{
    private static final String TAG = "TimelineAdapter";
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
        holder._venueName.setText(_timelines.get(position).getVenueName());
        holder._activeLecture.setText(_timelines.get(position).getActiveLecture());

        statusChecker(holder, position, _timelines.get(position).getActiveLecture());
        timeNotifier(holder, position);

    }

    @SuppressLint("SetTextI18n")
    private void timeNotifier(ViewHolder holder, int position)
    {
        String activeTimeline = _timelines.get(position).getStartTime();

        @SuppressLint("SimpleDateFormat") DateFormat _dateFormat = new SimpleDateFormat("dd/MM/yyyy - hh:mm a");
        try
        {
            Date _createdDate = _dateFormat.parse(activeTimeline);
            long _mills = Objects.requireNonNull(_createdDate).getTime();
            String date = getRemainingTime(_mills);

            if(date.isEmpty())
            {
                holder._startTime.setText(_timelines.get(position).getStartTime());
            }
            else
            {
                holder._startTime.setText(date);
            }

            if(date.equals("NOW"))
            {
                holder._activeLecture.setText("ONGOING");
                statusChecker(holder, position, "ONGOING");
            }
        }
        catch(Exception e)
        {
            Log.d(TAG, "timeNotifier: " + e.getMessage());
        }

    }

    public void statusChecker(ViewHolder holder, int position, String status)
    {

        if(status.contains("ONGOING"))
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
