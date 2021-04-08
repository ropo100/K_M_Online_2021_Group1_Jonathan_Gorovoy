package com.example.jonathan_gorovoy_android;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class ModifyEventActivity extends AppCompatActivity {

    Button btn1, btn2;
    int year, month, rowInMonth, day;
    boolean isSpecificDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_event);

        Intent intent = getIntent();
        String sourceActivity = intent.getStringExtra("source_activity");
        isSpecificDay = intent.getBooleanExtra("isSpecificDay", false);
        if(isSpecificDay)
        {
            year = intent.getIntExtra("year", 2000);
            month = intent.getIntExtra("month", 1);
            rowInMonth = intent.getIntExtra("rowInMonth", 1);
            day = intent.getIntExtra("day", 1);
            //TODO: query with event index and date and fill out the boxes corresponding to a specific event on a specific day
            //also disable ability to edit if the event happened in a date before today(in the past)
        }
        else
        {
            //TODO: query with event index and fill out boxes corresponding to a deadline or an event in a routine (no specific date, only due date)
        }

        ActionBar ab = getSupportActionBar();
        if(ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }

        btn1=(Button)findViewById(R.id.button21);
        btn2=(Button)findViewById(R.id.button22);

        btn1.setOnClickListener(this::onClick);
        btn2.setOnClickListener(this::onClick);
    }

    public void onClick(View view) {
        Intent i;
        switch(view.getId())
        {
            case R.id.button21:
                //TODO: get values in fields, error check them, update or add to database if no syntax errors (for example not int in date)
                //and according to the values in due date/start hour and end hour decide if its an event in a day or routine and go to that activity or if its a deadline and go to that activity
                if(true) //TODO: change condition according to above comment
                {
                    i = new Intent(this, ViewDeadlinesActivity.class);
                    i.putExtra("source_activity", "activity_modify_event");
                    startActivity(i);
                }
                else
                {
                    i = new Intent(this, ModifyDayActivity.class);
                    i.putExtra("source_activity", "activity_modify_event");
                    i.putExtra("year", year);
                    i.putExtra("month", month);
                    i.putExtra("rowInMonth", rowInMonth);
                    i.putExtra("day", day);
                    startActivity(i);
                }
                break;
            /*case R.id.button22:
                i = new Intent(this, ModifyDayActivity.class);
                i.putExtra("source_activity", "activity_modify_event");
                i.putExtra("year", year);
                i.putExtra("month", month);
                i.putExtra("rowInMonth", rowInMonth);
                i.putExtra("day", day);
                startActivity(i);
                break;*/
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                Intent i;
                i = new Intent(this, ModifyDayActivity.class);
                i.putExtra("source_activity", "activity_modify_event");
                i.putExtra("year", year);
                i.putExtra("month", month);
                i.putExtra("rowInMonth", rowInMonth);
                i.putExtra("day", day);
                startActivity(i);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}