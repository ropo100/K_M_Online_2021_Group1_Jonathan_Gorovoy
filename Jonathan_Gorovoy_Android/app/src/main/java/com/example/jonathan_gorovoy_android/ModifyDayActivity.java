package com.example.jonathan_gorovoy_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class ModifyDayActivity extends AppCompatActivity {

    Button btn1, btn2;
    int year, month, rowInMonth;
    int day, eventIndex;
    boolean isSpecificDay;

    ListView eventList;
    ArrayList<EventDayView> eventArray = new ArrayList<EventDayView>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_day);

        btn1=(Button)findViewById(R.id.button11);
        btn2=(Button)findViewById(R.id.button12);

        eventList = (ListView)findViewById(R.id.eventList);
        getEventsDemo();
        EventDayViewAdapter edva = new EventDayViewAdapter(this, R.layout.event_day_view, eventArray);
        eventList.setAdapter(edva);

        btn1.setOnClickListener(this::onClick);
        btn2.setOnClickListener(this::onClick);
    }

    public void getEventsDemo()
    {
        EventDayView ev = new EventDayView("15:00", "16:30", "Walk dog out", "Take dog out for a walk around the park");
        eventArray.add(ev);
        ev = new EventDayView("16:30", "17:00", "Lunch", "Grab lunch from the fridge");
        eventArray.add(ev);
        ev = new EventDayView("17:20", "18:00", "Watch Lesson", "Watch the lesson recorded by math teacher in preparation for the test");
        eventArray.add(ev);
        ev = new EventDayView("18:00", "19:30", "Code Android project", "Add new adapter view to android project xml file");
        eventArray.add(ev);
    }

    public void onClick(View view) {
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
    }
}