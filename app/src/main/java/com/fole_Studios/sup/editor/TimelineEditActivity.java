package com.fole_Studios.sup.editor;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.fole_Studios.sup.R;
import com.fole_Studios.sup.adapters.NewTimelineAdapter;
import com.fole_Studios.sup.adapters.TDayViewPagerAdapter;
import com.fole_Studios.sup.models.NewTimeline;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Objects;

public class TimelineEditActivity extends AppCompatActivity
{

    private TabLayout _tabLayout;
    private ViewPager _viewPager;
    private ChipGroup _chipGroup;
    private Chip _chip;
    private String _chipName;
    private RecyclerView _recyclerView;
    private ArrayList<NewTimeline> _newTimeline;
    private NewTimelineAdapter _adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline_edit);

        _tabLayout = findViewById(R.id.edit_timeline_tablayout);
        _viewPager = findViewById(R.id.edit_timeline_viewpager);
        _chipGroup = findViewById(R.id.edit_timeline_choice_type_chips);
        _recyclerView = findViewById(R.id.edit_timeline_new_exam_recyclerview);

        _chipGroup.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(ChipGroup group, int checkedId)
            {
                selectedChip();
                initRecyclerview();
            }
        });

        initViewPager();
        initRecyclerview();
    }

    private void initRecyclerview()
    {
        dataList();
        _adapter = new NewTimelineAdapter(_newTimeline, null);
        _recyclerView.setLayoutManager(new LinearLayoutManager(this));
        _recyclerView.setAdapter(_adapter);
        _adapter.notifyDataSetChanged();
    }

    private void dataList()
    {
        _newTimeline = new ArrayList<>();
        _newTimeline.add(new NewTimeline("", "", "", "", ""));
    }

    public void selectedChip()
    {
        _chipName = "";

        for(int i = 0; i < _chipGroup.getChildCount(); i++)
        {
            _chip = (Chip) _chipGroup.getChildAt(i);
            if(_chip.isChecked())
            {
                //this chip is selected.....
                _chipName = _chip.getText().toString();
                if(_chipName.contains("Exam"))
                {
                    _tabLayout.setVisibility(View.INVISIBLE);
                    _viewPager.setVisibility(View.GONE);
                    _recyclerView.setVisibility(View.VISIBLE);
                }
                else
                {
                    _tabLayout.setVisibility(View.VISIBLE);
                    _viewPager.setVisibility(View.VISIBLE);
                    _recyclerView.setVisibility(View.GONE);
                }
            }
        }
    }

    private void initViewPager()
    {
        TDayViewPagerAdapter _adapter = new TDayViewPagerAdapter(this.getSupportFragmentManager(), _tabLayout.getTabCount());
        _viewPager.setAdapter(_adapter);
        _tabLayout.setupWithViewPager(_viewPager);

        //Setting text to the tab items
        Objects.requireNonNull(_tabLayout.getTabAt(0)).setText(R.string.monday);
        Objects.requireNonNull(_tabLayout.getTabAt(1)).setText(R.string.tuesday);
        Objects.requireNonNull(_tabLayout.getTabAt(2)).setText(R.string.wednesday);
        Objects.requireNonNull(_tabLayout.getTabAt(3)).setText(R.string.thursday);
        Objects.requireNonNull(_tabLayout.getTabAt(4)).setText(R.string.friday);
        Objects.requireNonNull(_tabLayout.getTabAt(5)).setText(R.string.saturday);
        Objects.requireNonNull(_tabLayout.getTabAt(6)).setText(R.string.sunday);

        _viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(_tabLayout));
        _tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener()
        {
            @Override
            public void onTabSelected(TabLayout.Tab tab)
            {
                _viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab)
            {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab)
            {

            }
        });
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        initViewPager();
    }

}