package com.example.jonathan_gorovoy_android.classes;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class EventDayView { // class for an event in the view of days in particular
    private String startHour;
    private String endHour;
    private String title;
    private String description;
    private boolean inPast;
    private int eventIndex;

    public EventDayView(String startHour, String endHour, String title, String description, int eventIndex)
    {
        this.startHour = startHour;
        this.endHour = endHour;
        this.title = title;
        this.description = description;
        this.inPast = false;
        this.eventIndex = eventIndex;
    }

    public void setInPast(int year, int month, int day, boolean isSpecificDay, Date currentDate)
    { // function for deciding if event is in the past from the current time, currentDate is GregorianCalendar.getInstance().getTime()
        if(!isSpecificDay)
        {
            inPast = false;
        }
        else
        {
            int hour = Integer.parseInt(endHour.substring(0, endHour.indexOf(":")));
            int minute = Integer.parseInt(endHour.substring(endHour.indexOf(":")+1));
            GregorianCalendar eventDate = new GregorianCalendar(year, month, day, hour, minute);
            if(currentDate.after(eventDate.getTime()))
            {
                inPast = true;
            }
        }
    }

    public String getStartHour() { return this.startHour;}
    public void setStartHour(String startHour) { this.startHour=startHour;}

    public String getEndHour() { return this.endHour;}
    public void setEndHour(String endHour) { this.endHour=endHour;}

    public String getTitle() { return this.title;}
    public void setTitle(String title) { this.title=title;}

    public String getDescription() { return this.description;}
    public void setDescription(String description) { this.description=description;}

    public boolean getInPast() { return this.inPast;}
    public void setInPastValue(boolean inPast) { this.inPast = inPast;} // if you know from the start if it is in the past

    public int getEventIndex() { return this.eventIndex; };
    public void setEventIndex(int eventIndex) { this.eventIndex = eventIndex; };

    public String toString()
    {
        return "EventDayView{" +
                "startHour='" + startHour + "\' " +
                "endHour='" + endHour + "\' " +
                "title='" + title + "\' " +
                "description='" + description + "\' " +
                "eventIndex=" + eventIndex +
                '}';
    }
}
