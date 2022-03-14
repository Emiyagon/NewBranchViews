package com.illyasr.mydempviews.service;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

public class DeleteService extends IntentService {
    public DeleteService() {
        super("");
        // TODO Auto-generated constructor stub
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i("LOG", "===========deleteService");
    }
}
