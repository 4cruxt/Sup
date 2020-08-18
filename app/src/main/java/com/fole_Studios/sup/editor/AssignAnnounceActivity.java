package com.fole_Studios.sup.editor;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.fole_Studios.sup.R;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import static com.fole_Studios.sup.MainActivity.ANNOUNCEMENT_BUTTON_ID;
import static com.fole_Studios.sup.MainActivity.ASSIGNMENT_BUTTON_ID;
import static com.fole_Studios.sup.MainActivity.BUTTON_ID_TAG;

public class AssignAnnounceActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assign_announce);

        int _buttonId = getIntent().getIntExtra(BUTTON_ID_TAG, 0);
        TextView _title = findViewById(R.id.edit_assign_announce_main_title);
        ChipGroup _chips = findViewById(R.id.edit_assign_announce_choice_chips);
        TextView _contentTitle = findViewById(R.id.edit_assign_announce_content_text);
        EditText _content = findViewById(R.id.edit_assign_announce_content);

        if(_buttonId == ASSIGNMENT_BUTTON_ID)
        {
            setupNewAssignment(_title, _chips, _contentTitle, _content);
        }
        else if(_buttonId == ANNOUNCEMENT_BUTTON_ID)
        {
            setupNewAnnouncement(_title, _chips, _contentTitle, _content);
        }
        else
        {
            //If the button is 0 end the edit activity.
            this.finish();
        }
    }

    private void setupNewAssignment(TextView title, ChipGroup chips, TextView contentTitle, EditText content)
    {
        Chip _chipOne = (Chip) chips.getChildAt(0);
        Chip _chipTwo = (Chip) chips.getChildAt(1);

        //Assign content values
        title.setText(getResources().getString(R.string.edit_new_assignment));
        _chipOne.setText(getResources().getString(R.string.individual_assignment));
        _chipTwo.setText(getResources().getString(R.string.group_assignment));
        contentTitle.setText(getResources().getString(R.string.assignment));

        /*
          @parameter Content
         * Data in this parameter should be stored into the firestore database.
         * @parameter chips
         * Determine the selected chip and extract its text as a type and store it in the firestore database.
        */

    }

    private void setupNewAnnouncement(TextView title, ChipGroup chips, TextView contentTitle, EditText content)
    {
        Chip _chipOne = (Chip) chips.getChildAt(0);
        Chip _chipTwo = (Chip) chips.getChildAt(1);
        Chip _chipThree = (Chip) chips.getChildAt(2);

        //Assign content values
        title.setText(getResources().getString(R.string.edit_new_announcement));
        _chipOne.setText(getResources().getString(R.string.class_announcement));
        _chipTwo.setText(getResources().getString(R.string.exam_announcement));
        _chipThree.setText(getResources().getString(R.string.general_announcement));
        _chipThree.setVisibility(View.VISIBLE);

        /*
          @parameter Content
         * Data in this parameter should be stored into the firestore database.
         * @parameter chips
         * Determine the selected chip and extract its text as a type and store it in the firestore database.
        */

    }
}