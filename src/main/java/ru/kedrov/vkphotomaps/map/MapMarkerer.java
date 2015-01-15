package ru.kedrov.vkphotomaps.map;

import android.app.Activity;
import android.os.AsyncTask;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

import ru.kedrov.vkphotomaps.tasks.MapMarkerTask;

public class MapMarkerer {

    public static void markAround(GoogleMap map, LatLng latLng, Activity context) {
        new MapMarkerTask(map, latLng, context).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

}
