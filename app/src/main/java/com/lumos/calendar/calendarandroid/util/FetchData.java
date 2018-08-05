package com.lumos.calendar.calendarandroid.util;

import android.os.AsyncTask;

import com.lumos.calendar.calendarandroid.MainActivity;
import com.lumos.calendar.calendarandroid.SampleEvent;
import com.yuyakaido.android.couplescalendar.model.Theme;

import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class FetchData extends AsyncTask<Void, Void, Void>{

    @Override
    protected Void doInBackground(Void... voids) {
        try {
            System.out.println("objects fecthing");
            URL getEventsUrl = new URL("https://lumos-calendar-api.herokuapp.com/calendar/api/v1/events");

            HttpURLConnection httpURLConnection = (HttpURLConnection) getEventsUrl.openConnection();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder builder = new StringBuilder();

            String line = "";
            while (line != null) {
                line = bufferedReader.readLine();
                builder.append(line);
            }

            JSONArray result = new JSONArray(builder.toString());

            MainActivity.eventsJsonObjects = new ArrayList<>();

            for(int i = 0; i < result.length(); i++) {
                MainActivity.eventsJsonObjects.add(result.getJSONObject(i));
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }
}
