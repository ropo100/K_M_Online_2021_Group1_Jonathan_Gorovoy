package com.example.jonathan_gorovoy_android;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

public class ModifyEventActivity extends AppCompatActivity {

    Button btn1, btn2;
    int year=0, month=0, day=0, eventIndex=0, routineIndex=0;
    int dueDay=0, dueMonth=0, dueYear=0;
    boolean isSpecificDay=true, isDeadline=false;
    String title="", description="", startHour="", endHour="", dueDate="";
    String sourceActivity;
    Dal dal;

    CheckBox deadlineCheckbox;
    EditText editTitle, editDescription, editStartHour, editEndHour, editDeadline;
    InputFilter filterHour; // filter for 23:59 input
    boolean doneOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_event);

        createTimeFilter();

        dal = new Dal(ModifyEventActivity.this);

        editTitle = (EditText)findViewById(R.id.editTitle);
        editDescription = (EditText)findViewById(R.id.editDescription);
        editStartHour = (EditText)findViewById(R.id.eventStartTime);
        editEndHour = (EditText)findViewById(R.id.eventEndTime);
        deadlineCheckbox = (CheckBox)findViewById(R.id.isDeadlineCheckbox);

        editStartHour.setFilters(new InputFilter[]{filterHour});
        editEndHour.setFilters(new InputFilter[]{filterHour});

        Intent intent = getIntent();
        sourceActivity = intent.getStringExtra("source_activity");
        isSpecificDay = intent.getBooleanExtra("isSpecificDay", false);
        eventIndex = intent.getIntExtra("eventIndex", 0);
        routineIndex = intent.getIntExtra("routineIndex", 0);
        isDeadline = intent.getBooleanExtra("isDeadline", false);
        deadlineCheckbox.setChecked(isDeadline);
        if (isSpecificDay) {
            year = intent.getIntExtra("year", 2000);
            month = intent.getIntExtra("month", 1);
            day = intent.getIntExtra("day", 1);
        }
        if(eventIndex != 0) {
            title = intent.getStringExtra("title");
            description = intent.getStringExtra("description");
            startHour = intent.getStringExtra("startHour");
            endHour = intent.getStringExtra("endHour");
            editTitle.setText(title);
            editDescription.setText(description);
            editStartHour.setText(startHour);
            editEndHour.setText(endHour);
            //TODO: also disable ability to edit if the event happened in a date before today(in the past)

        }

        ActionBar ab = getSupportActionBar();
        if(ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }

        btn1=(Button)findViewById(R.id.btnApply);
        btn2=(Button)findViewById(R.id.btnDelete);

        btn1.setOnClickListener(this::onClick);
        btn2.setOnClickListener(this::onClick);
    }

    public void onClick(View view) {
        switch(view.getId())
        {
            case R.id.btnApply:
                startHour = editStartHour.getText().toString();
                endHour = editEndHour.getText().toString();
                title = editTitle.getText().toString();
                description = editDescription.getText().toString();
                dueDate = editDeadline.getText().toString();
                isDeadline = deadlineCheckbox.isChecked();
                dal.addEvent(day, month, year, startHour, endHour, title, description, routineIndex, eventIndex, isDeadline);
                finishAndReturn();
                break;
            case R.id.btnDelete:
                dal.deleteEvent(eventIndex);
                finishAndReturn();
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finishAndReturn();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void finishAndReturn()
    {
        //Move back to the same screen you entered from with the same intent parameters they need to load the same thing
        Intent i;
        if(sourceActivity.equals("activity_modify_day"))
        {
            i = new Intent(this, ModifyDayActivity.class);
            i.putExtra("source_activity", "activity_modify_event");
            i.putExtra("day",day);
            i.putExtra("month",month);
            i.putExtra("year",year);
            i.putExtra("routineIndex",routineIndex);
            i.putExtra("isSpecificDay", isSpecificDay);
            startActivity(i);
        }
        else if(sourceActivity.equals("activity_view_deadlines"))
        {
            i = new Intent(this, ViewDeadlinesActivity.class);
            i.putExtra("source_activity", "activity_modify_event");
            startActivity(i);
        }
        else
        {
            i = new Intent(this, MainActivity.class);
            i.putExtra("source_activity", "activity_modify_event");
            startActivity(i);
        }
    }

    private void createTimeFilter() {
        filterHour  = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest,
                                       int dstart, int dend) {

                if(source.length() > 1 && !doneOnce){
                    source = source.subSequence(source.length()-1, source.length());
                    if(source.charAt(0)  >= '0' && source.charAt(0) <= '2'){
                        doneOnce = true;
                        return source;
                    }else{
                        return "";
                    }
                }


                if (source.length() == 0) {
                    return null;// deleting, keep original editing
                }
                String result = "";
                result += dest.toString().substring(0, dstart);
                result += source.toString().substring(start, end);
                result += dest.toString().substring(dend, dest.length());

                if (result.length() > 5) {
                    return "";// do not allow this edit
                }
                boolean allowEdit = true;
                char c;
                if (result.length() > 0) {
                    c = result.charAt(0);
                    allowEdit = (c >= '0' && c <= '2');
                }
                if (result.length() > 1) {
                    c = result.charAt(1);
                    if(result.charAt(0) == '0' || result.charAt(0) == '1')
                        allowEdit = allowEdit && (c >= '0' && c <= '9');
                    else
                        allowEdit = allowEdit && (c >= '0' && c <= '3');
                }
                if (result.length() > 2) {
                    c = result.charAt(2);
                    allowEdit = allowEdit && (c == ':');
                }
                if (result.length() > 3) {
                    c = result.charAt(3);
                    allowEdit = allowEdit && (c >= '0' && c <= '5');
                }
                if (result.length() > 4) {
                    c = result.charAt(4);
                    allowEdit = allowEdit && (c >= '0' && c <= '9');
                }
                return allowEdit ? null : "";
            }

        };
    }
}