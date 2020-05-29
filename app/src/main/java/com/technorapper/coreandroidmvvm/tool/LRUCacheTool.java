package com.technorapper.coreandroidmvvm.tool;

import android.graphics.Bitmap;

import androidx.collection.LruCache;

import com.technorapper.coreandroidmvvm.network.model.flikr.Photo;
import com.technorapper.coreandroidmvvm.network.repository.LRUCacheRepository;

import org.jetbrains.annotations.NotNull;

import static com.technorapper.coreandroidmvvm.network.global.SystemVariable.CACHESIZE;

public class LRUCacheTool {
    public static LruCache<String, Bitmap> memoryCache;

    public LRUCacheTool() {

        memoryCache = new LruCache<String, Bitmap>(CACHESIZE) {
            @Override
            protected int sizeOf(@NotNull String key, @NotNull Bitmap bitmap) {
                // The cache size will be measured in kilobytes rather than
                // number of items.
                return bitmap.getByteCount() / 1024;
            }
        };

    }


    public static void addBitmapToMemoryCache(Integer farm, String server, String id, String secret, Bitmap bitmap) {
        if (bitmap != null) {
            if (getBitmapFromMemCache(farm, server, id, secret) == null) {
                memoryCache.put(farm + server + id + secret, bitmap);
            }
        }
    }


    public static Bitmap getBitmapFromMemCache(final Integer farm, final String server, final String id, final String secret) {

        return memoryCache.get(farm + server + id + secret);
    }

    public static Bitmap getBitmapFromMemCacheOrDownload(Photo photo) {
        Bitmap bitmap = memoryCache.get(photo.getFarm() + photo.getServer() + photo.getId() + photo.getSecret());
        if (bitmap == null) {

            try {
                LRUCacheRepository.downloadThisImageWithOwnThread(photo);
            } catch (Exception e) {
                return null;
            }
            return null;
        } else
            return bitmap;
    }
}

