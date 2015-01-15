package ru.kedrov.vkphotomaps.photos;

import android.os.AsyncTask;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import ru.kedrov.vkphotomaps.tasks.JSONFromLatLngTask;
import ru.kedrov.vkphotomaps.tasks.PhotoParseTask;

public class PhotoDownloader {

    public static List<PhotoModel> getPhotos(LatLng latLng) {

        JSONFromLatLngTask jsonTask = new JSONFromLatLngTask(latLng);

        String responseJSON = null;

        try {
            responseJSON = jsonTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        responseJSON = responseJSON != null ? responseJSON : JSONFromLatLngTask.DEFAULT_RESPONSE;

        PhotoParseTask parseTask = new PhotoParseTask
                (responseJSON);

        List<PhotoModel> photos = null;

        try {
            photos = parseTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        return photos != null ? photos : new ArrayList<PhotoModel>();
    }

}
