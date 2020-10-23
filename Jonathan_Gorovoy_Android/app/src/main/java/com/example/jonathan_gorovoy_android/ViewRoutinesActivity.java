package com.example.jonathan_gorovoy_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ViewRoutinesActivity extends AppCompatActivity {

    Button btn1, btn2;
    int routineIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_routines);

        btn1=(Button)findViewById(R.id.button81);
        btn2=(Button)findViewById(R.id.button82);

        btn1.setOnClickListener(this::onClick);
        btn2.setOnClickListener(this::onClick);
    }

    public void onClick(View view) {
        Intent i;
        switch(view.getId()) {
            case R.id.button81:
                i = new Intent(this, MainActivity.class);
                i.putExtra("source_activity", "activity_view_routines");
                startActivity(i);
                break;
            case R.id.button82:
                i = new Intent(this, ModifyRoutineActivity.class);
                i.putExtra("source_activity", "activity_view_routines");
                i.putExtra("eventIndex", routineIndex);
                startActivity(i);
                break;
        }
    }
}