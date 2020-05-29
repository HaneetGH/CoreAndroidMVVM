package com.technorapper.coreandroidmvvm.ui.mainactivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.technorapper.coreandroidmvvm.R;
import com.technorapper.coreandroidmvvm.databinding.ActivityMainBinding;
import com.technorapper.coreandroidmvvm.helper.EventTask;
import com.technorapper.coreandroidmvvm.helper.Task;
import com.technorapper.coreandroidmvvm.network.model.flikr.FlikrImageModel;
import com.technorapper.coreandroidmvvm.ui.base.BaseActivity;

public class MainActivity extends BaseActivity {
    private ActivityMainBinding binding;
    private MainActivityViewModel viewModel;
    FlikrImageModel flikrImageModel = new FlikrImageModel();
    int selectedPhoto = -1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void setBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setHandler(new ClickHandler());
        binding.setPrevDisable("Disable");
        binding.setNextDisable("");
    }

    @Override
    protected void attachViewModel() {
        viewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);
        viewModel.getAllListImages();
        binding.setImage(getResources().getDrawable(R.drawable.loader));
        viewModel.eventTaskMutableLiveData.observe(this, new Observer<EventTask>() {
            @Override
            public void onChanged(EventTask eventTask) {
                switch (eventTask.status) {
                    case LOADING:
                        binding.progressCircular.show();
                        Log.d("Task", "Loading");
                        break;
                    case SUCCESS:
                        binding.progressCircular.hide();
                        if (eventTask.data != null) {
                            if (eventTask.task == Task.FLIKRURL) {
                                flikrImageModel = (FlikrImageModel) eventTask.data;

                                viewModel.downloadAll(flikrImageModel.getPhotos().getPhoto());
                            }
                            if (eventTask.task == Task.DOWNLOADING) {
                                Log.d("Downloading Complete", "########");
                            }
                            if (eventTask.task == Task.FIRSTIMAGE) {
                                ClickHandler clickHandler = new ClickHandler();
                                clickHandler.next();
                                Log.d("Downloading Complete", "########");
                            }
                            if (eventTask.task == Task.LRUBITMAP) {
                                binding.setImage(new BitmapDrawable(getResources(), (Bitmap) eventTask.data));
                                Log.d("BITMAP", "########");
                            }
                        }
                        break;
                    case ERROR:
                        binding.progressCircular.hide();
                        Log.d("Task", "error");
                        break;
                }


            }
        });
    }

    public class ClickHandler {

        public void next() {
            if (flikrImageModel == null || flikrImageModel.getPhotos() == null || flikrImageModel.getPhotos().getPhoto() == null)
                return;
            ++selectedPhoto;
            if (selectedPhoto > flikrImageModel.getPhotos().getPhoto().size() - 1) {
                selectedPhoto = flikrImageModel.getPhotos().getPhoto().size() - 1;
                binding.setPrevDisable("");
                binding.setNextDisable("Disable");

            } else {
                binding.setImage(getResources().getDrawable(R.drawable.loader));
                viewModel.getBitmap(flikrImageModel.getPhotos().getPhoto().get(selectedPhoto));
            }

        }

        public void previous() {
            --selectedPhoto;
            if (flikrImageModel == null || flikrImageModel.getPhotos() == null || flikrImageModel.getPhotos().getPhoto() == null)
                return;
            if (selectedPhoto < 0) {
                selectedPhoto = -1;
                binding.setPrevDisable("Disable");
                binding.setNextDisable("");
            } else {
                binding.setImage(getResources().getDrawable(R.drawable.loader));
                viewModel.getBitmap(flikrImageModel.getPhotos().getPhoto().get(selectedPhoto));
            }
        }
    }
}
