package com.example.jonathan_gorovoy_android;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Range;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.example.jonathan_gorovoy_android.classes.EventDayView;
import com.example.jonathan_gorovoy_android.classes.ReminderCreator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Dictionary;
import java.util.TimeZone;
import java.util.stream.IntStream;

public class ModifyEventActivity extends AppCompatActivity {

    Button btn1, btn2, btn3;
    int year=0, month=0, day=0, eventIndex=0, routineIndex=0;
    boolean isSpecificDay=true, isDeadline=false, inPast = false;
    String title="", description="", startHour="", endHour="";
    String sourceActivity;
    Dal dal;

    CheckBox deadlineCheckbox;
    EditText editTitle, editDescription, editStartHour, editEndHour;
    InputFilter filterHour; // filter for 00:00-23:59 input
    boolean doneOnce = false;

    ArrayList<String> reminderArray = new ArrayList<String>();
    Integer[] amountRange = {0, 1, 2, 3, 4, 5, 10, 15, 20, 25, 30, 40, 45, 50, 55, 60, 90};
    String[] units = {"minutes", "hours", "days", "weeks"};
    Integer amountChosen = 1;
    String unitChosen = "minutes";
    int reminderId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_event);

        dal = new Dal(ModifyEventActivity.this);

        editTitle = (EditText)findViewById(R.id.editTitle);
        editDescription = (EditText)findViewById(R.id.editDescription);
        editStartHour = (EditText)findViewById(R.id.eventStartTime);
        editEndHour = (EditText)findViewById(R.id.eventEndTime);
        deadlineCheckbox = (CheckBox)findViewById(R.id.isDeadlineCheckbox);

        Intent intent = getIntent();
        sourceActivity = intent.getStringExtra("source_activity");
        isSpecificDay = intent.getBooleanExtra("isSpecificDay", false);
        eventIndex = intent.getIntExtra("eventIndex", 0);
        routineIndex = intent.getIntExtra("routineIndex", 0);
        isDeadline = intent.getBooleanExtra("isDeadline", false);
        inPast = intent.getBooleanExtra("inPast", false);
        deadlineCheckbox.setChecked(isDeadline);
        if (isSpecificDay) {
            year = intent.getIntExtra("year", 2000);
            month = intent.getIntExtra("month", 1);
            day = intent.getIntExtra("day", 1);
        }
        if(eventIndex != 0) {
            title = intent.getStringExtra("title");
            description = intent.getStringExtra("description");
            startHour = intent.getStringExtra("startHour");
            endHour = intent.getStringExtra("endHour");
            editTitle.setText(title);
            editDescription.setText(description);
            editStartHour.setText(startHour);
            editEndHour.setText(endHour);
        }

        ActionBar ab = getSupportActionBar();
        if(ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }

        btn1=(Button)findViewById(R.id.btnApply);
        btn2=(Button)findViewById(R.id.btnDelete);
        btn3=(Button)findViewById(R.id.btnShowReminders);

        btn1.setOnClickListener(this::onClick);
        btn2.setOnClickListener(this::onClick);
        btn3.setOnClickListener(this::onClick);

        if(inPast)
        {
            editTitle.setEnabled(false);
            editDescription.setEnabled(false);
            editStartHour.setEnabled(false);
            editEndHour.setEnabled(false);
            deadlineCheckbox.setEnabled(false);
            btn2.setEnabled(false);
            btn1.setText("OK");
        }

        createTimeFilter();
        editStartHour.setFilters(new InputFilter[]{filterHour});
        createTimeFilter();
        editEndHour.setFilters(new InputFilter[]{filterHour});
    }

    public void onClick(View view) {
        switch(view.getId())
        {
            case R.id.btnApply:
                if(!inPast) {
                    startHour = editStartHour.getText().toString();
                    endHour = editEndHour.getText().toString();
                    title = editTitle.getText().toString();
                    description = editDescription.getText().toString();
                    isDeadline = deadlineCheckbox.isChecked();
                    dal.addEvent(day, month, year, startHour, endHour, title, description, routineIndex, eventIndex, isDeadline);
                    if(eventIndex != 0) { // update all the notifications if this is not a new event
                        //for the purpose of accommodating for changes to title or start hour
                        ArrayList<String> reminderTexts = dal.getReminders(eventIndex);
                        for (int i = 0; i < reminderTexts.size(); i++) {
                            String reminderText = reminderTexts.get(i);
                            amountChosen = Integer.valueOf(reminderText.substring(0, reminderText.indexOf(" ")));
                            unitChosen = reminderText.substring(reminderText.indexOf(" ") + 1);
                            scheduleNotification();
                        }

                    }
                }
                finishAndReturn();
                break;
            case R.id.btnDelete:
                ArrayList<String> reminderTexts = dal.getReminders(eventIndex);
                for(int i=0;i<reminderTexts.size();i++) {
                    String reminderText = reminderTexts.get(i);
                    amountChosen = Integer.valueOf(reminderText.substring(0, reminderText.indexOf(" ")));
                    unitChosen = reminderText.substring(reminderText.indexOf(" ") + 1);
                    reminderId = dal.getReminderIndex(eventIndex, amountChosen, unitChosen);
                    deleteNotification();
                    dal.deleteReminder(reminderId);
                }
                dal.deleteEvent(eventIndex);
                finishAndReturn();
                break;
            case R.id.btnShowReminders:
                viewReminders();
                break;
        }
    }

    void viewReminders() {
        //cant add reminders to an event before it exists
        if(eventIndex != 0) {
            reminderArray = dal.getReminders(eventIndex);
            ListAdapter reminderAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, reminderArray);
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
            dialogBuilder.setTitle("Current Reminders:");
            dialogBuilder.setAdapter(reminderAdapter,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int item) {
                            if (!inPast) {
                                String reminderText = reminderArray.get(item);
                                amountChosen = Integer.valueOf(reminderText.substring(0, reminderText.indexOf(" ")));
                                unitChosen = reminderText.substring(reminderText.indexOf(" ") + 1);
                                reminderId = dal.getReminderIndex(eventIndex, amountChosen, unitChosen);

                                displayReminder();
                            }
                        }
                    });
            if (!inPast) {
                dialogBuilder.setNeutralButton("Add Reminder", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        reminderId = 0;
                        amountChosen = amountRange[1];
                        unitChosen = units[0];

                        displayReminder();
                    }
                });
            }
            dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            AlertDialog dialog = dialogBuilder.create();
            dialog.show();
        }
        else
        {
            Toast.makeText(this, "Cannot add Reminders to an event before creating it", Toast.LENGTH_LONG).show();
        }
    }

    void displayReminder() {
        //inflate dialog for add/update/delete reminder with reminder id
        AlertDialog.Builder reminderBuilder = new AlertDialog.Builder(ModifyEventActivity.this);
        reminderBuilder.setTitle("Reminder:");
        View view = View.inflate(ModifyEventActivity.this, R.layout.reminder_view, null);
        reminderBuilder.setView(view);
        Spinner amountSpinner = (Spinner)view.findViewById(R.id.amountSpinner);
        Spinner unitSpinner = (Spinner)view.findViewById(R.id.unitSpinner);
        AdapterView.OnItemSelectedListener selectedListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        };
        amountSpinner.setOnItemSelectedListener(selectedListener);
        unitSpinner.setOnItemSelectedListener(selectedListener);
        ArrayAdapter amountAdapter = new ArrayAdapter(ModifyEventActivity.this, R.layout.support_simple_spinner_dropdown_item, amountRange);
        amountAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        amountSpinner.setAdapter(amountAdapter);
        ArrayAdapter unitAdapter = new ArrayAdapter(ModifyEventActivity.this, R.layout.support_simple_spinner_dropdown_item, units);
        unitAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        unitSpinner.setAdapter(unitAdapter);
        amountSpinner.setSelection(Arrays.asList(amountRange).indexOf(amountChosen));
        unitSpinner.setSelection(Arrays.asList(units).indexOf(unitChosen));
        reminderBuilder.setPositiveButton("Apply", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                amountChosen = (Integer)amountSpinner.getSelectedItem();
                unitChosen = (String)unitSpinner.getSelectedItem();
                dal.addReminder(reminderId, eventIndex, amountChosen, unitChosen);
                scheduleNotification();
                dialog.cancel();
            }
        });
        reminderBuilder.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(reminderId != 0)
                {
                    deleteNotification();
                    dal.deleteReminder(reminderId);
                }
                dialog.cancel();
            }
        });
        AlertDialog reminderDialog = reminderBuilder.create();
        reminderDialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finishAndReturn();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAndReturn();
    }

    public void finishAndReturn()
    {
        //Move back to the same screen you entered from with the same intent parameters they need to load the same thing
        Intent i;
        if(sourceActivity.equals("activity_modify_day"))
        {
            i = new Intent(this, ModifyDayActivity.class);
            i.putExtra("source_activity", "activity_modify_event");
            i.putExtra("day",day);
            i.putExtra("month",month);
            i.putExtra("year",year);
            i.putExtra("routineIndex",routineIndex);
            i.putExtra("isSpecificDay", isSpecificDay);
            startActivity(i);
        }
        else if(sourceActivity.equals("activity_view_deadlines"))
        {
            i = new Intent(this, ViewDeadlinesActivity.class);
            i.putExtra("source_activity", "activity_modify_event");
            startActivity(i);
        }
        else
        {
            i = new Intent(this, MainActivity.class);
            i.putExtra("source_activity", "activity_modify_event");
            startActivity(i);
        }
    }

    private void scheduleNotification()
    {
        reminderId=dal.getReminderIndex(eventIndex, amountChosen, unitChosen);
        int notificationId = Integer.parseInt(1212 + "" + reminderId);
        Context context = getApplicationContext();

        NotificationCompat.Builder reminderBuilder = new NotificationCompat.Builder(context, context.getString(R.string.notifChannelId))
                .setContentTitle(amountChosen != 0 ? (amountChosen + " " + (amountChosen != 1 ? unitChosen : unitChosen.substring(0, unitChosen.length()-1)) + " until " + title + " starts") : title + " is starting now")
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(description))
                .setAutoCancel(true)
                .setOngoing(false)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setSmallIcon(R.drawable.ic_launcher_foreground);

        Intent destination = new Intent(context, ModifyDayActivity.class);
        destination.putExtra("source_activity", "activity_modify_event");
        destination.putExtra("day", day);
        destination.putExtra("month", month);
        destination.putExtra("year",year);
        destination.putExtra("routineIndex",routineIndex);
        destination.putExtra("isSpecificDay", isSpecificDay);
        destination.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingDestination = PendingIntent.getActivity(context, notificationId, destination, PendingIntent.FLAG_CANCEL_CURRENT);
        reminderBuilder.setContentIntent(pendingDestination);
        Notification reminder = reminderBuilder.build();

        Intent receiverIntent = new Intent(context, ReminderCreator.class);
        receiverIntent.putExtra("notificationVar", reminder);
        receiverIntent.putExtra("notificationId", notificationId);
        PendingIntent scheduledReceive = PendingIntent.getBroadcast(context, notificationId, receiverIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        Calendar eventStartCalendar = Calendar.getInstance();
        Date today = eventStartCalendar.getTime();
        eventStartCalendar.set(Calendar.YEAR, year);
        eventStartCalendar.set(Calendar.MONTH, month-1);
        eventStartCalendar.set(Calendar.DAY_OF_MONTH, day);
        eventStartCalendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(startHour.substring(0, startHour.indexOf(':'))));
        eventStartCalendar.set(Calendar.MINUTE, Integer.parseInt(startHour.substring(startHour.indexOf(':')+1)));
        eventStartCalendar.set(Calendar.SECOND, 0);
        int unitType = 0;
        switch(unitChosen)
        {
            case "minutes":
                unitType = Calendar.MINUTE;
                break;
            case "hours":
                unitType = Calendar.HOUR;
                break;
            case "days":
                unitType = Calendar.DAY_OF_MONTH;
                break;
            case "weeks":
                unitType = Calendar.WEEK_OF_MONTH;
                break;
        }
        eventStartCalendar.add(unitType, -amountChosen);

        Date whenToNotify = eventStartCalendar.getTime(); //should calculate again after subtracting the amount
        if(today.before(whenToNotify)) { //only notify if the notification time will be in the future and not in the past
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, whenToNotify.getTime(), scheduledReceive);
        }
        else
        {
            deleteNotification();
        }
    }

    private void deleteNotification()
    {
        int notificationId = Integer.parseInt(1212 + "" + reminderId);
        Context context = getApplicationContext();

        Intent receiverIntent = new Intent(context, ReminderCreator.class);
        receiverIntent.putExtra("notificationId", notificationId);
        PendingIntent scheduledReceive = PendingIntent.getBroadcast(context, notificationId, receiverIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(scheduledReceive);
    }

    private void createTimeFilter() {
        filterHour  = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest,
                                       int dstart, int dend) {

                if(source.length() > 1 && !doneOnce){
                    source = source.subSequence(source.length()-1, source.length());
                    if(source.charAt(0)  >= '0' && source.charAt(0) <= '2'){
                        doneOnce = true;
                        return source;
                    }else{
                        return "";
                    }
                }


                if (source.length() == 0) {
                    return null;// deleting, keep original editing
                }
                String result = "";
                result += dest.toString().substring(0, dstart);
                result += source.toString().substring(start, end);
                result += dest.toString().substring(dend, dest.length());

                if (result.length() > 5) {
                    return "";// do not allow this edit
                }
                boolean allowEdit = true;
                char c;
                if (result.length() > 0) {
                    c = result.charAt(0);
                    allowEdit = (c >= '0' && c <= '2');
                }
                if (result.length() > 1) {
                    c = result.charAt(1);
                    if(result.charAt(0) == '0' || result.charAt(0) == '1')
                        allowEdit = allowEdit && (c >= '0' && c <= '9');
                    else
                        allowEdit = allowEdit && (c >= '0' && c <= '3');
                }
                if (result.length() > 2) {
                    c = result.charAt(2);
                    allowEdit = allowEdit && (c == ':');
                }
                if (result.length() > 3) {
                    c = result.charAt(3);
                    allowEdit = allowEdit && (c >= '0' && c <= '5');
                }
                if (result.length() > 4) {
                    c = result.charAt(4);
                    allowEdit = allowEdit && (c >= '0' && c <= '9');
                }
                return allowEdit ? null : "";
            }

        };
    }
}