package com.example.jonathan_gorovoy_android;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.EventLog;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.jonathan_gorovoy_android.adapters.EventDayViewAdapter;
import com.example.jonathan_gorovoy_android.classes.DeadlineView;
import com.example.jonathan_gorovoy_android.classes.EventDayView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class ModifyDayActivity extends AppCompatActivity {

    Button btn1, btn2;
    int year, month;
    int day, eventIndex;
    int routineIndex;
    int receivedRoutineIndex;
    boolean isSpecificDay;
    boolean inPast = false;
    String sourceActivity;
    Dal dal;

    ListView eventList;
    ArrayList<EventDayView> eventArray = new ArrayList<EventDayView>();
    TextView dateText;
    ArrayList<DeadlineView> deadlineArray = new ArrayList<DeadlineView>();
    ArrayList<String> routineArray = new ArrayList<>();

    int btnChoice=0;//0=add event, 1=move deadline, 2=apply routine

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_day);

        dateText = (TextView)findViewById(R.id.date);
        String dateString = "";

        dal = new Dal(ModifyDayActivity.this);

        eventIndex=0;

        Intent intent = getIntent();
        sourceActivity = intent.getStringExtra("source_activity");
        receivedRoutineIndex = intent.getIntExtra("routineIndex", 0);
        if(sourceActivity.equals("activity_month_calendar") || sourceActivity.equals("activity_week_calendar") || (sourceActivity.equals("activity_modify_event") && receivedRoutineIndex == 0))
        {
            year = intent.getIntExtra("year", 2000);
            month = intent.getIntExtra("month", 1);
            day = intent.getIntExtra("day", 1);
            routineIndex = 0;
            dateString = day + "/" + month + "/" + year;
            Calendar today = Calendar.getInstance();
            int todayYear = today.get(Calendar.YEAR), todayMonth = today.get(Calendar.MONTH)+1, todayDay = today.get(Calendar.DAY_OF_MONTH);
            if(year<todayYear || (year==todayYear && month<todayMonth) || (year==todayYear && month==todayMonth && day<todayDay))
            {//check if this day is in past provided its not a routine from the if statement it is nested in
                inPast=true;
            }
        }
        else if(sourceActivity.equals("activity_view_routines") || (sourceActivity.equals("activity_modify_event") && receivedRoutineIndex != 0))
        {
            year=0;
            month=0;
            day=0;
            routineIndex = intent.getIntExtra("routineIndex", 0);
            dateString = dal.getRoutineName(routineIndex);
        }
        isSpecificDay = intent.getBooleanExtra("isSpecificDay", routineIndex == 0);
        dateText.setText(dateString);

        ActionBar ab = getSupportActionBar();
        if(ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }

        btn1=(Button)findViewById(R.id.btnAdd);

        eventList = (ListView)findViewById(R.id.eventList);
        //getEventsDemo();
        eventArray = dal.getEventsInDay(day, month, year, routineIndex, inPast);
        EventDayViewAdapter edva = new EventDayViewAdapter(this, R.layout.event_day_view, eventArray);
        eventList.setAdapter(edva);
        AdapterView.OnItemClickListener eventListListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EventDayView item = eventArray.get(position);
                Intent i = new Intent(ModifyDayActivity.this, ModifyEventActivity.class);
                i.putExtra("source_activity", "activity_modify_day");
                i.putExtra("year", year);
                i.putExtra("month", month);
                i.putExtra("day", day);
                i.putExtra("isSpecificDay", isSpecificDay);
                i.putExtra("routineIndex", routineIndex);
                i.putExtra("title", item.getTitle());
                i.putExtra("description",item.getDescription());
                i.putExtra("startHour",item.getStartHour());
                i.putExtra("endHour",item.getEndHour());
                i.putExtra("eventIndex",item.getEventIndex());
                i.putExtra("inPast", inPast);
                i.putExtra("isDeadline", item.getIsDeadline());
                startActivity(i);
            }
        };
        eventList.setOnItemClickListener(eventListListener);

        btn1.setOnClickListener(this::onClick);
    }

    public void getEventsDemo()
    {
        int demoYear = 2019;
        int demoMonth = 8;
        int demoDay = 25;
        EventDayView ev = new EventDayView("15:00", "16:30", "Walk dog out", "Take dog out for a walk around the park",0 , false);
        ev.setInPast(demoYear, demoMonth, demoDay, true, GregorianCalendar.getInstance().getTime());
        eventArray.add(ev);
        ev = new EventDayView("16:30", "17:00", "Lunch", "Grab lunch from the fridge", 0, false);
        ev.setInPast(2021, demoMonth, demoDay, true, GregorianCalendar.getInstance().getTime());
        eventArray.add(ev);
        ev = new EventDayView("17:20", "18:00", "Watch Lesson", "Watch the lesson recorded by math teacher in preparation for the test", 0, false);
        ev.setInPastValue(true);
        eventArray.add(ev);
        ev = new EventDayView("18:00", "19:30", "Code Android project", "Add new adapter view to android project xml file", 0, false);
        eventArray.add(ev);
    }

    public void onClick(View view) {
        btnChoice = 0;
        switch(view.getId()) {
            case R.id.btnAdd:
                if(!inPast) {
                    if (routineIndex == 0) {
                        String[] options = {"Add Event Here", "Move Deadline Here", "Apply Routine Here"};
                        ListAdapter optionListAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, options);
                        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
                        dialogBuilder.setTitle("Choose Action:");
                        dialogBuilder.setAdapter(optionListAdapter,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int item) {
                                        switch (item) { // by index in options array
                                            case 0: // Add Event
                                                addEventAction();
                                                break;
                                            case 1: // Move Deadline
                                                moveDeadlineAction();
                                                break;
                                            case 2: // Apply Routine
                                                applyRoutineAction();
                                                break;
                                            default:
                                                break;
                                        }
                                    }
                                });
                        AlertDialog dialog = dialogBuilder.create();
                        dialog.show();
                    } else {// if it is a routine the plus button should just add an event
                        addEventAction();
                    }
                }
                break;
        }
    }

    void addEventAction() {
        Intent i;
        i = new Intent(this, ModifyEventActivity.class);
        i.putExtra("source_activity", "activity_modify_day");
        i.putExtra("year", year);
        i.putExtra("month", month);
        i.putExtra("day", day);
        i.putExtra("eventIndex", eventIndex);
        i.putExtra("isSpecificDay", isSpecificDay);
        i.putExtra("routineIndex", routineIndex);
        i.putExtra("title", "");
        i.putExtra("description", "");
        i.putExtra("startHour", "");
        i.putExtra("endHour", "");
        i.putExtra("isDeadline", false);
        i.putExtra("inPast", false);
        startActivity(i);
    }

    void moveDeadlineAction() {
        ArrayList<String> deadlines = new ArrayList<>();
        ArrayList<DeadlineView> futureDeadlineArray = new ArrayList<>();
        deadlineArray = dal.getAllDeadlines();
        Calendar deadlineCalendar = Calendar.getInstance(), today = Calendar.getInstance();
        for(int i=0;i<deadlineArray.size();i++)
        {
            DeadlineView dv = deadlineArray.get(i);
            deadlineCalendar.set(Calendar.DAY_OF_MONTH, dv.getDay());
            deadlineCalendar.set(Calendar.MONTH, dv.getMonth()-1);
            deadlineCalendar.set(Calendar.YEAR, dv.getYear());
            if(today.before(deadlineCalendar)) {
                deadlines.add(dv.getText());
                futureDeadlineArray.add(dv);
            }
        }
        ListAdapter optionListAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, deadlines);
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle("Choose Deadline to move here:");
        dialogBuilder.setAdapter(optionListAdapter,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        DeadlineView selectedDv = futureDeadlineArray.get(item);
                        dal.addEvent(day, month, year, selectedDv.getStartHour(), selectedDv.getEndHour(), selectedDv.getText(), selectedDv.getDescription(), 0, selectedDv.getEventIndex(), true);
                        //update reminders
                        ArrayList<String> reminderTexts = dal.getReminders(selectedDv.getEventIndex());
                        for (int i = 0; i < reminderTexts.size(); i++) {
                            String reminderText = reminderTexts.get(i);
                            Integer amountChosen = Integer.valueOf(reminderText.substring(0, reminderText.indexOf(" ")));
                            String unitChosen = reminderText.substring(reminderText.indexOf(" ") + 1);
                            ModifyEventActivity.scheduleNotification(getApplicationContext(), dal, selectedDv.getEventIndex(), amountChosen, unitChosen, selectedDv.getText(), selectedDv.getDescription(), day, month, year, routineIndex, isSpecificDay, selectedDv.getStartHour());
                        }
                        //refresh
                        eventArray = dal.getEventsInDay(day, month, year, routineIndex, inPast);
                        EventDayViewAdapter edva = new EventDayViewAdapter(ModifyDayActivity.this, R.layout.event_day_view, eventArray);
                        eventList.setAdapter(edva);
                    }
                });
        AlertDialog dialog = dialogBuilder.create();
        dialog.show();
    }

    void applyRoutineAction() {
        routineArray = dal.getRoutines();
        ListAdapter optionListAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, routineArray);
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle("Choose Routine to Apply:");
        dialogBuilder.setAdapter(optionListAdapter,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        int selectedRoutineIndex = dal.getRoutineIndex(routineArray.get(item));
                        new RoutineApplier(0, 0, 0, selectedRoutineIndex, false).execute();
                    }
                });
        AlertDialog dialog = dialogBuilder.create();
        dialog.show();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                if(sourceActivity.equals("activity_month_calendar") || sourceActivity.equals("activity_week_calendar") || (sourceActivity.equals("activity_modify_event") && receivedRoutineIndex == 0)) {
                    Intent i = new Intent(this, MonthCalendarActivity.class);
                    i.putExtra("source_activity", "activity_modify_day");
                    i.putExtra("year", year);
                    i.putExtra("month", month);
                    startActivity(i);
                    return true;
                }
                else if (sourceActivity.equals("activity_view_routines") || (sourceActivity.equals("activity_modify_event") && receivedRoutineIndex != 0))
                {
                    Intent i = new Intent(this, ViewRoutinesActivity.class);
                    i.putExtra("source_activity", "activity_modify_day");
                    startActivity(i);
                }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(sourceActivity.equals("activity_month_calendar") || sourceActivity.equals("activity_week_calendar") || (sourceActivity.equals("activity_modify_event") && receivedRoutineIndex == 0)) {
            Intent i = new Intent(this, MonthCalendarActivity.class);
            i.putExtra("source_activity", "activity_modify_day");
            i.putExtra("year", year);
            i.putExtra("month", month);
            startActivity(i);
        }
        else if (sourceActivity.equals("activity_view_routines") || (sourceActivity.equals("activity_modify_event") && receivedRoutineIndex != 0))
        {
            Intent i = new Intent(this, ViewRoutinesActivity.class);
            i.putExtra("source_activity", "activity_modify_day");
            startActivity(i);
        }
    }

    private class RoutineApplier extends AsyncTask<Void, Void, Void> {

        int rDay;
        int rMonth;
        int rYear;
        int rRoutineIndex;
        boolean rIsSpecificDay;

        RoutineApplier(int day, int month, int year, int routineIndex, boolean isSpecificDay)
        {
            this.rDay = day;
            this.rMonth = month;
            this.rYear = year;
            this.rRoutineIndex = routineIndex;
            this.rIsSpecificDay = isSpecificDay;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            ArrayList<EventDayView> routineEvents = dal.getEventsInDay(0, 0, 0, rRoutineIndex, false);
            for(int i=0;i<routineEvents.size();i++)
            { // doesn't copy reminders, routines cant have reminders in them since reminders i coded in a way that they are instantly scheduled when created
                EventDayView routineEvent = routineEvents.get(i);
                dal.addEvent(day, month, year, routineEvent.getStartHour(), routineEvent.getEndHour(), routineEvent.getTitle(), routineEvent.getDescription(), 0, 0, routineEvent.getIsDeadline());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result)
        {
            //refresh event list view
            eventArray = dal.getEventsInDay(day, month, year, routineIndex, inPast);
            EventDayViewAdapter eventDayViewAdapter = new EventDayViewAdapter(ModifyDayActivity.this, R.layout.event_day_view, eventArray);
            eventList.setAdapter(eventDayViewAdapter);
        }
    }
}

