package com.fole_Studios.sup;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.fole_Studios.sup.adapters.SliderAdapter;

public class OnBoardActivity extends AppCompatActivity
{
    private ViewPager _onboardViewPager;
    private LinearLayout _pagination;
    private SliderAdapter _sliderAdapter;
    private TextView[] _dots;

    private Button _backButton;
    private Button _nextButton;
    private Button _finishButton;

    private int _currentPage;
    ViewPager.OnPageChangeListener _viewPagerListener = new ViewPager.OnPageChangeListener()
    {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
        {

        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onPageSelected(int position)
        {
            addDotsIndicator(position);

            _currentPage = position;

            if(position == 0)
            {
                _nextButton.setEnabled(true);
                _backButton.setEnabled(false);
                _finishButton.setEnabled(false);
                _backButton.setVisibility(View.INVISIBLE);
                _finishButton.setVisibility(View.INVISIBLE);

                _nextButton.setText("Next");
                _backButton.setText("");

            }
            else if(position == _dots.length - 1)
            {
                _nextButton.setEnabled(false);
                _backButton.setEnabled(true);
                _finishButton.setEnabled(true);
                _backButton.setVisibility(View.VISIBLE);
                _finishButton.setVisibility(View.VISIBLE);

                _nextButton.setText("");
                _backButton.setText("Back");

            }
            else
            {
                _nextButton.setEnabled(true);
                _backButton.setEnabled(true);
                _finishButton.setEnabled(false);
                _backButton.setVisibility(View.VISIBLE);
                _finishButton.setVisibility(View.INVISIBLE);

                _nextButton.setText("Next");
                _backButton.setText("Back");
            }

        }

        @Override
        public void onPageScrollStateChanged(int state)
        {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_board);

        _onboardViewPager = findViewById(R.id.onboard_viewpager);
        _pagination = findViewById(R.id.pagination);
        _backButton = findViewById(R.id.back_button);
        _nextButton = findViewById(R.id.next_button);
        _finishButton = findViewById(R.id.finish_button);

        initSliderAdapter();
        addDotsIndicator(0);
        onClickOnboardButton();
        dataSliderSwitch();

    }

    private void dataSliderSwitch()
    {
        _sliderAdapter.slider_images = new int[]{R.drawable.ic_lecture_24, R.drawable.ic_assignment_24, R.drawable.ic_event_24};
        _sliderAdapter.slider_descriptions = new String[]{"Get notified for every ongoing and upcoming lectures", "Stay updated with assignments provided by lecturers", "Get notified for any event occurrence"};

    }


    @SuppressLint("SetTextI18n")
    private void onClickOnboardButton()
    {

        _nextButton.setText("Next");
        _nextButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                _onboardViewPager.setCurrentItem(_currentPage + 1);
            }
        });

        _backButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                _onboardViewPager.setCurrentItem(_currentPage - 1);
            }
        });

        _finishButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                openMainIntent();
            }
        });

    }

    private void initSliderAdapter()
    {
        _sliderAdapter = new SliderAdapter(this);
        _onboardViewPager.setAdapter(_sliderAdapter);

        _onboardViewPager.addOnPageChangeListener(_viewPagerListener);
    }

    public void addDotsIndicator(int position)
    {
        _dots = new TextView[3];
        _pagination.removeAllViews();

        for(int i = 0; i < _dots.length; i++)
        {
            _dots[i] = new TextView(this);
            _dots[i].setText(Html.fromHtml("&#8226;"));
            _dots[i].setTextSize(30);
            _dots[i].setTextColor(getResources().getColor(R.color.colorPrimaryPartial));

            _pagination.addView(_dots[i]);

        }

        if(_dots.length > 0)
        {
            _dots[position].setTextColor(getResources().getColor(R.color.colorPrimary));
            _dots[position].setTextSize(36);
        }
    }

    private void openMainIntent()
    {
        Intent mainIntent = new Intent(this, MainActivity.class);
        startActivity(mainIntent);
        finish();
    }
}