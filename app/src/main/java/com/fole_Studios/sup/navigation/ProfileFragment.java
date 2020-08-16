package com.fole_Studios.sup.navigation;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.fole_Studios.sup.BottomSettingFragment;
import com.fole_Studios.sup.EditProfileActivity;
import com.fole_Studios.sup.R;

import java.util.Objects;

import static com.fole_Studios.sup.database.DBqueries.enableFloatingButton;
import static com.fole_Studios.sup.database.DBqueries.getUserDataFromDatabase;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment
{

    private static final int PROF_FRAGMENT_ID = 5;
    private ImageView _settingButton;
    private ImageView _verificationBadge;
    private Button _editButton;
    private TextView _registrationNumber, _courseName, _username, _userBio;

    public ProfileFragment()
    {
        //Required empty constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View _view = inflater.inflate(R.layout.fragment_profile, container, false);

        _settingButton = _view.findViewById(R.id.p_main_setting_button);
        _registrationNumber = _view.findViewById(R.id.p_m_c_reg_num);
        _courseName = _view.findViewById(R.id.p_m_c_course_name);
        _username = _view.findViewById(R.id.p_m_c_username);
        _userBio = _view.findViewById(R.id.p_m_c_bio);
        _editButton = _view.findViewById(R.id.p_m_c_edit_pro_button);
        _verificationBadge = _view.findViewById(R.id.p_m_c_verified_badge);

        enableFloatingButton(PROF_FRAGMENT_ID);
        enableBottomNav();
        loadDatabaseData();
        return _view;
    }

    private void loadDatabaseData()
    {
        getUserDataFromDatabase(_username, _userBio, _courseName, _registrationNumber, _editButton, _verificationBadge);
    }


    private void enableBottomNav()
    {
        _editButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent editProfileIntent = new Intent(v.getContext(), EditProfileActivity.class);
                v.getContext().startActivity(editProfileIntent);
            }
        });
        _settingButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                promptBottomNav();
            }
        });
    }

    private void promptBottomNav()
    {
        BottomSettingFragment _bottomSettingFragment = new BottomSettingFragment();
        _bottomSettingFragment.show(Objects.requireNonNull(getActivity()).getSupportFragmentManager(), _bottomSettingFragment.getTag());
    }

}