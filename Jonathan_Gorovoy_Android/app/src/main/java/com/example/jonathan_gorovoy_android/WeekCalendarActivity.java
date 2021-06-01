package com.example.jonathan_gorovoy_android;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class WeekCalendarActivity extends AppCompatActivity {

    Button btn1, btn2, btn3;
    int year, month, day, rowInMonth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_week_calendar);

        Intent intent = getIntent();
        String sourceActivity = intent.getStringExtra("source_activity");
        year = intent.getIntExtra("year", 2000);
        month = intent.getIntExtra("month", 1);
        //change it so it sends the day that was long pressed too
        //TODO: query database with days in the week corresponding to the parameters from the previous intent

        ActionBar ab = getSupportActionBar();
        if(ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }

        btn1=(Button)findViewById(R.id.button91);
        btn2=(Button)findViewById(R.id.button92);
        btn3=(Button)findViewById(R.id.button93);

        btn1.setOnClickListener(this::onClick);
        btn2.setOnClickListener(this::onClick);
        btn3.setOnClickListener(this::onClick);
    }

    public void onClick(View view) {
        /*Intent i;
        switch(view.getId()) {
            case R.id.button91:
                i = new Intent(this, MonthCalendarActivity.class);
                i.putExtra("source_activity", "activity_week_calendar");
                i.putExtra("year", year);
                i.putExtra("month", month);
                startActivity(i);
                break;
            case R.id.button92:
                i = new Intent(this, PastDayActivity.class);
                i.putExtra("source_activity", "activity_week_calendar");
                i.putExtra("year", year);
                i.putExtra("month", month);
                i.putExtra("day", day);
                startActivity(i);
                break;
            case R.id.button93:
                i = new Intent(this, ModifyDayActivity.class);
                i.putExtra("source_activity", "activity_week_calendar");
                i.putExtra("year", year);
                i.putExtra("month", month);
                i.putExtra("day", day);
                startActivity(i);
                break;
        }*/
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                Intent i = new Intent(this, MonthCalendarActivity.class);
                i.putExtra("source_activity", "activity_week_calendar");
                i.putExtra("year", year);
                i.putExtra("month", month);
                startActivity(i);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(this, MonthCalendarActivity.class);
        i.putExtra("source_activity", "activity_week_calendar");
        i.putExtra("year", year);
        i.putExtra("month", month);
        startActivity(i);
    }
}