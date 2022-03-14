package com.illyasr.mydempviews.util;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Environment;
import android.widget.ImageView;
import android.widget.Toast;


import com.illyasr.mydempviews.R;

import java.io.File;
import java.io.FileOutputStream;

public abstract class SaveImageUtils extends AsyncTask {
    Activity mActivity;
    ImageView mImageView;
    public SaveImageUtils(Activity activity, ImageView imageView) {
        this.mImageView = imageView;
        this.mActivity = activity;
    }

    protected String doInBackground(Bitmap... params) {
        String result = mActivity.getResources().getString(R.string.save_picture_failed);
        try {
            String sdcard = Environment.getExternalStorageDirectory().toString();
            File file = new File(sdcard + "/123");
            if (!file.exists()) {
                file.mkdirs();
            }
            File imageFile = new File(file.getAbsolutePath(), "命名" + ".jpg");
            FileOutputStream outStream = null;
            outStream = new FileOutputStream(imageFile);
            Bitmap image = params[0];
            image.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
            outStream.flush();
            outStream.close();
            result = mActivity.getResources().getString(R.string.save_picture_success, file.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    protected void onPostExecute(String result) {
        Toast.makeText(mActivity, result, Toast.LENGTH_SHORT).show();
        mImageView.setDrawingCacheEnabled(false);
    }

    /*
    * new SaveImageUtils(ShareActivity.this, mScanCode).execute(imageBitmap);
    * */
}