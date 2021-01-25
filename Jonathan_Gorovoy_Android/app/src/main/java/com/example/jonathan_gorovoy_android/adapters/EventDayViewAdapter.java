package com.example.jonathan_gorovoy_android.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.jonathan_gorovoy_android.classes.EventDayView;
import com.example.jonathan_gorovoy_android.R;

import java.util.List;

public class EventDayViewAdapter extends ArrayAdapter<EventDayView>
{
    private Context ctx;
    private int eventResourceId;
    private List<EventDayView> data;

    public EventDayViewAdapter(Context context, int resource, List<EventDayView> objects)
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
        EventDayView ev = this.data.get(position);
        TextView startHour = view.findViewById(R.id.eventStartHour);
        startHour.setText(ev.getStartHour());
        TextView endHour = view.findViewById(R.id.eventEndHour);
        endHour.setText(ev.getEndHour());
        TextView title = view.findViewById(R.id.eventTitle);
        title.setText(ev.getTitle());
        TextView description = view.findViewById(R.id.eventDescription);
        description.setText(ev.getDescription());
        return view;
    }

}
