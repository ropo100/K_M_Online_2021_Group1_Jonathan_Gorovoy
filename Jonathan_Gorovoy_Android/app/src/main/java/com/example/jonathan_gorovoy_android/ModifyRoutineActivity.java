package com.example.jonathan_gorovoy_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ModifyRoutineActivity extends AppCompatActivity {

    Button btn1;
    Button btn2;
    int eventIndex, isSpecificDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_routine);

        btn1=(Button)findViewById(R.id.button31);
        btn2=(Button)findViewById(R.id.button32);

        btn1.setOnClickListener(this::onClick);
        btn2.setOnClickListener(this::onClick);
    }

    public void onClick(View view) {
        Intent i;
        switch(view.getId()) {
            case R.id.button31:
                i = new Intent(this, ModifyEventActivity.class);
                i.putExtra("source_activity", "activity_modify_routine");
                i.putExtra("eventIndex", eventIndex);
                i.putExtra("isSpecificDay", isSpecificDay);
                startActivity(i);
                break;
            case R.id.button32:
                i = new Intent(this, ViewRoutinesActivity.class);
                i.putExtra("source_activity", "activity_modify_routine");
                startActivity(i);
                break;
        }
    }
}