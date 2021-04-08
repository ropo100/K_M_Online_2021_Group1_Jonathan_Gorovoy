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

public class ViewDeadlinesActivity extends AppCompatActivity {

    Button btn1, btn2;
    int eventIndex;
    boolean isSpecificDay;

    ListView deadlineList;
    ArrayList<DeadlineView> deadlineArray = new ArrayList<DeadlineView>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_deadlines);

        ActionBar ab = getSupportActionBar();
        if(ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }

        btn1=(Button)findViewById(R.id.button71);
        btn2=(Button)findViewById(R.id.button72);

        deadlineList = (ListView)findViewById(R.id.deadlineList);
        getDeadlinesDemo();
        DeadlineViewAdapter dva = new DeadlineViewAdapter(this, R.layout.deadline_view, deadlineArray);
        deadlineList.setAdapter(dva);
        AdapterView.OnItemClickListener deadlineListListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DeadlineView item = deadlineArray.get(position);
                //TODO: query database to find event index according to DeadlineView item members/methods
                Intent i = new Intent(ViewDeadlinesActivity.this, ModifyEventActivity.class);
                i.putExtra("source_activity", "activity_view_deadlines");
                i.putExtra("eventIndex", eventIndex);
                i.putExtra("isSpecificDay", false); //deadlines are until specific dates, but going to this activity isn't from a specific day to return to, thus only the due date matters and can be changed and the deadline isn't tied to a day
                startActivity(i);
            }
        };
        deadlineList.setOnItemClickListener(deadlineListListener);

        btn1.setOnClickListener(this::onClick);
        btn2.setOnClickListener(this::onClick);
    }

    private void getDeadlinesDemo() {
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
    }

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
}