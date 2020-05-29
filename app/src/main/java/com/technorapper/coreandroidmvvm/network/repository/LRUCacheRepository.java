package com.technorapper.coreandroidmvvm.network.repository;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import com.technorapper.coreandroidmvvm.network.model.flikr.Photo;
import com.technorapper.coreandroidmvvm.tool.LRUCacheTool;

import java.io.IOException;
import java.net.URL;

public class LRUCacheRepository {

    public static void downloadThisImageWithOwnThread(final Photo photo) {

        new AsyncTask<String, String, Bitmap>() {


            @Override
            protected Bitmap doInBackground(String... strings) {
                Bitmap image = null;
                try {
                    final URL url = new URL("https://farm" + photo.getFarm() + ".staticflickr.com/" + photo.getServer() + "/" + photo.getId() + "_" + photo.getSecret() + "_q.jpg");
                    Log.d("URL", url.toString());
                    image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
                return image;
            }

            @Override
            protected void onPostExecute(Bitmap s) {
                super.onPostExecute(s);
                LRUCacheTool.addBitmapToMemoryCache(photo.getFarm(), photo.getServer(), photo.getId(), photo.getSecret(), s);
            }
        }.execute();


    }
}
