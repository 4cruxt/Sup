package com.fole_Studios.sup.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;

import com.fole_Studios.sup.R;

@SuppressWarnings("ALL")
public class SliderAdapter extends PagerAdapter
{
    //Arrays
    public int[] slider_images = {R.drawable.ic_lecture_24, R.drawable.ic_assignment_24, R.drawable.ic_event_24};
    public String[] slider_headings = {"LECTURE", "ASSIGNMENTS", "EVENTS"};
    public String[] slider_descriptions = {"Get notified for every ongoing and upcoming lectures", "Stay updated with assignemnts provided by lecturers", "Get notified for any event occurrence"};
    Context _context;
    LayoutInflater _layoutInflater;

    public SliderAdapter(Context context)
    {
        _context = context;
    }

    @Override
    public int getCount()
    {
        return slider_headings.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object)
    {
        return view == (ConstraintLayout) object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position)
    {

        _layoutInflater = (LayoutInflater) _context.getSystemService(_context.LAYOUT_INFLATER_SERVICE);
        View _view = _layoutInflater.inflate(R.layout.slider_layout, container, false);

        ImageView _sliderImage = _view.findViewById(R.id.slider_image);
        TextView _sliderHeadingText = _view.findViewById(R.id.slider_heading);
        TextView _sliderDescriptionText = _view.findViewById(R.id.slider_description);

        _sliderImage.setImageResource(slider_images[position]);
        _sliderHeadingText.setText(slider_headings[position]);
        _sliderDescriptionText.setText(slider_descriptions[position]);

        container.addView(_view);

        return _view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object)
    {
        container.removeView((ConstraintLayout) object);
    }
}

