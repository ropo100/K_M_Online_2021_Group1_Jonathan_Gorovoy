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
    ArrayList<String> routineArray = new ArrayList<>();

    Button btnCreate, btnRemove;

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

        btnCreate=(Button)findViewById(R.id.btnCreate);
        btnRemove=(Button)findViewById(R.id.btnRemove);

        routineList = (ListView)findViewById(R.id.routineList);
        routineArray = dal.getRoutines();
        ArrayAdapter<String> sa = new ArrayAdapter<>(this, android.R.layout.simple_expandable_list_item_1, routineArray);
        routineList.setAdapter(sa);
        @SuppressWarnings("Convert2Lambda") AdapterView.OnItemClickListener routineListListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String routineName = routineArray.get(position);
                routineIndex = dal.getRoutineIndex(routineName);
                Intent i = new Intent(ViewRoutinesActivity.this, ModifyDayActivity.class);
                i.putExtra("source_activity", "activity_view_routines");
                i.putExtra("routineIndex", routineIndex);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
            }
        };
        routineList.setOnItemClickListener(routineListListener);

        btnCreate.setOnClickListener(this::onClick);
        btnRemove.setOnClickListener(this::onClick);
    }

    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.btnCreate:
                //enter string dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                EditText routineInput = new EditText(this);
                builder.setTitle("Create Routine");
                routineInput.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(routineInput);
                //noinspection Convert2Lambda
                builder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        routineText = routineInput.getText().toString();
                        dal.addRoutine(routineText);
                        routineArray = dal.getRoutines();
                        ArrayAdapter<String> sa = new ArrayAdapter<>(ViewRoutinesActivity.this, android.R.layout.simple_expandable_list_item_1, routineArray);
                        routineList.setAdapter(sa);
                    }
                });
                //noinspection Convert2Lambda
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
                //choose routine by name and delete it
                ListAdapter routineListAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, routineArray);
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
                dialogBuilder.setTitle("Remove Routine");
                //noinspection Convert2Lambda
                dialogBuilder.setAdapter(routineListAdapter,
                  new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        dal.deleteRoutine(dal.getRoutineIndex(routineArray.get(item)));
                        routineArray = dal.getRoutines();
                        ArrayAdapter<String> sa = new ArrayAdapter<>(ViewRoutinesActivity.this, android.R.layout.simple_expandable_list_item_1, routineArray);
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
        // Respond to the action bar's Up/Home button
        if (item.getItemId() == android.R.id.home) {
            Intent i = new Intent(this, MainActivity.class);
            i.putExtra("source_activity", "activity_view_routines");
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(this, MainActivity.class);
        i.putExtra("source_activity", "activity_view_routines");
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }
}