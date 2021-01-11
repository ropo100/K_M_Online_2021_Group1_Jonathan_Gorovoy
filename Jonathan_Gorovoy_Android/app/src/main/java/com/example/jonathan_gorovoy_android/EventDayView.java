package com.example.jonathan_gorovoy_android;

public class EventDayView { // class for an event in the view of days in particular
    private String startHour;
    private String endHour;
    private String title;
    private String description;

    public EventDayView(String startHour, String endHour, String title, String description)
    {
        this.startHour = startHour;
        this.endHour = endHour;
        this.title = title;
        this.description = description;
    }

    public String getStartHour() { return this.startHour;}
    public void setStartHour(String startHour) { this.startHour=startHour;}

    public String getEndHour() { return this.endHour;}
    public void setEndHour(String endHour) { this.endHour=endHour;}

    public String getTitle() { return this.title;}
    public void setTitle(String title) { this.title=title;}

    public String getDescription() { return this.description;}
    public void setDescription(String description) { this.description=description;}

    public String toString()
    {
        return "EventDayView{" +
                "startHour='" + startHour + '\'' +
                "endHour='" + endHour + '\'' +
                "title='" + title + '\'' +
                "description='" + description + '\'' +
                '}';
    }
}
