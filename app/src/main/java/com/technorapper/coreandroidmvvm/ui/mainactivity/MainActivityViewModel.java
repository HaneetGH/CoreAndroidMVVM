package com.technorapper.coreandroidmvvm.ui.mainactivity;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.technorapper.coreandroidmvvm.helper.EventTask;
import com.technorapper.coreandroidmvvm.network.model.flikr.Photo;
import com.technorapper.coreandroidmvvm.network.repository.MainActivityRepository;

import java.util.List;

public class MainActivityViewModel extends ViewModel {
    public MutableLiveData<EventTask> eventTaskMutableLiveData = new MutableLiveData<>();

    protected void getListImages() {
        MainActivityRepository.getListOfImages(eventTaskMutableLiveData);
    }

    protected void downloadAll(List<Photo> photos) {
        if (photos == null)
            return;
        MainActivityRepository.downloadAllImages(photos, eventTaskMutableLiveData);
    }

    protected void getBitmap(Photo photo) {
        if (photo == null)
            return;
        MainActivityRepository.getNextImage(photo, eventTaskMutableLiveData);
    }
}
