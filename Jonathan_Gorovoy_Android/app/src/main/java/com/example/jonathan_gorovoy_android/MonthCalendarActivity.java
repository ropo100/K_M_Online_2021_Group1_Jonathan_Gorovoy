package com.example.jonathan_gorovoy_android;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.jonathan_gorovoy_android.classes.CalendarView2;

import java.util.Calendar;
import java.util.Date;

@SuppressWarnings({"ALL", "Convert2Lambda"})
public class MonthCalendarActivity extends AppCompatActivity {

    int year, month;

    @SuppressWarnings("Convert2Lambda")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_month_calendar);

        Intent intent = getIntent();
        Calendar today = Calendar.getInstance();
        year = intent.getIntExtra("year", today.get(Calendar.YEAR));
        month = intent.getIntExtra("month", today.get(Calendar.MONTH)+1);

        ActionBar ab = getSupportActionBar();
        if(ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }

        CalendarView2 cv = ((CalendarView2)findViewById(R.id.calendarView));
        cv.setStartingMonth(month, year); // sets initial starting month and year for calendar according to intent
        //noinspection Convert2Lambda
        cv.setEventHandler(new CalendarView2.EventHandler() {
            @Override
            public void onDayPress(Date date) {
                Calendar dateCal = Calendar.getInstance();
                dateCal.setTime(date);
                Intent i = new Intent(MonthCalendarActivity.this, ModifyDayActivity.class);
                i.putExtra("source_activity", "activity_month_calendar");
                i.putExtra("year", dateCal.get(Calendar.YEAR));
                i.putExtra("month", dateCal.get(Calendar.MONTH)+1);
                i.putExtra("day", dateCal.get(Calendar.DAY_OF_MONTH));
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Respond to the action bar's Up/Home button
        if (item.getItemId() == android.R.id.home) {
            Intent i = new Intent(this, MainActivity.class);
            i.putExtra("source_activity", "activity_month_calendar");
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(this, MainActivity.class);
        i.putExtra("source_activity", "activity_month_calendar");
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }
}