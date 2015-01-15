package ru.kedrov.vkphotomaps.photos;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class URLBitmapDescriptorFutureFactory {

    private final ExecutorService executor = Executors.newCachedThreadPool();

    public Future<BitmapDescriptor> fromURL(final String src) {
        return executor.submit(new Callable<BitmapDescriptor>() {

            @Override
            public BitmapDescriptor call() throws Exception {
                URL url = null;
                try {
                    url = new URL(src);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                HttpURLConnection urlConnection = null;
                Bitmap bitmap = null;
                try {
                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setDoInput(true);
                    urlConnection.connect();
                    InputStream input = urlConnection.getInputStream();
                    bitmap = BitmapFactory.decodeStream(input);
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }
                }
                return bitmap != null ?
                        BitmapDescriptorFactory.fromBitmap(bitmap) : null;
            }

        });

    }


}
