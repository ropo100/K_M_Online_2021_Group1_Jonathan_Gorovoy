package com.example.jonathan_gorovoy_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ViewDeadlinesActivity extends AppCompatActivity {

    Button btn1, btn2;
    int eventIndex, isSpecificDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_deadlines);

        btn1=(Button)findViewById(R.id.button71);
        btn2=(Button)findViewById(R.id.button72);

        btn1.setOnClickListener(this::onClick);
        btn2.setOnClickListener(this::onClick);
    }

    public void onClick(View view) {
        Intent i;
        switch(view.getId()) {
            case R.id.button71:
                i = new Intent(this, MainActivity.class);
                i.putExtra("source_activity", "activity_view_deadlines");
                startActivity(i);
                break;
            case R.id.button72:
                i = new Intent(this, ModifyEventActivity.class);
                i.putExtra("source_activity", "activity_view_deadlines");
                i.putExtra("eventIndex", eventIndex);
                i.putExtra("isSpecificDay", isSpecificDay);
                startActivity(i);
                break;
        }
    }
}