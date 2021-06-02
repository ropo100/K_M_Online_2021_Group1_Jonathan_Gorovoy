package com.example.jonathan_gorovoy_android.classes;

import java.util.Date;
import java.util.GregorianCalendar;

public class EventDayView { // class for an event in the view of days in particular
    private final String startHour;
    private final String endHour;
    private final String title;
    private final String description;
    private boolean inPast;
    private final int eventIndex;
    private final boolean isDeadline;

    public EventDayView(String startHour, String endHour, String title, String description, int eventIndex, boolean isDeadline)
    {
        this.startHour = startHour;
        this.endHour = endHour;
        this.title = title;
        this.description = description;
        this.inPast = false;
        this.eventIndex = eventIndex;
        this.isDeadline = isDeadline;
    }

    public String getStartHour() { return this.startHour;}

    public String getEndHour() { return this.endHour;}

    public String getTitle() { return this.title;}

    public String getDescription() { return this.description;}

    public boolean getInPast() { return this.inPast;}
    public void setInPastValue(boolean inPast) { this.inPast = inPast;} // if you know from the start if it is in the past

    public int getEventIndex() { return this.eventIndex; }

    public boolean getIsDeadline() { return this.isDeadline; }

    public String toString()
    {
        return "EventDayView{" +
                "startHour='" + startHour + "' " +
                "endHour='" + endHour + "' " +
                "title='" + title + "' " +
                "description='" + description + "' " +
                "eventIndex=" + eventIndex +
                '}';
    }
}
