package com.example.jonathan_gorovoy_android;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.example.jonathan_gorovoy_android.adapters.DeadlineViewAdapter;
import com.example.jonathan_gorovoy_android.classes.DeadlineView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

public class ViewDeadlinesActivity extends AppCompatActivity {

    int eventIndex;
    boolean isSpecificDay;

    ListView deadlineList;
    ArrayList<DeadlineView> deadlineArray = new ArrayList<DeadlineView>();
    Dal dal;

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
        //getDeadlinesDemo();
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
                startActivity(i);
            }
        };
        deadlineList.setOnItemClickListener(deadlineListListener);

    }

    /*private void getDeadlinesDemo() {
        DeadlineView dv = new DeadlineView("Safrut work", "17/12/2020");
        deadlineArray.add(dv);
        dv = new DeadlineView("Math test", "29/12/2020");
        deadlineArray.add(dv);
        dv = new DeadlineView("Physics lab", "04/01/2021");
        deadlineArray.add(dv);
        dv = new DeadlineView("Learn for ezrahut test", "14/01/2021");
        deadlineArray.add(dv);
        dv = new DeadlineView("Birthday", "20/02/2021");
        deadlineArray.add(dv);
        dv = new DeadlineView("Math bagrut", "19/04/2021");
        deadlineArray.add(dv);
    }*/

    public void onClick(View view) {
        /*Intent i;
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
        }*/
    }

    boolean determineIsInPast(int day, int month, int year)
    {
        Calendar today = Calendar.getInstance();
        int todayYear = today.get(Calendar.YEAR), todayMonth = today.get(Calendar.MONTH)+1, todayDay = today.get(Calendar.DAY_OF_MONTH);
        return year<todayYear || (year==todayYear && month<todayMonth) || (year==todayYear && month==todayMonth && day<todayDay);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                Intent i = new Intent(this, MainActivity.class);
                i.putExtra("source_activity", "activity_view_deadlines");
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
        startActivity(i);
    }
}