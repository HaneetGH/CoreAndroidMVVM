package com.technorapper.coreandroidmvvm.tool;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import androidx.collection.LruCache;

import com.technorapper.coreandroidmvvm.helper.EventTask;
import com.technorapper.coreandroidmvvm.helper.Task;
import com.technorapper.coreandroidmvvm.network.model.flikr.Photo;

import java.io.IOException;
import java.net.URL;

import static com.technorapper.coreandroidmvvm.network.global.SystemVariable.CACHESIZE;

public class LRUCacheTool {
    private LruCache<String, Bitmap> memoryCache;

    public LRUCacheTool() {

        memoryCache = new LruCache<String, Bitmap>(CACHESIZE) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                // The cache size will be measured in kilobytes rather than
                // number of items.
                return bitmap.getByteCount() / 1024;
            }
        };

    }


    public void addBitmapToMemoryCache(Integer farm, String server, String id, String secret, Bitmap bitmap) {
        if (bitmap != null) {
            if (getBitmapFromMemCache(farm, server, id, secret) == null) {
                memoryCache.put(farm + server + id + secret, bitmap);
            }
        }
    }

    public Bitmap getBitmapFromMemCacheOrDownload(final Integer farm, final String server, final String id, final String secret) {
        Bitmap bitmap = memoryCache.get(farm + server + id + secret);
        if (bitmap == null) {


            new AsyncTask<String, String, Bitmap>() {
                @Override
                protected Bitmap doInBackground(String... params) {
                    Bitmap image = null;
                    try {
                        URL url = new URL("https://farm" + farm + ".staticflickr.com/" + server + "/" + id + "_" + secret + "_q.jpg");
                        image = BitmapFactory.decodeStream(url.openConnection().getInputStream());

                    } catch (IOException e) {
                        System.out.println(e);
                    }
                    // Parse the JSON using the library of your choice
                    return image;
                }

                @Override
                protected void onPostExecute(Bitmap image) {
                    super.onPostExecute(image);
                    addBitmapToMemoryCache(farm, server, id, secret, image);

                }
            }.execute();

            return null;
        } else
            return bitmap;
    }

    public Bitmap getBitmapFromMemCache(final Integer farm, final String server, final String id, final String secret) {
        Bitmap bitmap = memoryCache.get(farm + server + id + secret);

        return bitmap;
    }
}
