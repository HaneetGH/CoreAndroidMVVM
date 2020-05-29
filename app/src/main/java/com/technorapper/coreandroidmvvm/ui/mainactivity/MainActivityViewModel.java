package com.technorapper.coreandroidmvvm.ui.mainactivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.technorapper.coreandroidmvvm.helper.EventTask;
import com.technorapper.coreandroidmvvm.helper.Task;
import com.technorapper.coreandroidmvvm.network.model.flikr.FlikrImageModel;
import com.technorapper.coreandroidmvvm.network.model.flikr.Photo;
import com.technorapper.coreandroidmvvm.network.repository.MainActivityRepository;
import com.technorapper.coreandroidmvvm.tool.LRUCacheTool;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class MainActivityViewModel extends ViewModel {
    MutableLiveData<EventTask> eventTaskMutableLiveData = new MutableLiveData<>();
    private static LRUCacheTool lruCacheTool = new LRUCacheTool();


    void downloadImageThread(final List<Photo> photos) {

        new AsyncTask<String, String, String>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                eventTaskMutableLiveData.postValue(EventTask.loading(Task.DOWNLOADING));
            }

            @Override
            protected String doInBackground(String... params) {
                boolean isForFirstImage = true;
                for (final Photo photo : photos) {
                    try {
                        Bitmap image = MainActivityRepository.downloadThisImage(photo);
                        // Parse the JSON using the library of your choice
                        lruCacheTool.addBitmapToMemoryCache(photo.getFarm(), photo.getServer(), photo.getId(), photo.getSecret(), image);
                        if (isForFirstImage) {
                            eventTaskMutableLiveData.postValue(EventTask.message(photo, Task.FIRSTIMAGE));
                            isForFirstImage = false;
                        }
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

    void getBitmap(Photo photo) {
        if (photo == null)
            return;
        eventTaskMutableLiveData.postValue(MainActivityRepository.getNextImage(photo));
    }


    void getAllListImages() {
        new AsyncTask<String, Void, FlikrImageModel>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                eventTaskMutableLiveData.postValue(EventTask.loading(Task.FLIKRURL));
            }

            @Override
            protected FlikrImageModel doInBackground(String... params) {
                FlikrImageModel data = new FlikrImageModel();
                data = MainActivityRepository.processImageListRequest();
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
}
