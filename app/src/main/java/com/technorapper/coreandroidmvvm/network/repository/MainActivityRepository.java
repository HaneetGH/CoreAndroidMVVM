package com.technorapper.coreandroidmvvm.network.repository;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import androidx.collection.LruCache;
import androidx.lifecycle.MutableLiveData;

import com.technorapper.coreandroidmvvm.helper.EventTask;
import com.technorapper.coreandroidmvvm.helper.Task;
import com.technorapper.coreandroidmvvm.network.api.ApiCall;
import com.technorapper.coreandroidmvvm.network.model.flikr.FlikrImageModel;
import com.technorapper.coreandroidmvvm.network.model.flikr.Photo;
import com.technorapper.coreandroidmvvm.network.model.flikr.Photos;
import com.technorapper.coreandroidmvvm.network.tool.JSONParser;
import com.technorapper.coreandroidmvvm.tool.LRUCacheTool;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivityRepository {


    public static EventTask getNextImage(Photo photo) {
        return EventTask.success(LRUCacheTool.getBitmapFromMemCacheOrDownload(photo), Task.LRUBITMAP);

    }


    public static FlikrImageModel processImageListRequest() {
        FlikrImageModel data = new FlikrImageModel();
        Photos photos = new Photos();
        Photo photo;
        List<Photo> photoList = new ArrayList<>();
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = jsonParser.getJSONFromUrl(ApiCall.BASEURL + ApiCall.IMAGELIST);
        try {


            Log.d("response", jsonObject.toString());
            JSONObject jsonObjPhotos = jsonObject.getJSONObject("photos");
            photos.setPage(jsonObjPhotos.getInt("page"));
            photos.setPages(jsonObjPhotos.getInt("pages"));
            photos.setTotal(jsonObjPhotos.getString("total"));
            photos.setPerpage(jsonObjPhotos.getInt("perpage"));

            JSONArray jsonArrayPhoto = jsonObjPhotos.getJSONArray("photo");
            for (int i = 0; i < jsonArrayPhoto.length() - 1; i++) {
                JSONObject JsonObj = jsonArrayPhoto.getJSONObject(i);
                photo = new Photo();
                photo.setId(JsonObj.getString("id"));

                photo.setFarm(JsonObj.getInt("farm"));
                photo.setIsfamily(JsonObj.getInt("isfamily"));
                photo.setIspublic(JsonObj.getInt("ispublic"));
                photo.setIsfriend(JsonObj.getInt("isfriend"));

                photo.setOwner(JsonObj.getString("owner"));
                photo.setSecret(JsonObj.getString("secret"));
                photo.setServer(JsonObj.getString("server"));
                photo.setTitle(JsonObj.getString("title"));
                photoList.add(photo);
            }
            photos.setPhoto(photoList);
            data.setPhotos(photos);
            return data;
        } catch (Exception e) {
            return null;
        }

    }

    public static Bitmap downloadThisImage(Photo photo) throws MalformedURLException {

        final URL url = new URL("https://farm" + photo.getFarm() + ".staticflickr.com/" + photo.getServer() + "/" + photo.getId() + "_" + photo.getSecret() + "_q.jpg");
        Log.d("URL", url.toString());
        Bitmap image = null;
        try {
            image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            return image;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


}
