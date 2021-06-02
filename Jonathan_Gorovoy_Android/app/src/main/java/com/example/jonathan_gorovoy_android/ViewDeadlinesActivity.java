package com.example.jonathan_gorovoy_android;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.jonathan_gorovoy_android.adapters.DeadlineViewAdapter;
import com.example.jonathan_gorovoy_android.classes.DeadlineView;

import java.util.ArrayList;
import java.util.Calendar;

public class ViewDeadlinesActivity extends AppCompatActivity {

    ListView deadlineList;
    ArrayList<DeadlineView> deadlineArray = new ArrayList<>();
    Dal dal;

    @SuppressWarnings("Convert2Lambda")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_deadlines);

        ActionBar ab = getSupportActionBar();
        if(ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }

        dal = new Dal(ViewDeadlinesActivity.this);

        deadlineList = (ListView)findViewById(R.id.deadlineList);
        deadlineArray = dal.getAllDeadlines();
        DeadlineViewAdapter dva = new DeadlineViewAdapter(this, R.layout.deadline_view, deadlineArray);
        deadlineList.setAdapter(dva);
        AdapterView.OnItemClickListener deadlineListListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DeadlineView item = deadlineArray.get(position);
                Intent i = new Intent(ViewDeadlinesActivity.this, ModifyEventActivity.class);
                i.putExtra("source_activity", "activity_view_deadlines");
                i.putExtra("year", item.getYear());
                i.putExtra("month", item.getMonth());
                i.putExtra("day", item.getDay());
                i.putExtra("isSpecificDay", true); // the only ones shown here are ones that are on specific days
                i.putExtra("routineIndex", 0);
                i.putExtra("title", item.getText());
                i.putExtra("description", item.getDescription());
                i.putExtra("startHour", item.getStartHour());
                i.putExtra("endHour", item.getEndHour());
                i.putExtra("eventIndex", item.getEventIndex());
                i.putExtra("inPast", determineIsInPast(item.getDay(), item.getMonth(), item.getYear()));
                i.putExtra("isDeadline", true);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
            }
        };
        deadlineList.setOnItemClickListener(deadlineListListener);

    }

    boolean determineIsInPast(int day, int month, int year)
    {
        Calendar today = Calendar.getInstance();
        int todayYear = today.get(Calendar.YEAR), todayMonth = today.get(Calendar.MONTH)+1, todayDay = today.get(Calendar.DAY_OF_MONTH);
        return year<todayYear || (year==todayYear && month<todayMonth) || (year==todayYear && month==todayMonth && day<todayDay);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Respond to the action bar's Up/Home button
        if (item.getItemId() == android.R.id.home) {
            Intent i = new Intent(this, MainActivity.class);
            i.putExtra("source_activity", "activity_view_deadlines");
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(this, MainActivity.class);
        i.putExtra("source_activity", "activity_view_deadlines");
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }
}