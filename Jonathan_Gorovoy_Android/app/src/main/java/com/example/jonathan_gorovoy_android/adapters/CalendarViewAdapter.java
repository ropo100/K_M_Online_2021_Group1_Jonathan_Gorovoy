package com.example.jonathan_gorovoy_android.adapters;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.Context;
import android.graphics.Color;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.jonathan_gorovoy_android.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class CalendarViewAdapter extends ArrayAdapter<Date> {
    // for view inflation
    private LayoutInflater inflater;
    private Calendar currentCalendar;

    public CalendarViewAdapter(Context context, ArrayList<Date> days, Calendar currentCalendar)
    {
        super(context, R.layout.calendar_day_view, days);
        inflater = LayoutInflater.from(context);
        this.currentCalendar = (Calendar)currentCalendar.clone();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View view, @NonNull ViewGroup parent)
    {
        Calendar calendar = Calendar.getInstance();
        Date date = getItem(position); // day in question
        calendar.setTime(date);
        int day = calendar.get(Calendar.DATE);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

        // today
        Date today = new Date();
        Calendar calendarToday = Calendar.getInstance();
        calendarToday.setTime(today);

        // inflate item if it does not exist yet
        if (view == null)
            view = inflater.inflate(R.layout.calendar_day_view, parent, false);

        // set text color to black by default
        ((TextView)view).setTextColor(Color.BLACK);
        // center text inside TextView
        ((TextView)view).setGravity(Gravity.CENTER);
        if (month != currentCalendar.get(Calendar.MONTH) || year != currentCalendar.get(Calendar.YEAR)) {
            // if day not in current month/year that is being displayed, make it gray
            ((TextView)view).setTextColor(Color.LTGRAY);
        } else if (day == calendarToday.get(Calendar.DATE) && month == calendarToday.get(Calendar.MONTH) && year == calendarToday.get(Calendar.YEAR)) {
            // if day is today, change background to green
            view.setBackgroundColor(getContext().getResources().getColor(R.color.green_light, null));
        }
        else if (day != calendarToday.get(Calendar.DATE) && month != calendarToday.get(Calendar.MONTH) && year != calendarToday.get(Calendar.YEAR))
        {
            view.setBackgroundColor(getContext().getResources().getColor(R.color.white, null));
        }

        // set actual text to number of date in question
        ((TextView)view).setText(String.valueOf(calendar.get(Calendar.DATE)));

        return view;
    }
}