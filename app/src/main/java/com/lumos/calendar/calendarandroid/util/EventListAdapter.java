package com.lumos.calendar.calendarandroid.util;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.lumos.calendar.calendarandroid.R;
import com.lumos.calendar.calendarandroid.SampleEvent;

import java.util.ArrayList;

public class EventListAdapter extends ArrayAdapter<SampleEvent> {

    private static final String TAG = "EventListAdapter";

    private Context mContext;
    private int mResource;
    private int lastPosition = -1;

    /**
     * Holds variables in a View
     */
    private static class ViewHolder {
        TextView title;
        TextView description;
    }

    /**
     * Default constructor for the PersonListAdapter
     * @param context
     * @param resource
     * @param objects
     */
    public EventListAdapter(Context context, int resource, ArrayList<SampleEvent> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //get the persons information
        String title = getItem(position).getTitle();
        String description = getItem(position).getDescription();

        //create the view result for showing the animation

        //ViewHolder object
        ViewHolder holder;

        if(convertView == null){
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(mResource, parent, false);
            holder= new ViewHolder();
            holder.title = (TextView) convertView.findViewById(R.id.show_title);
            holder.description = (TextView) convertView.findViewById(R.id.description);

            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder) convertView.getTag();
        }

        lastPosition = position;

        holder.title.setText(title);
        holder.description.setText(description);

        return convertView;
    }
}
