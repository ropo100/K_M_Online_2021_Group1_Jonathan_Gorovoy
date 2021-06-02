package com.example.jonathan_gorovoy_android.classes;

public class DeadlineView {
    private final String text;
    private final String date;
    private final int day;
    private final int month;
    private final int year;
    private final int eventIndex;
    private final String startHour;
    private final String endHour;
    private final String description;

    public DeadlineView(String text, int day, int month, int year, int eventIndex, String startHour, String endHour, String description)
    {
        this.text= text;
        this.day = day;
        this.month = month;
        this.year = year;
        this.date = day + "/" + month + "/" + year;
        this.eventIndex = eventIndex;
        this.startHour = startHour;
        this.endHour = endHour;
        this.description = description;
    }

    public String getText() { return this.text;}

    public String getDate() { return this.date;}

    public int getDay() { return this.day;}

    public int getMonth() { return this.month;}

    public int getYear() { return this.year;}

    public int getEventIndex() { return this.eventIndex;}

    public String getStartHour() { return this.startHour;}

    public String getEndHour() { return this.endHour;}

    public String getDescription() { return this.description;}

    public String toString()
    {
        return "DeadlineView{" +
                "text='" + text + '\'' +
                "date='" + date + '\'' +
                '}';
    }
}
