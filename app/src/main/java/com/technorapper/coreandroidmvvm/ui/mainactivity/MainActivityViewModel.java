package com.technorapper.coreandroidmvvm.ui.mainactivity;

import android.os.AsyncTask;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.technorapper.coreandroidmvvm.helper.EventTask;
import com.technorapper.coreandroidmvvm.helper.Task;
import com.technorapper.coreandroidmvvm.network.model.flikr.FlikrImageModel;
import com.technorapper.coreandroidmvvm.network.model.flikr.Photo;
import com.technorapper.coreandroidmvvm.network.repository.MainActivityRepository;

import java.util.List;

public class MainActivityViewModel extends ViewModel {
    MutableLiveData<EventTask> eventTaskMutableLiveData = new MutableLiveData<>();


    void downloadAll(List<Photo> photos) {
        if (photos == null)
            return;
        MainActivityRepository.downloadAllImages(photos, eventTaskMutableLiveData);
    }

    void getBitmap(Photo photo) {
        if (photo == null)
            return;
        MainActivityRepository.getNextImage(photo, eventTaskMutableLiveData);
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
