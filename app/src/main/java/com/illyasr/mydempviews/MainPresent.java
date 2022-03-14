package com.illyasr.mydempviews;

import androidx.lifecycle.MutableLiveData;

import com.illyasr.mydempviews.base.BasePresenter;

public class MainPresent extends BasePresenter {
    public MutableLiveData<String> liveData = new MutableLiveData<>();

    public MutableLiveData<Boolean> maintitle = new MutableLiveData<>();

    public void it() {
        liveData.setValue("");
    }
}
