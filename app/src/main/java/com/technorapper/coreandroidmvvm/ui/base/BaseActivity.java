package com.technorapper.coreandroidmvvm.ui.base;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


public abstract class BaseActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBinding();
        attachViewModel();
    }

    protected abstract void setBinding();

    protected abstract void attachViewModel();
}
