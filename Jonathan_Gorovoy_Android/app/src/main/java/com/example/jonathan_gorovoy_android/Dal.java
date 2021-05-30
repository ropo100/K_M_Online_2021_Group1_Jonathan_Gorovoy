package com.example.jonathan_gorovoy_android;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.widget.Toast;

import com.example.jonathan_gorovoy_android.classes.DeadlineView;
import com.example.jonathan_gorovoy_android.classes.EventDayView;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;

public class Dal extends SQLiteAssetHelper{
    public Dal(Context context) {
        super(context, "events_db.db", null, 1);
    }

    public void addEvent(int day, int month, int year, String startHour, String endHour, String title, String description, int routineIndex, int eventIndex) {
        //if not part of routine routineIndex would be 0
        SQLiteDatabase db = getWritableDatabase();
        SQLiteStatement statement;
        if(eventIndex == 0) {
            String sql_INSERT = "INSERT INTO events (day, month, year, startHour, endHour, title, description, routineIndex) VALUES (?,?,?,?,?,?,?,?)";
            statement = db.compileStatement(sql_INSERT);
        }
        else
        {
            String sql_UPDATE = "UPDATE events SET day=?, month=?, year=?, startHour=?, endHour=?, title=?, description=?, routineIndex=? WHERE id=?";
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
        if(eventIndex != 0)
        {
            statement.bindLong(9, eventIndex);
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

    public void addDeadline(int dueDay, int dueMonth, int dueYear, int eventIndex) {
        SQLiteDatabase db = getWritableDatabase();
        SQLiteStatement statement;
        if(isDeadline(eventIndex)) {
            String sql_INSERT = "INSERT INTO deadlines (dueDay, dueMonth, dueYear, eventIndex) values (?,?,?,?)";
            statement = db.compileStatement(sql_INSERT);
        }
        else
        {
            String sql_UPDATE = "UPDATE deadlines SET dueDay=?, dueMonth=?, dueYear=? WHERE eventIndex=?";
            statement = db.compileStatement(sql_UPDATE);
        }

        statement.bindLong(1, dueDay);
        statement.bindLong(2, dueMonth);
        statement.bindLong(3, dueYear);
        statement.bindLong(4, eventIndex);
        statement.execute();
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
        String st = "SELECT id FROM routines WHERE routineName=\'"+routineName+"\'";
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

    public ArrayList<String> getRoutines()
    {
        ArrayList<String> ary = new ArrayList<>();
        String st = "SELECT routineName FROM routines";
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery(st, null);
        String routineName = "";
        while(cursor.moveToNext()) {
            routineName = cursor.getString(cursor.getColumnIndex("routineName"));
            ary.add(routineName);
        }
        cursor.close();
        db.close();
        return ary;
    }

    public ArrayList<EventDayView> getEventsInDay(int day, int month, int year, int routineIndex)
    {
        ArrayList<EventDayView> ary = new ArrayList<>();
        String st = "";
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
            EventDayView e = new EventDayView(startHour, endHour, title, description, eventIndex);
            ary.add(e);
        }

        cursor.close();
        db.close();
        return ary;
    }

    public boolean isDeadline(int eventIndex)
    {
        String st = "SELECT * FROM deadlines WHERE eventIndex="+eventIndex;
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery(st, null);
        boolean found = cursor.getCount() != 0;
        cursor.close();
        db.close();
        return found;
    }

    public String getDeadline(int eventIndex)
    {
        String st = "SELECT * FROM deadlines WHERE eventIndex="+eventIndex;
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery(st, null);
        String date = cursor.getInt(cursor.getColumnIndex("dueDay")) + "/" + cursor.getInt(cursor.getColumnIndex("dueMonth")) + "/" + cursor.getInt(cursor.getColumnIndex("dueYear"));
        cursor.close();
        db.close();
        return date;
    }

    public ArrayList<DeadlineView> getAllDeadlines()
    {
        return null;
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

    public void deleteDeadline(int eventIndex)
    {
        SQLiteDatabase db = getWritableDatabase();
        String sql_DELETE = "DELETE FROM deadlines WHERE eventIndex=?";
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
}
