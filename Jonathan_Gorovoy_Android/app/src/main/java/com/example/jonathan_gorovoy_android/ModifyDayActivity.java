package com.example.jonathan_gorovoy_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ModifyDayActivity extends AppCompatActivity {

    Button btn1, btn2;
    int year, month, rowInMonth;
    int day, eventIndex;
    boolean isSpecificDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_day);

        btn1=(Button)findViewById(R.id.button11);
        btn2=(Button)findViewById(R.id.button12);

        btn1.setOnClickListener(this::onClick);
        btn2.setOnClickListener(this::onClick);
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