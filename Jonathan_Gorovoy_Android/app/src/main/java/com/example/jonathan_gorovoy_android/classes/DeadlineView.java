package com.example.jonathan_gorovoy_android.classes;

public class DeadlineView {
    private String text;
    private String date;

    public DeadlineView(String text, String date)
    {
        this.text= text;
        this.date = date;
    }

    public String getText() { return this.text;}
    public void setText(String text) { this.text=text;}

    public String getDate() { return this.date;}
    public void setDate(String date) { this.date=date;}

    public String toString()
    {
        return "DeadlineView{" +
                "text='" + text + '\'' +
                "date='" + date + '\'' +
                '}';
    }
}
