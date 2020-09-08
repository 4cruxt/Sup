package com.fole_Studios.sup.editor;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.fole_Studios.sup.R;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import static com.fole_Studios.sup.MainActivity.ANNOUNCEMENT_BUTTON_ID;
import static com.fole_Studios.sup.MainActivity.ASSIGNMENT_BUTTON_ID;
import static com.fole_Studios.sup.MainActivity.BUTTON_ID_TAG;

public class AssignAnnounceActivity extends AppCompatActivity
{

    private Button _submitButton;
    private String _chipName;
    private ChipGroup _chips;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assign_announce);

        int _buttonId = getIntent().getIntExtra(BUTTON_ID_TAG, 0);
        TextView _title = findViewById(R.id.edit_assign_announce_main_title);
        _chips = findViewById(R.id.edit_assign_announce_choice_chips);
        TextView _contentTitle = findViewById(R.id.edit_assign_announce_content_text);
        EditText _content = findViewById(R.id.edit_assign_announce_content);
        _submitButton = findViewById(R.id.edit_assign_announce_post_button);

        if(_buttonId == ASSIGNMENT_BUTTON_ID)
        {
            setupNewAssignment(_title, _chips, _contentTitle, _content, "ASSIGNMENT");
        }
        else if(_buttonId == ANNOUNCEMENT_BUTTON_ID)
        {
            setupNewAnnouncement(_title, _chips, _contentTitle, _content, "ANNOUNCEMENT");
        }
        else
        {
            //If the button is 0 end the edit activity.
            this.finish();
        }

        _chips.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(ChipGroup group, int checkedId)
            {
                selectedChip();
            }
        });

        selectedChip();
    }

    public void selectedChip()
    {
        _chipName = "";

        for(int i = 0; i < _chips.getChildCount(); i++)
        {
            Chip _chip = (Chip) _chips.getChildAt(i);
            if(_chip.isChecked())
            {
                //this chip is selected.....
                _chipName = _chip.getText().toString();
            }
        }
    }

    private void setupNewAssignment(TextView title, ChipGroup chips, TextView contentTitle, EditText content, String setupType)
    {
        Chip _chipOne = (Chip) chips.getChildAt(0);
        Chip _chipTwo = (Chip) chips.getChildAt(1);

        //Assign content values
        title.setText(getResources().getString(R.string.edit_new_assignment));
        String _setType = setupType;
        _chipOne.setText(getResources().getString(R.string.individual_assignment));
        _chipTwo.setText(getResources().getString(R.string.group_assignment));
        contentTitle.setText(getResources().getString(R.string.assignment));

        /*
          @parameter Content
         * Data in this parameter should be stored into the firestore database.
         * @parameter chips
         * Determine the selected chip and extract its text as a type and store it in the firestore database.
        */

        _submitButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Toast.makeText(AssignAnnounceActivity.this, "" + _chipName, Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void setupNewAnnouncement(TextView title, ChipGroup chips, TextView contentTitle, EditText content, String setupType)
    {
        Chip _chipOne = (Chip) chips.getChildAt(0);
        Chip _chipTwo = (Chip) chips.getChildAt(1);
        Chip _chipThree = (Chip) chips.getChildAt(2);

        //Assign content values
        String _setType = setupType;
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

        _submitButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Toast.makeText(AssignAnnounceActivity.this, "" + _chipName, Toast.LENGTH_SHORT).show();
            }
        });
    }

}