package ru.kedrov.vkphotomaps.tasks;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ru.kedrov.vkphotomaps.photos.PhotoModel;

public class PhotoParseTask extends AsyncTask<Void, Integer, List<PhotoModel>> {

    private final String photosJSON;

    public PhotoParseTask(String photosJSON) {
        this.photosJSON = photosJSON;
    }

    @Override
    public List<PhotoModel> doInBackground(Void... params) {
        List<PhotoModel> photosList = new ArrayList<>();
        try {

            JSONArray photos
                    = new JSONObject(photosJSON).getJSONArray("response");

            if (photos.length() > 1) {

                int i;

                for (i = 1; i < photos.length(); i++) {

                    JSONObject photo = photos.getJSONObject(i);

                    PhotoModel photoModel
                            = new PhotoModel(photo.getString("src"),
                            photo.getString("src_big"),
                            photo.getDouble("lat"), photo.getDouble("long"));

                    photosList.add(photoModel);

                    publishProgress(i, photos.length());

                }

                Log.d("Photos parsed", "Total: " + i);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return photosList;
    }

}
