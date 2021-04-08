package com.example.jonathan_gorovoy_android;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.example.jonathan_gorovoy_android.adapters.EventDayViewAdapter;
import com.example.jonathan_gorovoy_android.classes.EventDayView;

import java.util.ArrayList;
import java.util.GregorianCalendar;

public class ModifyDayActivity extends AppCompatActivity {

    Button btn1, btn2;
    int year, month, rowInMonth;
    int day, eventIndex;
    int routineIndex;
    boolean isSpecificDay;

    ListView eventList;
    ArrayList<EventDayView> eventArray = new ArrayList<EventDayView>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_day);

        Intent intent = getIntent();
        String sourceActivity = intent.getStringExtra("source_activity");
        if(sourceActivity.equals("activity_month_calendar") || sourceActivity.equals("activity_week_calendar") || sourceActivity.equals("activity_modify_event"))
        {
            year = intent.getIntExtra("year", 2000);
            month = intent.getIntExtra("month", 1);
            rowInMonth = intent.getIntExtra("rowInMonth", 1);
            day = intent.getIntExtra("day", 1);
        }
        else if(sourceActivity.equals("activity_view_routines"))
        {
            routineIndex = intent.getIntExtra("routineIndex", 1);
        }

        ActionBar ab = getSupportActionBar();
        if(ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }

        btn1=(Button)findViewById(R.id.button11);
        btn2=(Button)findViewById(R.id.button12);

        eventList = (ListView)findViewById(R.id.eventList);
        getEventsDemo(); // TODO: replace by query of database for events in the given day
        EventDayViewAdapter edva = new EventDayViewAdapter(this, R.layout.event_day_view, eventArray);
        eventList.setAdapter(edva);
        AdapterView.OnItemClickListener eventListListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EventDayView item = eventArray.get(position);
                //TODO: query database using item functions and the other variables to find eventIndex
                Intent i = new Intent(ModifyDayActivity.this, ModifyEventActivity.class);
                i.putExtra("source_activity", "activity_modify_day");
                i.putExtra("year", year);
                i.putExtra("month", month);
                i.putExtra("day", day);
                i.putExtra("rowInMonth", rowInMonth);
                i.putExtra("eventIndex", eventIndex);
                i.putExtra("isSpecificDay", isSpecificDay);
                startActivity(i);
            }
        };
        eventList.setOnItemClickListener(eventListListener);

        btn1.setOnClickListener(this::onClick);
        btn2.setOnClickListener(this::onClick);
    }

    public void getEventsDemo()
    {
        int demoYear = 2019;
        int demoMonth = 8;
        int demoDay = 25;
        EventDayView ev = new EventDayView("15:00", "16:30", "Walk dog out", "Take dog out for a walk around the park");
        ev.setInPast(demoYear, demoMonth, demoDay, true, GregorianCalendar.getInstance().getTime());
        eventArray.add(ev);
        ev = new EventDayView("16:30", "17:00", "Lunch", "Grab lunch from the fridge");
        ev.setInPast(2021, demoMonth, demoDay, true, GregorianCalendar.getInstance().getTime());
        eventArray.add(ev);
        ev = new EventDayView("17:20", "18:00", "Watch Lesson", "Watch the lesson recorded by math teacher in preparation for the test");
        ev.setInPastValue(true);
        eventArray.add(ev);
        ev = new EventDayView("18:00", "19:30", "Code Android project", "Add new adapter view to android project xml file");
        eventArray.add(ev);
    }

    public void onClick(View view) {
        /*
        Intent i;
        switch(view.getId())
        {
            case R.id.button11:
                i = new Intent(this, WeekCalendarActivity.class);
                i.putExtra("source_activity", "activity_modify_day");
                i.putExtra("year", year);
                i.putExtra("month", month);
                i.putExtra("rowInMonth", rowInMonth);
                startActivity(i);
                break;
            case R.id.button12:
                i = new Intent(this, ModifyEventActivity.class);
                i.putExtra("source_activity", "activity_modify_day");
                i.putExtra("year", year);
                i.putExtra("month", month);
                i.putExtra("rowInMonth", rowInMonth);
                i.putExtra("eventIndex", eventIndex);
                i.putExtra("isSpecificDay", isSpecificDay);
                i.putExtra("day", day);
                startActivity(i);
                break;
        }
        */
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                Intent i = new Intent(this, WeekCalendarActivity.class);
                i.putExtra("source_activity", "activity_modify_day");
                i.putExtra("year", year);
                i.putExtra("month", month);
                i.putExtra("rowInMonth", rowInMonth);
                startActivity(i);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}