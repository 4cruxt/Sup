package com.fole_Studios.sup.adapters;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.fole_Studios.sup.R;
import com.fole_Studios.sup.models.NewTimeline;

import java.util.ArrayList;

public class NewTimelineAdapter extends RecyclerView.Adapter<NewTimelineAdapter.ViewHolder>
{
    public static ArrayList<NewTimeline> _newTimeline = new ArrayList<>();
    private String _day;
    private String _typeTimeline;

    public NewTimelineAdapter(ArrayList<NewTimeline> newTimeline, String day)
    {
        _newTimeline = newTimeline;
        _day = day;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View _view = LayoutInflater.from(parent.getContext()).inflate(R.layout.new_timeline_item, parent, false);

        return new ViewHolder(_view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position)
    {
        holder._time.setText(_newTimeline.get(position).getTime());

        if(_newTimeline.get(position).getDate() != null)
        {
            holder._date.setText(_newTimeline.get(position).getDate());
            _typeTimeline = "exam".toUpperCase();
        }
        else
        {
            holder._date.setVisibility(View.INVISIBLE);
            _typeTimeline = "lecture".toUpperCase();
        }

        holder._venue.setText(_newTimeline.get(position).getVenue());
        holder._moduleCode.setText(_newTimeline.get(position).getModuleCode());
        holder._moduleName.setText(_newTimeline.get(position).getModuleName());

        holder.setEditViewData();

        holder._saveButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //When the save button is clicked all the data should be stored into the firestore database and displayed
                //on the dashboard timeline
                //_day variable to extract day
                //all data should be submitted to the database.

                Toast.makeText(v.getContext(), "" + _typeTimeline, Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount()
    {
        return _newTimeline.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        public EditText _time;
        public EditText _date;
        public EditText _venue;
        public EditText _moduleCode;
        public EditText _moduleName;
        public CardView _saveButton;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);

            _time = itemView.findViewById(R.id.new_timeline_time);
            _date = itemView.findViewById(R.id.new_timeline_date);
            _venue = itemView.findViewById(R.id.new_timeline_venue);
            _moduleCode = itemView.findViewById(R.id.new_timeline_module_code);
            _moduleName = itemView.findViewById(R.id.new_timeline_module_name);
            _saveButton = itemView.findViewById(R.id.new_timeline_item_save_card_button);

            setEditViewData();
        }

        public void setEditViewData()
        {
            _time.addTextChangedListener(new TextWatcher()
            {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after)
                {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count)
                {
                    _newTimeline.get(getAdapterPosition()).setTime(_time.getText().toString());
                }

                @Override
                public void afterTextChanged(Editable s)
                {

                }
            });
            _date.addTextChangedListener(new TextWatcher()
            {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after)
                {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count)
                {
                    _newTimeline.get(getAdapterPosition()).setDate(_date.getText().toString());
                }

                @Override
                public void afterTextChanged(Editable s)
                {

                }
            });
            _venue.addTextChangedListener(new TextWatcher()
            {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after)
                {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count)
                {
                    _newTimeline.get(getAdapterPosition()).setVenue(_venue.getText().toString());
                }

                @Override
                public void afterTextChanged(Editable s)
                {

                }
            });
            _moduleCode.addTextChangedListener(new TextWatcher()
            {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after)
                {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count)
                {
                    _newTimeline.get(getAdapterPosition()).setModuleCode(_moduleCode.getText().toString());
                }

                @Override
                public void afterTextChanged(Editable s)
                {

                }
            });
            _moduleName.addTextChangedListener(new TextWatcher()
            {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after)
                {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count)
                {
                    _newTimeline.get(getAdapterPosition()).setModuleName(_moduleName.getText().toString());
                }

                @Override
                public void afterTextChanged(Editable s)
                {

                }
            });
        }
    }
}
