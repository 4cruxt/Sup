package com.fole_Studios.sup;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.fole_Studios.sup.navigation.DashboardFragment;
import com.fole_Studios.sup.navigation.EventFragment;
import com.fole_Studios.sup.navigation.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity
{

    public static Activity selfIntent;
    private Fragment _fragment;
    private static FloatingActionButton _timelineButton;
    private static FloatingActionButton _annButton;
    private static FloatingActionButton _assignButton;

    public static void animateFab(int position)
    {
        if(position == 1)
        {
            _timelineButton.show();
            _annButton.hide(new FloatingActionButton.OnVisibilityChangedListener()
            {
                @Override
                public void onHidden(FloatingActionButton fab)
                {
                    super.onHidden(fab);
                    _timelineButton.show();
                }
            });
            _assignButton.hide(new FloatingActionButton.OnVisibilityChangedListener()
            {
                @Override
                public void onHidden(FloatingActionButton fab)
                {
                    super.onHidden(fab);
                    _timelineButton.show();
                }
            });
        }
        else if(position == 2)
        {
            _timelineButton.hide(new FloatingActionButton.OnVisibilityChangedListener()
            {
                @Override
                public void onHidden(FloatingActionButton fab)
                {
                    super.onHidden(fab);
                    _annButton.show();
                }
            });
        }
        else if(position == 3)
        {
            _timelineButton.hide(new FloatingActionButton.OnVisibilityChangedListener()
            {
                @Override
                public void onHidden(FloatingActionButton fab)
                {
                    super.onHidden(fab);
                    _assignButton.show();
                }
            });
        }
        else
        {
            _annButton.hide();
            _assignButton.hide();
            _timelineButton.hide();
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navigationListener()
    {

        return new BottomNavigationView.OnNavigationItemSelectedListener()
        {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item)
            {
                switch(item.getItemId())
                {
                    case R.id.m_b_dashboard:
                        _fragment = new DashboardFragment();
                        loadFragment(_fragment);
                        return true;
                    case R.id.m_b_events:
                        _fragment = new EventFragment();
                        loadFragment(_fragment);
                        return true;
                    case R.id.m_b_profile:
                        _fragment = new ProfileFragment();
                        loadFragment(_fragment);
                        return true;
                }
                return false;
            }
        };
    }

    private void loadFragment(Fragment fragment)
    {
        FragmentTransaction _transaction = getSupportFragmentManager().beginTransaction();
        _transaction.replace(R.id.main_fragment_container, fragment);
        _transaction.commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        selfIntent = this;

        _timelineButton = findViewById(R.id.main_add_timeline_button);
        _annButton = findViewById(R.id.main_add_ann_button);
        _assignButton = findViewById(R.id.main_add_assign_button);

        BottomNavigationView _bottomNavigationView = findViewById(R.id.main_bottom_navigation);
        _bottomNavigationView.setOnNavigationItemSelectedListener(navigationListener());

        int _fragmentId = getIntent().getIntExtra("Fragment_id", 0);

        if(_fragmentId == 2)
        {
            _fragment = new ProfileFragment();
            loadFragment(_fragment);
            _bottomNavigationView.getMenu().getItem(2).setChecked(true);
        }
        else
        {
            //Opening the first fragment.
            _bottomNavigationView.getMenu().getItem(0).setChecked(true);
            _fragment = new DashboardFragment();
            loadFragment(_fragment);
        }


    }

}