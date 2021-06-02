package com.example.jonathan_gorovoy_android;

import androidx.appcompat.app.AppCompatActivity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    Button btnDeadlines, btnRoutines, btnCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createNotificationChannel();

        btnDeadlines=(Button)findViewById(R.id.btnDeadlines);
        btnRoutines=(Button)findViewById(R.id.btnRoutines);
        btnCalendar=(Button)findViewById(R.id.btnCalendar);

        btnDeadlines.setOnClickListener(this::onClick);
        btnRoutines.setOnClickListener(this::onClick);
        btnCalendar.setOnClickListener(this::onClick);
    }
    public void onClick(View view) {
        Intent i;
        switch(view.getId())
        {
            case R.id.btnDeadlines:
                i = new Intent(this, ViewDeadlinesActivity.class);
                i.putExtra("source_activity", "activity_main");
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                break;
            case R.id.btnRoutines:
                i = new Intent(this, ViewRoutinesActivity.class);
                i.putExtra("source_activity", "activity_main");
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                break;
            case R.id.btnCalendar:
                i = new Intent(this, MonthCalendarActivity.class);
                i.putExtra("source_activity", "activity_main");
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        //do nothing because this is the landing page
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) { //if api level is the one that requires a channel
            CharSequence name = getString(R.string.notifChannelName);
            String description = getString(R.string.notifChannelDesc);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(getString(R.string.notifChannelId), name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}