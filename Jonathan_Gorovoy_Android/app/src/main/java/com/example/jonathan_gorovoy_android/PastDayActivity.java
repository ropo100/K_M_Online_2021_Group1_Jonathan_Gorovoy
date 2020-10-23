package com.example.jonathan_gorovoy_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class PastDayActivity extends AppCompatActivity {

    Button btn1;
    int year, month, rowInMonth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_past_day);

        btn1=(Button)findViewById(R.id.button51);

        btn1.setOnClickListener(this::onClick);
    }

    public void onClick(View view) {
        Intent i;
        switch(view.getId()) {
            case R.id.button51:
                i = new Intent(this, WeekCalendarActivity.class);
                i.putExtra("source_activity", "activity_past_day");
                i.putExtra("year", year);
                i.putExtra("month", month);
                i.putExtra("rowInMonth", rowInMonth);
                startActivity(i);
                break;
        }
    }
}