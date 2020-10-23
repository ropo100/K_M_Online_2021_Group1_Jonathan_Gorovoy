package com.example.jonathan_gorovoy_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MonthCalendarActivity extends AppCompatActivity {

    Button btn1, btn2;
    int year, month, rowInMonth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_month_calendar);

        btn1=(Button)findViewById(R.id.button41);
        btn2=(Button)findViewById(R.id.button42);

        btn1.setOnClickListener(this::onClick);
        btn2.setOnClickListener(this::onClick);
    }

    public void onClick(View view) {
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
                i.putExtra("rowInMonth", rowInMonth);
                startActivity(i);
                break;
        }
    }
}