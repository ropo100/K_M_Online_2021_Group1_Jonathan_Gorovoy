package com.example.jonathan_gorovoy_android.classes;

public class DeadlineView {
    private String text;
    private String date;
    private int day;
    private int month;
    private int year;
    private int eventIndex;
    private String startHour;
    private String endHour;
    private String description;

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
    public void setText(String text) { this.text=text;}

    public String getDate() { return this.date;}
    public void setDate(String date) { this.date=date;}

    public int getDay() { return this.day;}
    public void setDay(int day) { this.day=day;}

    public int getMonth() { return this.month;}
    public void setMonth(int month) { this.month=month;}

    public int getYear() { return this.year;}
    public void setYear(int year) { this.year=year;}

    public int getEventIndex() { return this.eventIndex;}
    public void setEventIndex(int eventIndex) { this.eventIndex=eventIndex;}

    public String getStartHour() { return this.startHour;}
    public void setStartHour(String startHour) { this.startHour=startHour;}

    public String getEndHour() { return this.endHour;}
    public void setEndHour(String endHour) { this.endHour=endHour;}

    public String getDescription() { return this.description;}
    public void setDescription(String description) { this.description=description;}

    public String toString()
    {
        return "DeadlineView{" +
                "text='" + text + '\'' +
                "date='" + date + '\'' +
                '}';
    }
}
