package com.lumos.calendar.calendarandroid;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.lumos.calendar.calendarandroid.util.EventListAdapter;

public class SearchActivity extends Activity {

    private Button searchEventButton;
    private ListView searchList;
    private EditText searchKey;
    private Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_activity);
        context = this;

        searchKey = findViewById(R.id.search_key);
        searchEventButton = findViewById(R.id.search_event_button);

        searchList = findViewById(R.id.search_view);
        searchEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventListAdapter adapter = new EventListAdapter(context, R.layout.adapter_view_layout, MainActivity.searchEvent(searchKey.getText().toString()));
                searchList.setAdapter(adapter);
            }
        });

        searchList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get the selected item text from ListView
                SampleEvent selectedItem = (SampleEvent) parent.getItemAtPosition(position);

                // Display the selected item text on TextView
                Intent activity = new Intent(SearchActivity.this, EventFormActivity.class);
                activity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                Bundle b = new Bundle();
                b.putInt("id", selectedItem.getId());
                b.putString("title", selectedItem.getTitle());
                b.putString("description", selectedItem.getDescription());
                b.putString("recurring", selectedItem.getRecurring());
                b.putString("reminder", selectedItem.getReminder());
                b.putLong("start_time", selectedItem.getStartAt().getTime());
                b.putLong("end_time", selectedItem.getEndAt().getTime());
                activity.putExtras(b); //Put your id to your next Intent

                SearchActivity.this.startActivity(activity);
            }
        });
    }
}
