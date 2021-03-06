package com.example.jonathan_gorovoy_android;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.example.jonathan_gorovoy_android.classes.DeadlineView;
import com.example.jonathan_gorovoy_android.classes.EventDayView;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;

public class Dal extends SQLiteAssetHelper{
    public Dal(Context context) {
        super(context, "events_db.db", null, 1);
    }

    public void addEvent(int day, int month, int year, String startHour, String endHour, String title, String description, int routineIndex, int eventIndex, boolean isDeadline) {
        //if not part of routine routineIndex would be 0
        SQLiteDatabase db = getWritableDatabase();
        SQLiteStatement statement;
        if(eventIndex == 0) {
            String sql_INSERT = "INSERT INTO events (day, month, year, startHour, endHour, title, description, routineIndex, isDeadline) VALUES (?,?,?,?,?,?,?,?,?)";
            statement = db.compileStatement(sql_INSERT);
        }
        else
        {
            String sql_UPDATE = "UPDATE events SET day=?, month=?, year=?, startHour=?, endHour=?, title=?, description=?, routineIndex=?, isDeadline=? WHERE id=?";
            statement = db.compileStatement(sql_UPDATE);
        }

        statement.bindLong(1, day);
        statement.bindLong(2, month);
        statement.bindLong(3, year);
        statement.bindString(4, startHour);
        statement.bindString(5, endHour);
        statement.bindString(6, title);
        statement.bindString(7, description);
        statement.bindLong(8, routineIndex);
        statement.bindLong(9, isDeadline ? 1 : 0);
        if(eventIndex != 0)
        {
            statement.bindLong(10, eventIndex);
        }
        statement.execute();

        db.close();
    }

    public void addRoutine(String routineName) {
        SQLiteDatabase db = getWritableDatabase();
        String sql_INSERT = "INSERT INTO routines (routineName) values (?)";
        SQLiteStatement statement = db.compileStatement(sql_INSERT);

        statement.bindString(1, routineName);
        statement.execute();
        db.close();
    }

    public void addReminder(int reminderId, int eventIndex, Integer amountChosen, String unitChosen)
    {
        boolean wouldBeDuplicate = reminderId == 0 && getReminderIndex(eventIndex, amountChosen, unitChosen) != 0;
        SQLiteDatabase db = getWritableDatabase();
        SQLiteStatement statement;
        if(reminderId == 0) {
            String sql_INSERT = "INSERT INTO reminders (eventIndex, timeBefore, unit) values (?,?,?)";
            statement = db.compileStatement(sql_INSERT);
        }
        else
        {
            String sql_UPDATE = "UPDATE reminders SET eventIndex=?, timeBefore=?, unit=? WHERE id="+reminderId;
            statement = db.compileStatement(sql_UPDATE);
        }

        if(!wouldBeDuplicate) {
            statement.bindLong(1, eventIndex);
            statement.bindLong(2, amountChosen);
            statement.bindString(3, unitChosen);
            statement.execute();
        }
        db.close();
    }

    public String getRoutineName(int routineIndex)
    {
        String st = "SELECT routineName FROM routines WHERE id="+routineIndex;
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery(st, null);
        String routineName = "";
        while(cursor.moveToNext()) {
            routineName = cursor.getString(cursor.getColumnIndex("routineName"));
        }
        cursor.close();
        db.close();
        return routineName;
    }

    public int getRoutineIndex(String routineName)
    {
        String st = "SELECT id FROM routines WHERE routineName='"+routineName+"'";
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery(st, null);
        int id = 0;
        while(cursor.moveToNext()) {
            id = cursor.getInt(cursor.getColumnIndex("id"));
        }
        cursor.close();
        db.close();
        return id;
    }

    public int getReminderIndex(int eventIndex, Integer amount, String unit)
    {
        int reminderId = 0;
        String st = "SELECT * FROM reminders WHERE eventIndex="+eventIndex+" AND timeBefore="+amount+" AND unit='"+unit+"'";
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery(st, null);
        while(cursor.moveToNext()) {
            reminderId = cursor.getInt(cursor.getColumnIndex("id"));
        }

        cursor.close();
        db.close();
        return reminderId;
    }

    public ArrayList<String> getRoutines()
    {
        ArrayList<String> ary = new ArrayList<>();
        String st = "SELECT routineName FROM routines";
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery(st, null);
        while(cursor.moveToNext()) {
            String routineName = cursor.getString(cursor.getColumnIndex("routineName"));
            ary.add(routineName);
        }
        cursor.close();
        db.close();
        return ary;
    }

    public ArrayList<EventDayView> getEventsInDay(int day, int month, int year, int routineIndex, boolean inPast)
    {
        ArrayList<EventDayView> ary = new ArrayList<>();
        String st;
        SQLiteDatabase db = getWritableDatabase();
        if(routineIndex != 0)
        {
            st="SELECT * FROM events WHERE routineIndex="+routineIndex+" ORDER BY startHour ASC";
        }
        else
        {
            st="SELECT * FROM events WHERE day="+day+" AND month="+month+" AND year="+year+" ORDER BY startHour ASC";
        }

        Cursor cursor = db.rawQuery(st, null);
        while(cursor.moveToNext())
        {
            String startHour = cursor.getString(cursor.getColumnIndex("startHour"));
            String endHour = cursor.getString(cursor.getColumnIndex("endHour"));
            String title = cursor.getString(cursor.getColumnIndex("title"));
            String description = cursor.getString(cursor.getColumnIndex("description"));
            int eventIndex = cursor.getInt(cursor.getColumnIndex("id"));
            boolean isDeadline = cursor.getInt(cursor.getColumnIndex("isDeadline")) != 0;
            EventDayView e = new EventDayView(startHour, endHour, title, description, eventIndex, isDeadline);
            e.setInPastValue(inPast);
            ary.add(e);
        }

        cursor.close();
        db.close();
        return ary;
    }


    public ArrayList<DeadlineView> getAllDeadlines()
    {
        ArrayList<DeadlineView> ary = new ArrayList<>();
        String st = "SELECT * FROM events WHERE isDeadline=1 AND routineIndex=0 ORDER BY year, month, day";
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery(st, null);
        while(cursor.moveToNext()) {
            String deadlineTitle = cursor.getString(cursor.getColumnIndex("title"));
            int deadlineDay = cursor.getInt(cursor.getColumnIndex("day"));
            int deadlineMonth = cursor.getInt(cursor.getColumnIndex("month"));
            int deadlineYear = cursor.getInt(cursor.getColumnIndex("year"));
            int eventIndex = cursor.getInt(cursor.getColumnIndex("id"));
            String description = cursor.getString(cursor.getColumnIndex("description"));
            String startHour = cursor.getString(cursor.getColumnIndex("startHour"));
            String endHour = cursor.getString(cursor.getColumnIndex("endHour"));
            ary.add(new DeadlineView(deadlineTitle, deadlineDay, deadlineMonth, deadlineYear, eventIndex, startHour, endHour, description));
        }
        cursor.close();
        db.close();
        return ary;
    }

    public ArrayList<String> getReminders(int eventIndex)
    {
        ArrayList<String> ary = new ArrayList<>();
        String st = "SELECT * FROM reminders WHERE eventIndex="+eventIndex;
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery(st, null);
        while(cursor.moveToNext()) {
            int amount = cursor.getInt(cursor.getColumnIndex("timeBefore"));
            String unit = cursor.getString(cursor.getColumnIndex("unit"));
            ary.add(amount+" "+unit);
        }
        cursor.close();
        db.close();
        return ary;
    }

    public void deleteEvent(int eventIndex)
    {
        SQLiteDatabase db = getWritableDatabase();
        String sql_DELETE = "DELETE FROM events WHERE id=?";
        SQLiteStatement statement = db.compileStatement(sql_DELETE);
        statement.bindLong(1, eventIndex);
        statement.execute();
        db.close();
    }

    public void deleteRoutine(int routineIndex)
    {
        SQLiteDatabase db = getWritableDatabase();
        String sql_DELETE = "DELETE FROM routines WHERE id=?";
        SQLiteStatement statement = db.compileStatement(sql_DELETE);

        statement.bindLong(1, routineIndex);
        statement.execute();
        db.close();
    }

    public void deleteReminder(int reminderId) {
        SQLiteDatabase db = getWritableDatabase();
        String sql_DELETE = "DELETE FROM reminders WHERE id=?";
        SQLiteStatement statement = db.compileStatement(sql_DELETE);

        statement.bindLong(1, reminderId);
        statement.execute();
        db.close();
    }
}
