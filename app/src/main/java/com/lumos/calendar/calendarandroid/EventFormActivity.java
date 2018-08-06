package com.lumos.calendar.calendarandroid;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;

public class EventFormActivity extends Activity {

    private EditText title, description, start_time, recurring, reminder;
    private String startTimeDate, startTimeClock;
    private Button setStartTime, updateOrCreateEvent, deleteEvent;
    private final String baseUrl = "https://lumos-calendar-api.herokuapp.com/calendar/api/v1/events/";
    private int eventId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_form);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        title = findViewById(R.id.event_title);
        description = findViewById(R.id.event_description);
        start_time = findViewById(R.id.event_start_time);
        recurring = findViewById(R.id.event_recurring);
        reminder = findViewById(R.id.event_reminder);
        setStartTime = findViewById(R.id.select_start_time);
        updateOrCreateEvent = findViewById(R.id.add_or_update_event_button);
        deleteEvent = findViewById(R.id.delete_event_button);
        updateOrCreateEvent.setText("Add Event");
        deleteEvent.setVisibility(View.INVISIBLE);

        updateOrCreateEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    URL url = new URL(baseUrl);
                    HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
                    httpCon.setDoOutput(true);
                    httpCon.setRequestProperty("Content-Type", "application/x-www-form-urlencoded" );
                    httpCon.setRequestMethod("POST");
                    httpCon.setRequestProperty("Content-Type","application/json");
                    JSONObject eventObject = new JSONObject();
                    eventObject.put("title",title.getText().toString());
                    eventObject.put("description", description.getText().toString());
                    eventObject.put("recurring", recurring.getText().toString());
                    eventObject.put("start_time", "2018-08-13T01:00:00.000Z");
                    eventObject.put("end_time", "2018-08-13T01:00:00.000Z");
                    eventObject.put("reminder", reminder.getText().toString());
                    eventObject.put("user_id", 1);
                    String json = eventObject.toString();

                    byte[] outputInBytes = json.getBytes("UTF-8");
                    OutputStream os = httpCon.getOutputStream();
                    os.write( outputInBytes );
                    os.close();

                    int responseCode = httpCon.getResponseCode();
                    System.out.println("response code: " + responseCode);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Intent activity = new Intent(EventFormActivity.this, MainActivity.class);
                activity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                EventFormActivity.this.startActivity(activity);
            }
        });

        try{ //if event will be added
            Bundle b = getIntent().getExtras();
            if (!b.isEmpty()) {
                System.out.println("okokok");
                updateOrCreateEvent.setText("Update Event");
                deleteEvent.setVisibility(View.VISIBLE);
                eventId = b.getInt("id");

                title.setText(b.getString("title"));
                description.setText(b.getString("description"));

                DateTimeFormatter dtfOut = DateTimeFormat.forPattern("MM/dd/yyyy HH:mm:ss");

                DateTime jodaStartTime = new DateTime((b.getLong("start_time")));

                start_time.setText(dtfOut.print(jodaStartTime));

                recurring.setText(b.getString("recurring"));
                reminder.setText(b.getString("reminder"));

                updateOrCreateEvent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            URL url = new URL(baseUrl + "" + eventId);
                            HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
                            httpCon.setDoOutput(true);
                            httpCon.setRequestProperty("Content-Type", "application/x-www-form-urlencoded" );
                            httpCon.setRequestMethod("PUT");
                            httpCon.setRequestProperty("Content-Type","application/json");
                            JSONObject eventObject = new JSONObject();
                            eventObject.put("title",title.getText().toString());
                            eventObject.put("description", description.getText().toString());
                            eventObject.put("recurring", recurring.getText().toString());
                            eventObject.put("start_time", "2018-08-13T01:00:00.000Z");
                            eventObject.put("end_time", "2018-08-13T01:00:00.000Z");
                            eventObject.put("reminder", reminder.getText().toString());
                            eventObject.put("user_id", 1);
                            String json = eventObject.toString();

                            byte[] outputInBytes = json.getBytes("UTF-8");
                            OutputStream os = httpCon.getOutputStream();
                            os.write( outputInBytes );
                            os.close();

                            int responseCode = httpCon.getResponseCode();
                            System.out.println("response code: " + responseCode);
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Intent activity = new Intent(EventFormActivity.this, MainActivity.class);
                        activity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        EventFormActivity.this.startActivity(activity);
                    }
                });

                deleteEvent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        System.out.println("***id:" + eventId);
                        try {
                            URL url = new URL(baseUrl + "" + eventId);
                            HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
                            httpCon.setDoOutput(true);
                            httpCon.setRequestProperty("Content-Type", "application/x-www-form-urlencoded" );
                            httpCon.setRequestMethod("DELETE");
                            int responseCode = httpCon.getResponseCode();
                            httpCon.connect();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Intent activity = new Intent(EventFormActivity.this, MainActivity.class);
                        activity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        EventFormActivity.this.startActivity(activity);
                    }
                });
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        start_time.setKeyListener(null);
        setStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar takvim = Calendar.getInstance();
                int yil = takvim.get(Calendar.YEAR);
                int ay = takvim.get(Calendar.MONTH);
                int gun = takvim.get(Calendar.DAY_OF_MONTH);
                int hour, minute;

                final Calendar c = Calendar.getInstance();
                hour = c.get(Calendar.HOUR_OF_DAY);
                minute = c.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(EventFormActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {
                                startTimeClock = hourOfDay + ":" + minute;
                                start_time.setText(startTimeDate + " " +  startTimeClock);
                            }
                        }, hour, minute, false);
                timePickerDialog.show();

                DatePickerDialog dpd = new DatePickerDialog(EventFormActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                month += 1;
                                startTimeDate = dayOfMonth + "/" + month + "/" + year;
                            }
                        }, yil, ay, gun);

                dpd.setButton(DatePickerDialog.BUTTON_POSITIVE, "Select", dpd);
                dpd.setButton(DatePickerDialog.BUTTON_NEGATIVE, "Cancel", dpd);
                dpd.show();
            }
        });
    }

}
