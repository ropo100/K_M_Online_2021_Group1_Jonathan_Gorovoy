package com.example.jonathan_gorovoy_android;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.jonathan_gorovoy_android.classes.ReminderCreator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

@SuppressWarnings("ALL")
public class ModifyEventActivity extends AppCompatActivity {

    Button btnApply, btnDelete, btnShowReminders;
    int year=0, month=0, day=0, eventIndex=0, routineIndex=0;
    boolean isSpecificDay=true, isDeadline=false, inPast = false;
    String title="", description="", startHour="", endHour="";
    String sourceActivity;
    Dal dal;

    CheckBox deadlineCheckbox;
    EditText editTitle, editDescription, editStartHour, editEndHour;
    InputFilter filterHour; // filter for 00:00-23:59 input
    boolean doneOnce = false;

    ArrayList<String> reminderArray = new ArrayList<>();
    final Integer[] amountRange = {0, 1, 2, 3, 4, 5, 10, 15, 20, 25, 30, 40, 45, 50, 55, 60, 90};
    final String[] units = {"minutes", "hours", "days", "weeks"};
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

        btnApply =(Button)findViewById(R.id.btnApply);
        btnDelete =(Button)findViewById(R.id.btnDelete);
        btnShowReminders =(Button)findViewById(R.id.btnShowReminders);

        btnApply.setOnClickListener(this::onClick);
        btnDelete.setOnClickListener(this::onClick);
        btnShowReminders.setOnClickListener(this::onClick);

        if(inPast)
        {
            editTitle.setEnabled(false);
            editDescription.setEnabled(false);
            editStartHour.setEnabled(false);
            editEndHour.setEnabled(false);
            deadlineCheckbox.setEnabled(false);
            btnDelete.setEnabled(false);
            btnApply.setText("OK");
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
                boolean noErrors=true;
                if(!inPast) {
                    startHour = editStartHour.getText().toString();
                    endHour = editEndHour.getText().toString();
                    title = editTitle.getText().toString();
                    description = editDescription.getText().toString();
                    isDeadline = deadlineCheckbox.isChecked();
                    noErrors=false;
                    if(title.isEmpty())
                    {
                        Toast.makeText(this, "Title can't be empty!", Toast.LENGTH_SHORT).show();
                    }
                    else if(startHour.length() != 5 || endHour.length() != 5)
                    {
                        Toast.makeText(this, "You must enter start and end hours!", Toast.LENGTH_SHORT).show();
                    }
                    else if(startHour.compareTo(endHour)>0)
                    {
                        Toast.makeText(this, "End hour must be after start hour!", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        noErrors=true;
                        dal.addEvent(day, month, year, startHour, endHour, title, description, routineIndex, eventIndex, isDeadline);
                        if (eventIndex != 0) { // update all the notifications if this is not a new event
                            //for the purpose of accommodating for changes to title or start hour
                            ArrayList<String> reminderTexts = dal.getReminders(eventIndex);
                            for (int i = 0; i < reminderTexts.size(); i++) {
                                String reminderText = reminderTexts.get(i);
                                amountChosen = Integer.valueOf(reminderText.substring(0, reminderText.indexOf(" ")));
                                unitChosen = reminderText.substring(reminderText.indexOf(" ") + 1);
                                scheduleNotification(getApplicationContext(), dal, eventIndex, amountChosen, unitChosen, title, description, day, month, year, routineIndex, isSpecificDay, startHour);
                            }
                        }
                    }
                }
                if(noErrors) {
                    finishAndReturn();
                }
                break;
            case R.id.btnDelete:
                ArrayList<String> reminderTexts = dal.getReminders(eventIndex);
                for(int i=0;i<reminderTexts.size();i++) {
                    String reminderText = reminderTexts.get(i);
                    amountChosen = Integer.valueOf(reminderText.substring(0, reminderText.indexOf(" ")));
                    unitChosen = reminderText.substring(reminderText.indexOf(" ") + 1);
                    reminderId = dal.getReminderIndex(eventIndex, amountChosen, unitChosen);
                    deleteNotification(getApplicationContext(), reminderId);
                    dal.deleteReminder(reminderId);
                }
                dal.deleteEvent(eventIndex);
                finishAndReturn();
                break;
            case R.id.btnShowReminders:
                if(routineIndex==0) {
                    viewReminders();
                }
                else
                {
                    Toast.makeText(this, "Reminders must be added to events after the routine is applied to a specific day", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    @SuppressWarnings("Convert2Lambda")
    void viewReminders() {
        //cant add reminders to an event before it exists
        if(eventIndex != 0) {
            reminderArray = dal.getReminders(eventIndex);
            ListAdapter reminderAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, reminderArray);
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
            //noinspection Convert2Lambda,Convert2Lambda
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
        ArrayAdapter<Integer> amountAdapter = new ArrayAdapter<>(ModifyEventActivity.this, R.layout.support_simple_spinner_dropdown_item, amountRange);
        amountAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        amountSpinner.setAdapter(amountAdapter);
        ArrayAdapter<String> unitAdapter = new ArrayAdapter<>(ModifyEventActivity.this, R.layout.support_simple_spinner_dropdown_item, units);
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
                scheduleNotification(getApplicationContext(), dal, eventIndex, amountChosen, unitChosen, title, description, day, month, year, routineIndex, isSpecificDay, startHour);
                dialog.cancel();
            }
        });
        reminderBuilder.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(reminderId != 0)
                {
                    deleteNotification(getApplicationContext(), reminderId);
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
        // Respond to the action bar's Up/Home button
        if (item.getItemId() == android.R.id.home) {
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
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        }
        else if(sourceActivity.equals("activity_view_deadlines"))
        {
            i = new Intent(this, ViewDeadlinesActivity.class);
            i.putExtra("source_activity", "activity_modify_event");
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        }
        else
        {
            i = new Intent(this, MainActivity.class);
            i.putExtra("source_activity", "activity_modify_event");
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        }
    }

    public static void scheduleNotification(Context context, Dal dal, int eventIndexP, Integer amountChosenP, String unitChosenP, String titleP, String descriptionP, int dayP, int monthP, int yearP, int routineIndexP, boolean isSpecificDayP, String startHourP)
    {
        int reminderIdP=dal.getReminderIndex(eventIndexP, amountChosenP, unitChosenP);
        int notificationId = Integer.parseInt(1212 + "" + reminderIdP);

        NotificationCompat.Builder reminderBuilder = new NotificationCompat.Builder(context, context.getString(R.string.notifChannelId))
                .setContentTitle(amountChosenP != 0 ? (amountChosenP + " " + (amountChosenP != 1 ? unitChosenP : unitChosenP.substring(0, unitChosenP.length()-1)) + " until " + titleP + " starts") : titleP + " is starting now")
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(descriptionP))
                .setAutoCancel(true)
                .setOngoing(false)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setSmallIcon(R.mipmap.ic_launcher);

        Intent destination = new Intent(context, ModifyDayActivity.class);
        destination.putExtra("source_activity", "activity_modify_event");
        destination.putExtra("day", dayP);
        destination.putExtra("month", monthP);
        destination.putExtra("year",yearP);
        destination.putExtra("routineIndex",routineIndexP);
        destination.putExtra("isSpecificDay", isSpecificDayP);
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
        eventStartCalendar.set(Calendar.YEAR, yearP);
        eventStartCalendar.set(Calendar.MONTH, monthP-1);
        eventStartCalendar.set(Calendar.DAY_OF_MONTH, dayP);
        eventStartCalendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(startHourP.substring(0, startHourP.indexOf(':'))));
        eventStartCalendar.set(Calendar.MINUTE, Integer.parseInt(startHourP.substring(startHourP.indexOf(':')+1)));
        eventStartCalendar.set(Calendar.SECOND, 0);
        int unitType = 0;
        switch(unitChosenP)
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
        eventStartCalendar.add(unitType, -amountChosenP);

        Date whenToNotify = eventStartCalendar.getTime(); //should calculate again after subtracting the amount
        if(today.before(whenToNotify)) { //only notify if the notification time will be in the future and not in the past
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, whenToNotify.getTime(), scheduledReceive);
        }
        else
        {
            deleteNotification(context, reminderIdP);
        }
    }

    public static void deleteNotification(Context context, int reminderIdP)
    {
        int notificationId = Integer.parseInt(1212 + "" + reminderIdP);

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