package com.example.jonathan_gorovoy_android;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ModifyEventActivity extends AppCompatActivity {

    Button btn1, btn2;
    int year=0, month=0, day=0, eventIndex=0, routineIndex=0;
    int dueDay=0, dueMonth=0, dueYear=0;
    boolean isSpecificDay=true, isDeadline=false;
    String title="", description="", startHour="", endHour="", dueDate="";
    String sourceActivity;
    Dal dal;

    EditText editTitle, editDescription, editStartHour, editEndHour, editDeadline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_event);

        dal = new Dal(ModifyEventActivity.this);

        editTitle = (EditText)findViewById(R.id.editTitle);
        editDescription = (EditText)findViewById(R.id.editDescription);
        editStartHour = (EditText)findViewById(R.id.eventStartTime);
        editEndHour = (EditText)findViewById(R.id.eventEndTime);
        editDeadline = (EditText)findViewById(R.id.eventDueDate);

        Intent intent = getIntent();
        sourceActivity = intent.getStringExtra("source_activity");
        isSpecificDay = intent.getBooleanExtra("isSpecificDay", false);
        eventIndex = intent.getIntExtra("eventIndex", 0);
        isDeadline = dal.isDeadline(eventIndex);
        routineIndex = intent.getIntExtra("routineIndex", 0);
        if (isSpecificDay) {
            year = intent.getIntExtra("year", 2000);
            month = intent.getIntExtra("month", 1);
            day = intent.getIntExtra("day", 1);
        }
        if(eventIndex != 0) {
            title = intent.getStringExtra("title");
            description = intent.getStringExtra("description");
            endHour = intent.getStringExtra("endHour");
            editTitle.setText(title);
            editDescription.setText(description);
            editEndHour.setText(endHour);
            if(isDeadline)
            { // start hour disabled for deadline
                dueDate = dal.getDeadline(eventIndex);
                editDeadline.setText(dueDate);
            }
            else {
                startHour = intent.getStringExtra("startHour");
                editStartHour.setText(startHour);
            }
            //also disable ability to edit if the event happened in a date before today(in the past)
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

                dal.addEvent(day, month, year, startHour, endHour, title, description, routineIndex, eventIndex);

                //TODO: separate dueDate into dueDay, dueMonth, dueYear
                if(isDeadline && !dueDate.equals(""))
                {
                    dal.addDeadline(dueDay, dueMonth, dueYear, eventIndex);
                }
                else if (isDeadline && dueDate.equals(""))
                { //if it was a deadline but is now empty as in no longer a deadline
                    dal.deleteDeadline(eventIndex);
                }
                finishAndReturn();
                break;
            case R.id.btnDelete:
                if(isDeadline) {
                    dal.deleteDeadline(eventIndex);
                }
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
}