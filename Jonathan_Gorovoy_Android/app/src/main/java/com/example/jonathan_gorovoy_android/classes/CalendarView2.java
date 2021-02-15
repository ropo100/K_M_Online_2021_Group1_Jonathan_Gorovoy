package com.example.jonathan_gorovoy_android.classes;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.jonathan_gorovoy_android.R;
import com.example.jonathan_gorovoy_android.adapters.CalendarViewAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class CalendarView2 extends LinearLayout {
    final int DAYS_COUNT = 42; // 6 rows and 7 columns to display the weeks
    LinearLayout header;
    ImageView btnPrev;
    ImageView btnNext;
    TextView txtDisplayDate;
    GridView gridView;
    Calendar currentDate= Calendar.getInstance();
    EventHandler eventHandler = null;

    public CalendarView2(Context context) {
        super(context);
        initControl(context);
    }

    public CalendarView2(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        initControl(context);
    }

    private void initControl(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.calendar_view, this);
        initUi();
        initClickListeners();
        updateCalendar();
    }

    private void initUi() {
        // after layout inflation, connect between ids and local variables
        header = findViewById(R.id.calendarWeekdayHeader);
        btnPrev = findViewById(R.id.calendarPrevButton);
        btnNext = findViewById(R.id.calendarNextButton);
        txtDisplayDate = findViewById(R.id.calendarDateDisplay);
        gridView = findViewById(R.id.calendarGrid);
    }

    private void initClickListeners() {
        btnPrev.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                currentDate.add(Calendar.MONTH, -1); // remove 1 month from current date
                updateCalendar();
            }
        });

        btnNext.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                currentDate.add(Calendar.MONTH, 1); // add 1 month to current date
                updateCalendar();
            }
        });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() { // click on specific day
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(eventHandler != null)
                    eventHandler.onDayPress((Date)parent.getItemAtPosition(position)); // call handler of day press outside class
            }
        });

        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() { // long click to select 1 whole week
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if(eventHandler == null)
                    return false;

                eventHandler.onWeekLongPress((Date)parent.getItemAtPosition(position)); // call long press handler outside class
                return true;
            }
        });
    }

    public void updateCalendar()
    {
        ArrayList<Date> cells = new ArrayList<>();
        Calendar calendar = (Calendar)currentDate.clone();

        calendar.set(Calendar.DAY_OF_MONTH, 1); // set to first day of month
        int monthBeginningCell = calendar.get(Calendar.DAY_OF_WEEK) - 1; // cell according to index of the first day in its week
        // for example if day 1 is a Sunday, index in GridView cells is 0 because its the first cell in the first row of GridView

        // move calendar to the first day of the week the month starts in (useful when month doesnt start on sunday)
        calendar.add(Calendar.DAY_OF_MONTH, -monthBeginningCell);

        while (cells.size() < DAYS_COUNT)
        { // get number of each day in month and add it to GridView cell
            cells.add(calendar.getTime()); // add current day of calendar to cells of GridView
            calendar.add(Calendar.DAY_OF_MONTH, 1); // progress to the next day, basically currentDay+1
        }

        // update grid
        gridView.setAdapter(new CalendarViewAdapter(getContext(), cells, currentDate));

        // update title of calendar to "Apr 2021" and such depending on the month
        SimpleDateFormat sdf = new SimpleDateFormat("MMM yyyy");
        String monthDate = sdf.format(currentDate.getTime());
        txtDisplayDate.setText(monthDate);
    }

    public void setEventHandler(EventHandler eventHandler)
    { // to set eventHandler of calendar in outside classes
        this.eventHandler = eventHandler;
    }

    public interface EventHandler
    { // interface for event handling by outside classes
        void onDayPress(Date date); // date of pressed day
        void onWeekLongPress(Date date); // date of day clicked within the week
    }
}