package com.fole_Studios.sup;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import static com.fole_Studios.sup.database.DBqueries.getUserDataFromDatabase;
import static com.fole_Studios.sup.database.DBqueries.updateUserDataInDatabase;

public class EditProfileActivity extends AppCompatActivity
{

    private EditText _regNumber, _username, _courseName, _userBio;
    private ImageButton _saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        _regNumber = findViewById(R.id.edit_pro_registration_number);
        _username = findViewById(R.id.edit_pro_username);
        _courseName = findViewById(R.id.edit_pro_course);
        _userBio = findViewById(R.id.edit_pro_bio);
        _saveButton = findViewById(R.id.edit_pro_save_button);

        getUserDataFromDatabase(_username, _userBio, _courseName, _regNumber, null, null);

        saveData();

    }

    private void saveData()
    {
        _saveButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                updateUser();
                openMainIntent();
            }
        });
    }

    private void updateUser()
    {
        updateUserDataInDatabase(_username, _regNumber, _courseName, _userBio, this);
    }

    private void openMainIntent()
    {
        MainActivity.selfIntent.finish();
        Intent mainIntent = new Intent(EditProfileActivity.this, MainActivity.class);
        mainIntent.putExtra("Fragment_id", 2);
        startActivity(mainIntent);
        finish();
    }

}