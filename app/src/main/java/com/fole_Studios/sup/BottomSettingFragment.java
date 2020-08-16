package com.fole_Studios.sup;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.fole_Studios.sup.authentication.AuthActivity;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Objects;

import static com.fole_Studios.sup.database.DBqueries.logoutUser;

/**
 * A simple {@link Fragment} subclass.
 */
public class BottomSettingFragment extends BottomSheetDialogFragment
{

    private TextView _editProfile, _logout;

    public BottomSettingFragment()
    {
        //Required empty constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment

        View _view = inflater.inflate(R.layout.setting_btm_sheet, container, false);

        _editProfile = _view.findViewById(R.id.s_btm_sht_p_edit_text);
        _logout = _view.findViewById(R.id.s_btm_sht_p_logout_text);

        _logout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //todo: logout out user from the system return him/her to the registration page.
                logoutUser();
                Intent authIntent = new Intent(getContext(), AuthActivity.class);
                Objects.requireNonNull(getContext()).startActivity(authIntent);
                MainActivity.selfIntent.finish();
            }
        });

        _editProfile.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent editProfileIntent = new Intent(getContext(), EditProfileActivity.class);
                Objects.requireNonNull(getContext()).startActivity(editProfileIntent);
                dismiss();
            }
        });
        return _view;
    }

}