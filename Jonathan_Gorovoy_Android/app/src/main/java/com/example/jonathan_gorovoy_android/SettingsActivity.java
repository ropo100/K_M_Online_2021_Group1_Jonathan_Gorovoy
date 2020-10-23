package com.example.jonathan_gorovoy_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SettingsActivity extends AppCompatActivity {

    Button btn1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        btn1=(Button)findViewById(R.id.button61);

        btn1.setOnClickListener(this::onClick);
    }

    public void onClick(View view) {
        Intent i;
        switch(view.getId()) {
            case R.id.button61:
                i = new Intent(this, MainActivity.class);
                i.putExtra("source_activity", "activity_settings");
                startActivity(i);
                break;
        }
    }
}