package com.example.jonathan_gorovoy_android.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.jonathan_gorovoy_android.classes.DeadlineView;
import com.example.jonathan_gorovoy_android.R;

import java.util.List;

public class DeadlineViewAdapter extends ArrayAdapter<DeadlineView> {
    private final Context ctx;
    private final int eventResourceId;
    private final List<DeadlineView> data;

    public DeadlineViewAdapter(Context context, int resource, List<DeadlineView> objects)
    {
        super(context, resource, objects);
        this.ctx = context;
        this.eventResourceId = resource;
        this.data = objects;
    }

    @Override
    public int getCount(){
        return data.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater li = (LayoutInflater)this.ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = li.inflate(this.eventResourceId, null);
        DeadlineView dv = this.data.get(position);
        TextView deadlineText = view.findViewById(R.id.deadlineText);
        deadlineText.setText(dv.getText());
        TextView deadlineDate = view.findViewById(R.id.deadlineDate);
        deadlineDate.setText(dv.getDate());
        return view;
    }
}
