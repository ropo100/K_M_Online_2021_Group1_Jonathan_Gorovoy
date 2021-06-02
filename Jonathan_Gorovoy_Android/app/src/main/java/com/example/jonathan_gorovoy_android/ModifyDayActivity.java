package com.example.jonathan_gorovoy_android;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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

@SuppressWarnings("Convert2Lambda")
public class ModifyDayActivity extends AppCompatActivity {

    Button btnAdd;
    int year, month;
    int day, eventIndex;
    int routineIndex;
    int receivedRoutineIndex;
    boolean isSpecificDay;
    boolean inPast = false;
    String sourceActivity;
    Dal dal;

    ListView eventList;
    ArrayList<EventDayView> eventArray = new ArrayList<>();
    TextView dateText;
    ArrayList<DeadlineView> deadlineArray = new ArrayList<>();
    ArrayList<String> routineArray = new ArrayList<>();

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

        btnAdd=(Button)findViewById(R.id.btnAdd);

        eventList = (ListView)findViewById(R.id.eventList);
        eventArray = dal.getEventsInDay(day, month, year, routineIndex, inPast);
        EventDayViewAdapter eventDayViewAdapter = new EventDayViewAdapter(this, R.layout.event_day_view, eventArray);
        eventList.setAdapter(eventDayViewAdapter);
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
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
            }
        };
        eventList.setOnItemClickListener(eventListListener);

        btnAdd.setOnClickListener(this::onClick);
    }

    public void onClick(View view) {
        if (view.getId() == R.id.btnAdd) {
            if (!inPast) {
                if (routineIndex == 0) {
                    String[] options = {"Add Event Here", "Move Deadline Here", "Apply Routine Here"};
                    ListAdapter optionListAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, options);
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
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
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
        ListAdapter optionListAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, deadlines);
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
                        EventDayViewAdapter eventDayViewAdapter = new EventDayViewAdapter(ModifyDayActivity.this, R.layout.event_day_view, eventArray);
                        eventList.setAdapter(eventDayViewAdapter);
                    }
                });
        AlertDialog dialog = dialogBuilder.create();
        dialog.show();
    }

    void applyRoutineAction() {
        routineArray = dal.getRoutines();
        ListAdapter optionListAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, routineArray);
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle("Choose Routine to Apply:");
        dialogBuilder.setAdapter(optionListAdapter,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        int selectedRoutineIndex = dal.getRoutineIndex(routineArray.get(item));
                        new RoutineApplier(selectedRoutineIndex).execute();
                    }
                });
        AlertDialog dialog = dialogBuilder.create();
        dialog.show();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Respond to the action bar's Up/Home button
        if (item.getItemId() == android.R.id.home) {
            if (sourceActivity.equals("activity_month_calendar") || sourceActivity.equals("activity_week_calendar") || (sourceActivity.equals("activity_modify_event") && receivedRoutineIndex == 0)) {
                Intent i = new Intent(this, MonthCalendarActivity.class);
                i.putExtra("source_activity", "activity_modify_day");
                i.putExtra("year", year);
                i.putExtra("month", month);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                return true;
            } else if (sourceActivity.equals("activity_view_routines") || (sourceActivity.equals("activity_modify_event") && receivedRoutineIndex != 0)) {
                Intent i = new Intent(this, ViewRoutinesActivity.class);
                i.putExtra("source_activity", "activity_modify_day");
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
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
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        }
        else if (sourceActivity.equals("activity_view_routines") || (sourceActivity.equals("activity_modify_event") && receivedRoutineIndex != 0))
        {
            Intent i = new Intent(this, ViewRoutinesActivity.class);
            i.putExtra("source_activity", "activity_modify_day");
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        }
    }

    private class RoutineApplier extends AsyncTask<Void, Void, Void> {

        final int rRoutineIndex;

        RoutineApplier(int routineIndex)
        {
            this.rRoutineIndex = routineIndex;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            ArrayList<EventDayView> routineEvents = dal.getEventsInDay(0, 0, 0, rRoutineIndex, false);
            for(int i=0;i<routineEvents.size();i++)
            { // doesn't copy reminders, routines cant have reminders in them since reminders I coded in a way that they are instantly scheduled when created
                EventDayView routineEvent = routineEvents.get(i);
                dal.addEvent(day, month, year, routineEvent.getStartHour(), routineEvent.getEndHour(), routineEvent.getTitle(), routineEvent.getDescription(), 0, 0, routineEvent.getIsDeadline());
                try {
                    Thread.sleep(50);
                }
                catch (InterruptedException ignored){
                }
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

