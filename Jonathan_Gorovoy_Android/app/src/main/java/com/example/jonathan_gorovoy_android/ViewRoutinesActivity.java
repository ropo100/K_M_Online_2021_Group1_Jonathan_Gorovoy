package com.example.jonathan_gorovoy_android;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class ViewRoutinesActivity extends AppCompatActivity {

    Button btn1, btn2;
    int routineIndex;

    ListView routineList;
    ArrayList<String> routineArray = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_routines);

        ActionBar ab = getSupportActionBar();
        if(ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }

        btn1=(Button)findViewById(R.id.button81);
        btn2=(Button)findViewById(R.id.button82);

        routineList = (ListView)findViewById(R.id.routineList);
        getRoutinesDemo();
        ArrayAdapter<String> sa = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, routineArray);
        routineList.setAdapter(sa);

        btn1.setOnClickListener(this::onClick);
        btn2.setOnClickListener(this::onClick);
    }

    private void getRoutinesDemo() {
        String str = "Ordinary Monday";
        routineArray.add(str);
        str = "School Sunday";
        routineArray.add(str);
        str = "Free Day Saturday";
        routineArray.add(str);
        str = "Ordinary Thursday";
        routineArray.add(str);
        str = "Homework Day";
        routineArray.add(str);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                Intent i = new Intent(this, MainActivity.class);
                i.putExtra("source_activity", "activity_view_routines");
                startActivity(i);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}