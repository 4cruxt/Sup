package com.fole_Studios.sup.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.fole_Studios.sup.editor.NewTimelineFragment;

@SuppressWarnings("ALL")
public class TDayViewPagerAdapter extends FragmentStatePagerAdapter
{
    private int _tabNumber;

    public TDayViewPagerAdapter(@NonNull FragmentManager fm, int tabNumber)
    {
        super(fm);
        _tabNumber = tabNumber;
    }

    @NonNull
    @Override
    public Fragment getItem(int position)
    {
        switch(position)
        {
            case 0:
                return new NewTimelineFragment("Mon");
            case 1:
                return new NewTimelineFragment("Tue");
            case 2:
                return new NewTimelineFragment("Wed");
            case 3:
                return new NewTimelineFragment("Thu");
            case 4:
                return new NewTimelineFragment("Fri");
            case 5:
                return new NewTimelineFragment("Sat");
            case 6:
                return new NewTimelineFragment("Sun");
            default:
                return null;
        }
    }

    @Override
    public int getCount()
    {
        return _tabNumber;
    }
}
