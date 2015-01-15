package ru.kedrov.vkphotomaps.tasks;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class JSONFromLatLngTask extends AsyncTask<Void, Void, String> {

    public static final String DEFAULT_RESPONSE = "{\"response\": [0]}";
    private final LatLng latLng;

    public JSONFromLatLngTask(LatLng latLng) {
        this.latLng = latLng;
    }

    @Override
    public String doInBackground(Void... params) {
        URL url = null;

        try {

            Log.d("Requesting JSON", "lat=" + latLng.latitude + ", lng=" + latLng.longitude);

            url = new URL(String
                    .format("https://api.vk.com/method/photos.search?lat=%s&long=%s&radius=%s"
                            , latLng.latitude, latLng.longitude, 100));

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        HttpURLConnection urlConnection = null;

        BufferedReader reader;

        String resultJson = null;

        try {

            StringBuilder builder = new StringBuilder();

            urlConnection = (HttpURLConnection) url.openConnection();

            InputStreamReader inputStreamReader = new InputStreamReader(urlConnection.getInputStream());

            reader = new BufferedReader(inputStreamReader);

            String line;

            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }

            resultJson = builder.toString();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            urlConnection.disconnect();
        }

        return resultJson;
    }

}
