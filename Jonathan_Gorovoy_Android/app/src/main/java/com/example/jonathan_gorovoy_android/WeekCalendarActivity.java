package com.example.jonathan_gorovoy_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class WeekCalendarActivity extends AppCompatActivity {

    Button btn1, btn2, btn3;
    int year, month, day, rowInMonth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_week_calendar);

        btn1=(Button)findViewById(R.id.button91);
        btn2=(Button)findViewById(R.id.button92);
        btn3=(Button)findViewById(R.id.button93);

        btn1.setOnClickListener(this::onClick);
        btn2.setOnClickListener(this::onClick);
        btn3.setOnClickListener(this::onClick);
    }

    public void onClick(View view) {
        Intent i;
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
                i.putExtra("rowInMonth", rowInMonth);
                startActivity(i);
                break;
            case R.id.button93:
                i = new Intent(this, ModifyDayActivity.class);
                i.putExtra("source_activity", "activity_week_calendar");
                i.putExtra("year", year);
                i.putExtra("month", month);
                i.putExtra("day", day);
                i.putExtra("rowInMonth", rowInMonth);
                startActivity(i);
                break;
        }
    }
}