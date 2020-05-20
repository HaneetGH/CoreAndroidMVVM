package com.technorapper.coreandroidmvvm.network.api;

import android.os.AsyncTask;

import com.technorapper.coreandroidmvvm.helper.EventTask;
import com.technorapper.coreandroidmvvm.helper.Task;

import org.json.JSONObject;

public class ApiCall {
    public static final String BASEURL = "https://api.flickr.com/services/";
    public static final String IMAGELIST = "rest/?method=flickr.photos.search&api_key=3e7cc266ae2b0e0d78e279ce8e361736&format=json&nojsoncallback=1&safe_search=1&tags=kitten&per_page=10&page=1";


}
