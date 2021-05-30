package com.example.jonathan_gorovoy_android;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.jonathan_gorovoy_android.classes.CalendarView2;

import java.time.Month;
import java.util.Calendar;
import java.util.Date;

public class MonthCalendarActivity extends AppCompatActivity {

    int year, month;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_month_calendar);

        Intent intent = getIntent();
        String sourceActivity = intent.getStringExtra("source_activity");
        if(sourceActivity.equals("activity_week_calendar"))
        {
            year = intent.getIntExtra("year", 2000);
            month = intent.getIntExtra("month", 1);
        }

        ActionBar ab = getSupportActionBar();
        if(ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }

        CalendarView2 cv = ((CalendarView2)findViewById(R.id.calendarView));

        cv.setEventHandler(new CalendarView2.EventHandler() {
            @Override
            public void onDayPress(Date date) {
                Toast.makeText(MonthCalendarActivity.this, date.toString(), Toast.LENGTH_LONG).show();
                Calendar dateCal = Calendar.getInstance();
                dateCal.setTime(date);
                Intent i = new Intent(MonthCalendarActivity.this, ModifyDayActivity.class);
                i.putExtra("source_activity", "activity_month_calendar");
                i.putExtra("year", dateCal.get(Calendar.YEAR));
                i.putExtra("month", dateCal.get(Calendar.MONTH)+1);
                i.putExtra("day", dateCal.get(Calendar.DAY_OF_MONTH));
                startActivity(i);
            }

            @Override
            public void onWeekLongPress(Date date) {
                Toast.makeText(MonthCalendarActivity.this, date.toString(), Toast.LENGTH_LONG).show();
                Calendar dateCal = Calendar.getInstance();
                dateCal.setTime(date);
                Intent i = new Intent(MonthCalendarActivity.this, WeekCalendarActivity.class);
                i.putExtra("source_activity", "activity_month_calendar");
                i.putExtra("year", dateCal.get(Calendar.YEAR));
                i.putExtra("month", dateCal.get(Calendar.MONTH)+1);
                startActivity(i);
            }
        });

    }

    public void onClick(View view) {
        /*
        Intent i;
        switch(view.getId())
        {
            case R.id.button41:
                i = new Intent(this, MainActivity.class);
                i.putExtra("source_activity", "activity_month_calendar");
                startActivity(i);
                break;
            case R.id.button42:
                i = new Intent(this, WeekCalendarActivity.class);
                i.putExtra("source_activity", "activity_month_calendar");
                i.putExtra("year", year);
                i.putExtra("month", month);
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
                Intent i = new Intent(this, MainActivity.class);
                i.putExtra("source_activity", "activity_month_calendar");
                startActivity(i);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}