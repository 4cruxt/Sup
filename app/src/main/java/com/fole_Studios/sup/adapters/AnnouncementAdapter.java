package com.fole_Studios.sup.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.fole_Studios.sup.R;
import com.fole_Studios.sup.models.Announcement;

import java.util.ArrayList;

public class AnnouncementAdapter extends RecyclerView.Adapter<AnnouncementAdapter.ViewHolder>
{
    private ArrayList<Announcement> _announcements;
    private Context _context;

    public AnnouncementAdapter(ArrayList<Announcement> announcements, Context context)
    {
        _announcements = announcements;
        _context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View _view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ann_item, parent, false);

        return new ViewHolder(_view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        holder._annType.setText(_announcements.get(position).getAnnType());
        holder._annText.setText(_announcements.get(position).getAnnText());
        holder._annTime.setText(_announcements.get(position).getAnnTime());

        String _typeAnn = _announcements.get(position).getAnnType();

        if(_typeAnn.equals("EXAMS"))
        {
            holder._annTypeHolder.setCardBackgroundColor(ContextCompat.getColor(_context, R.color.colorExamType));
        }
        else if(_typeAnn.equals("CLASSES"))
        {
            holder._annTypeHolder.setCardBackgroundColor(ContextCompat.getColor(_context, R.color.colorClassType));
        }
        else if(_typeAnn.equals("GENERAL"))
        {
            holder._annTypeHolder.setCardBackgroundColor(ContextCompat.getColor(_context, R.color.colorGeneralType));
        }
        else if(_typeAnn.equals("INDIVIDUAL"))
        {
            holder._annTypeHolder.setCardBackgroundColor(ContextCompat.getColor(_context, R.color.colorGreen));
        }
        else if(_typeAnn.equals("GROUP"))
        {
            holder._annTypeHolder.setCardBackgroundColor(ContextCompat.getColor(_context, R.color.colorGeneralType));
        }

    }

    @Override
    public int getItemCount()
    {
        return _announcements.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        private TextView _annType, _annText, _annTime;
        private CardView _annTypeHolder;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);

            _annType = itemView.findViewById(R.id.ann_main_type_text);
            _annTypeHolder = itemView.findViewById(R.id.ann_main_type_card);
            _annText = itemView.findViewById(R.id.ann_main_text);
            _annTime = itemView.findViewById(R.id.ann_main_time);
        }
    }
}
