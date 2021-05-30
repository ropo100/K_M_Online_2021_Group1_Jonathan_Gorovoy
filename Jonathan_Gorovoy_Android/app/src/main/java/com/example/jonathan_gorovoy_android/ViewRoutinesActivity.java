package com.example.jonathan_gorovoy_android;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class ViewRoutinesActivity extends AppCompatActivity {

    int routineIndex;

    ListView routineList;
    ArrayList<String> routineArray = new ArrayList<String>();

    Button btn1, btn2;

    String routineText = "";
    Dal dal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_routines);

        ActionBar ab = getSupportActionBar();
        if(ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }

        dal = new Dal(ViewRoutinesActivity.this);

        btn1=(Button)findViewById(R.id.btnCreate);
        btn2=(Button)findViewById(R.id.btnRemove);

        routineList = (ListView)findViewById(R.id.routineList);
        //getRoutinesDemo();
        routineArray = dal.getRoutines();
        ArrayAdapter<String> sa = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, routineArray);
        routineList.setAdapter(sa);
        AdapterView.OnItemClickListener routineListListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String routineName = routineArray.get(position);
                routineIndex = dal.getRoutineIndex(routineName);
                Intent i = new Intent(ViewRoutinesActivity.this, ModifyDayActivity.class);
                i.putExtra("source_activity", "activity_view_routines");
                i.putExtra("routineIndex", routineIndex);
                startActivity(i);
            }
        };
        routineList.setOnItemClickListener(routineListListener);

        btn1.setOnClickListener(this::onClick);
        btn2.setOnClickListener(this::onClick);
    }

    private void getRoutinesDemo() {
        String str = "Ordinary Monday";
        routineArray.add(str);
        str = "School Sunday";
        routineArray.add(str);
        str = "Free Day Saturday";
        routineArray.add(str);
        str = "Ordinary Thursday";
        routineArray.add(str);
        str = "Homework Day";
        routineArray.add(str);
    }


    public void onClick(View view) {
        Intent i;
        switch(view.getId()) {
            case R.id.btnCreate:
                //enter string dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                EditText routineInput = new EditText(this);
                builder.setTitle("Create Routine");
                routineInput.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(routineInput);
                builder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        routineText = routineInput.getText().toString();
                        dal.addRoutine(routineText);
                        routineArray = dal.getRoutines();
                        ArrayAdapter<String> sa = new ArrayAdapter<String>(ViewRoutinesActivity.this, android.R.layout.simple_expandable_list_item_1, routineArray);
                        routineList.setAdapter(sa);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog inputDialog = builder.create();
                inputDialog.show();
                break;
            case R.id.btnRemove:
                //choose names with checkbox and delete them if its not cancelled dialog
                ListAdapter routineListAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, routineArray);
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
                dialogBuilder.setTitle("Remove Routine");
                dialogBuilder.setAdapter(routineListAdapter,
                  new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        dal.deleteRoutine(dal.getRoutineIndex(routineArray.get(item)));
                        routineArray = dal.getRoutines();
                        ArrayAdapter<String> sa = new ArrayAdapter<String>(ViewRoutinesActivity.this, android.R.layout.simple_expandable_list_item_1, routineArray);
                        routineList.setAdapter(sa);
                    }
                });
                AlertDialog dialog = dialogBuilder.create();
                dialog.show();
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                Intent i = new Intent(this, MainActivity.class);
                i.putExtra("source_activity", "activity_view_routines");
                startActivity(i);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}