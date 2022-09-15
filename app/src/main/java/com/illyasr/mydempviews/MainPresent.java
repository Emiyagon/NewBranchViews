package com.illyasr.mydempviews;

import android.net.Uri;
import android.os.Environment;

import androidx.lifecycle.MutableLiveData;

import com.illyasr.mydempviews.base.BasePresenter;

public class MainPresent extends BasePresenter {
    public MutableLiveData<String> liveData = new MutableLiveData<>();

    public MutableLiveData<Boolean> maintitle = new MutableLiveData<>();

    public void it() {
        liveData.setValue("");
    }

    public String getRealFilePath(Uri uri ) {
        String path = uri.getPath();
        String[] pathArray = path.split(":");
        String fileName = pathArray[pathArray.length - 1];
        return Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + fileName;
    }
}
