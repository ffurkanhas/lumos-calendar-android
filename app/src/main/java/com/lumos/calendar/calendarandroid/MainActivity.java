package com.lumos.calendar.calendarandroid;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.dmfs.rfc5545.recur.InvalidRecurrenceRuleException;
import org.dmfs.rfc5545.recur.RecurrenceRule;
import org.dmfs.rfc5545.recur.RecurrenceRuleIterator;
import org.joda.time.DateTime;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.lumos.calendar.calendarandroid.util.EventListAdapter;
import com.lumos.calendar.calendarandroid.util.FetchData;
import com.yuyakaido.android.couplescalendar.model.CouplesCalendarEvent;
import com.yuyakaido.android.couplescalendar.model.Theme;
import com.yuyakaido.android.couplescalendar.ui.CouplesCalendarView;

import android.text.format.DateUtils;

public class MainActivity extends Activity implements CouplesCalendarView.OnMonthChangeListener, CouplesCalendarView.OnDateClickListener {

    private TextView currentMonth;
    private ListView mListView;
    public static ArrayList<JSONObject> eventsJsonObjects;
    public static ArrayList<SampleEvent> sampleEventList;
    private Button addEventButton, searchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addEventButton = findViewById(R.id.add_event_button);
        addEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Display the selected item text on TextView
                Intent activity = new Intent(MainActivity.this, EventFormActivity.class);
                activity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                MainActivity.this.startActivity(activity);
            }
        });

        searchButton = findViewById(R.id.search_button);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent activity = new Intent(MainActivity.this, SearchActivity.class);
                activity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                MainActivity.this.startActivity(activity);
            }
        });

        CouplesCalendarView view = findViewById(R.id.activity_main_couples_calendar_view);
        mListView = findViewById(R.id.listView);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get the selected item text from ListView
                SampleEvent selectedItem = (SampleEvent) parent.getItemAtPosition(position);

                // Display the selected item text on TextView
                Intent activity = new Intent(MainActivity.this, EventFormActivity.class);
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

                MainActivity.this.startActivity(activity);
            }
        });

        view.setOnMonthChangeListener(this);
        view.setOnDateClickListener(this);

        currentMonth = findViewById(R.id.current_month);

        int flags = DateUtils.FORMAT_SHOW_YEAR | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_NO_MONTH_DAY;
        Date month = new Date();
        currentMonth.setText(DateUtils.formatDateTime(this, month.getTime(), flags));

        FetchData fetchData = new FetchData();
        try {
            Void fetch_result = fetchData.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        view.setEvents(getEventsFromJsonData());

    }

    @Override
    public void onMonthChange(Date month) {
        int flags = DateUtils.FORMAT_SHOW_YEAR | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_NO_MONTH_DAY;
        setTitle(DateUtils.formatDateTime(this, month.getTime(), flags));
        currentMonth.setText(DateUtils.formatDateTime(this, month.getTime(), flags));
    }

    @Override
    public void onDateClick(Date date) {
        int flags = DateUtils.FORMAT_SHOW_YEAR | DateUtils.FORMAT_SHOW_DATE;

        EventListAdapter adapter = new EventListAdapter(this, R.layout.adapter_view_layout, getSpecificDateEvents(date));
        mListView.setAdapter(adapter);
    }

    private List<CouplesCalendarEvent> getEventsFromJsonData() {
        List<CouplesCalendarEvent> couplesCalendarEvents = new ArrayList<>();
        sampleEventList = new ArrayList<>();

        for (JSONObject eventJson : eventsJsonObjects) {
            SampleEvent temp = new SampleEvent();
            try {
                temp.setId(eventJson.getInt("id"));
                temp.setTitle(eventJson.getString("title"));
                temp.setDescription(eventJson.getString("description"));
                temp.setStartAt(new DateTime(eventJson.get("start_time")).toDate());
                temp.setEndAt(new DateTime(eventJson.get("end_time")).toDate());
                temp.setReminder(eventJson.getString("reminder"));
                temp.setEventColor(getResources().getColor(Theme.RED.getEventColorId()));
                temp.setRecurring(eventJson.getString("recurring"));
                if(!eventJson.isNull("recurring") && !eventJson.getString("recurring").equals("null") && !eventJson.getString("recurring").equals("")){
                    RecurrenceRule rule = new RecurrenceRule(eventJson.getString("recurring"));

                    org.dmfs.rfc5545.DateTime start = new org.dmfs.rfc5545.DateTime(new DateTime(eventJson.get("start_time")).getMillis());
                    org.dmfs.rfc5545.DateTime end = new org.dmfs.rfc5545.DateTime(new DateTime(eventJson.get("end_time")).getMillis());

                    RecurrenceRuleIterator itStart = rule.iterator(start);
                    RecurrenceRuleIterator itEnd = rule.iterator(end);

                    while (itStart.hasNext()) {
                        org.dmfs.rfc5545.DateTime nextInstanceStart = itStart.nextDateTime();
                        org.dmfs.rfc5545.DateTime nextInstanceEnd = itEnd.nextDateTime();
                        SampleEvent recurringEvent = (SampleEvent) temp.clone();
                        recurringEvent.setRecurring(eventJson.getString("recurring"));
                        recurringEvent.setStartAt(new DateTime(nextInstanceStart.getTimestamp()).toDate());
                        recurringEvent.setEndAt(new DateTime(nextInstanceEnd.getTimestamp()).toDate());
                        couplesCalendarEvents.add(recurringEvent);
                        sampleEventList.add(recurringEvent);
                    }
                }
                else{
                    couplesCalendarEvents.add(temp);
                    sampleEventList.add(temp);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (InvalidRecurrenceRuleException e) {
                e.printStackTrace();
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
        }
        return couplesCalendarEvents;
    }

    private ArrayList<SampleEvent> getSpecificDateEvents(Date selectedDate){
        ArrayList<SampleEvent> sampleEvents = new ArrayList<>();

        for(SampleEvent sampleEvent: sampleEventList){
            DateTime dateTimeSelected = new DateTime(selectedDate);
            DateTime dateTimeEvent = new DateTime(sampleEvent.getStartAt());
            if(dateTimeSelected.getDayOfYear() == dateTimeEvent.getDayOfYear()
                    && dateTimeSelected.getDayOfMonth() == dateTimeEvent.getDayOfMonth()
                    && dateTimeSelected.getDayOfWeek() == dateTimeEvent.getDayOfWeek()){
                sampleEvents.add(sampleEvent);
            }
        }

        return sampleEvents;
    }

    public static ArrayList<SampleEvent> searchEvent(String key){
        ArrayList<SampleEvent> sampleEvents = new ArrayList<>();

        for(SampleEvent sampleEvent: sampleEventList){
            if(sampleEvent.getDescription().toLowerCase().contains(key.toLowerCase()) || sampleEvent.getTitle().toLowerCase().contains(key.toLowerCase())) {
                sampleEvents.add(sampleEvent);
            }
        }

        return sampleEvents;
    }

}