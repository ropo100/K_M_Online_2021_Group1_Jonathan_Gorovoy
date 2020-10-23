package com.example.jonathan_gorovoy_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ModifyEventActivity extends AppCompatActivity {

    Button btn1, btn2;
    int year, month, rowInMonth, day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_event);

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
                i = new Intent(this, ViewRoutinesActivity.class);
                i.putExtra("source_activity", "activity_modify_event");
                startActivity(i);
                break;
            case R.id.button22:
                i = new Intent(this, ModifyDayActivity.class);
                i.putExtra("source_activity", "activity_modify_event");
                i.putExtra("year", year);
                i.putExtra("month", month);
                i.putExtra("rowInMonth", rowInMonth);
                i.putExtra("day", day);
                startActivity(i);
                break;
        }
    }
}