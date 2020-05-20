package com.technorapper.coreandroidmvvm.network.repository;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.JsonReader;
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
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.technorapper.coreandroidmvvm.network.global.SystemVariable.CONNECTION_TIMEOUT;
import static com.technorapper.coreandroidmvvm.network.global.SystemVariable.READ_TIMEOUT;
import static com.technorapper.coreandroidmvvm.network.global.SystemVariable.REQUEST_METHOD;

public class MainActivityRepository {
    private LruCache<String, Bitmap> memoryCache;
    private static LRUCacheTool lruCacheTool = new LRUCacheTool();

    public static void getListOfImages(final MutableLiveData<EventTask> eventTaskMutableLiveData) {

        new AsyncTask<String, Void, FlikrImageModel>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                eventTaskMutableLiveData.postValue(EventTask.loading(Task.FLIKRURL));
            }

            @Override
            protected FlikrImageModel doInBackground(String... params) {
                FlikrImageModel data = new FlikrImageModel();
                Photos photos = new Photos();
                Photo photo = new Photo();
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

                } catch (Exception e) {

                }

                // Parse the JSON using the library of your choice
                return data;
            }

            @Override
            protected void onCancelled() {
                super.onCancelled();
                eventTaskMutableLiveData.postValue(EventTask.error(Task.FLIKRURL));
            }

            @Override
            protected void onPostExecute(FlikrImageModel data) {
                super.onPostExecute(data);
                eventTaskMutableLiveData.postValue(EventTask.success(data, Task.FLIKRURL));
            }
        }.execute();
    }

    public static void downloadAllImages(final List<Photo> photos, final MutableLiveData<EventTask> eventTaskMutableLiveData) {
        eventTaskMutableLiveData.postValue(EventTask.loading(Task.DOWNLOADING));

        new AsyncTask<String, String, String>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                eventTaskMutableLiveData.postValue(EventTask.loading(Task.DOWNLOADING));
            }

            @Override
            protected String doInBackground(String... params) {
                for (final Photo photo : photos) {
                    try {
                        final URL url = new URL("https://farm" + photo.getFarm() + ".staticflickr.com/" + photo.getServer() + "/" + photo.getId() + "_" + photo.getSecret() + "_q.jpg");
                        Log.d("URL", url.toString());
                        Bitmap image = null;
                        try {
                            image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        // Parse the JSON using the library of your choice
                        lruCacheTool.addBitmapToMemoryCache(photo.getFarm(), photo.getServer(), photo.getId(), photo.getSecret(), image);
                    } catch (Exception e) {

                    }
                }
                return "complete";
            }

            @Override
            protected void onCancelled() {
                super.onCancelled();
                eventTaskMutableLiveData.postValue(EventTask.error(Task.DOWNLOADING));
            }

            @Override
            protected void onPostExecute(String data) {
                super.onPostExecute(data);

                eventTaskMutableLiveData.postValue(EventTask.success(data, Task.DOWNLOADING));

            }
        }.execute();


    }


    public static void getNextImage(Photo photo, MutableLiveData<EventTask> eventTaskMutableLiveData) {

        eventTaskMutableLiveData.postValue(EventTask.success(lruCacheTool.getBitmapFromMemCacheOrDownload(photo.getFarm(), photo.getServer(), photo.getId(), photo.getSecret()), Task.LRUBITMAP));
    }


}
