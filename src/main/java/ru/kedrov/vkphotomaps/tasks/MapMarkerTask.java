package ru.kedrov.vkphotomaps.tasks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.view.Gravity;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import ru.kedrov.vkphotomaps.R;
import ru.kedrov.vkphotomaps.photos.PhotoDownloader;
import ru.kedrov.vkphotomaps.photos.PhotoModel;
import ru.kedrov.vkphotomaps.photos.URLBitmapDescriptorFutureFactory;

public class MapMarkerTask extends AsyncTask<Void, Integer, Map<MarkerOptions, Future<BitmapDescriptor>>> {

    private final GoogleMap map;
    private final LatLng latLng;
    private final Activity context;
    private final ProgressDialog dialog;

    public MapMarkerTask(GoogleMap map, LatLng latLng, Activity context) {
        this.map = map;
        this.latLng = latLng;
        this.context = context;
        this.dialog = new ProgressDialog(context);
    }

    @Override
    public void onPreExecute() {

        this.dialog.setMessage(context.getString(R.string.downloading_dialog_message));
        this.dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        this.dialog.getWindow().setGravity(Gravity.BOTTOM);
        this.dialog.setCanceledOnTouchOutside(true);
        this.dialog.show();

    }

    @Override
    public Map<MarkerOptions, Future<BitmapDescriptor>> doInBackground(Void... params) {

        Map<MarkerOptions, Future<BitmapDescriptor>> futuresMap = new HashMap<>();

        List<PhotoModel> photos = PhotoDownloader.getPhotos(latLng);

        URLBitmapDescriptorFutureFactory bitmapDescriptorFactory = new URLBitmapDescriptorFutureFactory();

        int i = 0;

        for (PhotoModel photo : photos) {

            LatLng photoPosition = new LatLng(photo.lat, photo.lon);

            MarkerOptions marker = new MarkerOptions()
                    .position(photoPosition).title(photo.largeLink).flat(true);

            Future<BitmapDescriptor> future = bitmapDescriptorFactory.fromURL(photo.smallLink);

            futuresMap.put(marker, future);

            publishProgress(++i, photos.size());

        }

        return futuresMap;
    }

    @Override
    public void onPostExecute(Map<MarkerOptions, Future<BitmapDescriptor>> futuresMap) {

        map.addCircle(new CircleOptions().
                center(latLng).radius(420).strokeWidth(5).strokeColor(Color.argb(20, 6, 135, 205)));

        Set<Map.Entry<MarkerOptions, Future<BitmapDescriptor>>> entries = futuresMap.entrySet();

        for (Map.Entry<MarkerOptions, Future<BitmapDescriptor>> entry : entries) {
            try {
                MarkerOptions marker = entry.getKey();

                BitmapDescriptor icon = entry.getValue().get();

                map.addMarker(marker.icon(icon));

            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

        this.dialog.dismiss();
    }

    @Override
    public void onProgressUpdate(Integer... values) {
        this.dialog.setMax(values[1]);
        this.dialog.setProgress(values[0]);
    }
}
