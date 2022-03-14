package com.illyasr.mydempviews.util;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.illyasr.mydempviews.MyApplication;
import com.illyasr.mydempviews.R;
import com.jeremyliao.liveeventbus.LiveEventBus;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 *   下载视频和图片到本地的方法
 */
public class DownloadUtil{
    public static void saveImg(Activity activity,String url) {
        Glide.get(activity).clearMemory();
        Glide.with(activity)
                .asBitmap()
                .load(url)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap bitmap, @Nullable Transition<? super Bitmap> transition) {
                        saveToSystemGallery(bitmap);
                    }//

                });
    }

    public static void saveToSystemGallery(Bitmap bmp) {
        // 首先保存图片
        File fileDir = new File(Environment.getExternalStorageDirectory(),
//                SdPathConfig.SAVE_IMG_PATH
                "/Chaoqi"
        );
        if (!fileDir.exists()) {
            fileDir.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(fileDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 其次把文件插入到系统图库
        try {
            MediaStore.Images.Media.insertImage(MyApplication.getInstance().getContentResolver(),
                    file.getAbsolutePath(), fileName, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // 最后通知图库更新
        //sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse(file.getAbsolutePath())));
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri uri = Uri.fromFile(file);
        intent.setData(uri);
        MyApplication.getInstance().sendBroadcast(intent);
        //图片保存成功，图片路径：
        Toast.makeText(MyApplication.getInstance(),
                "图片保存路径：" + file.getAbsolutePath(), Toast.LENGTH_SHORT).show();
    }


//    private OnDownloadListener onDownloadListener;
    public interface OnDownloadListener{
        void onSuccess(String s);
        void onFailed(String s);
    }
    public static void downMp4(Context context, final String url,OnDownloadListener onDownloadListener) {

        Toast.makeText(MyApplication.getInstance(),"准备下载",Toast.LENGTH_SHORT).show();
        final ProgressDialog pd; // 进度条对话框
        pd = new ProgressDialog(context);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        //正在下载更新
        pd.setMessage("下载中...");
        pd.setCanceledOnTouchOutside(false);
        pd.show();
        new Thread() {
            @Override
            public void run() {
                try {
//                    File file = getFileFromServer("http://img.tukuppt.com/video_show/3987418/00/02/84/5b9556857f5d9.mp4", pd);
                    File file = getFileFromServer(url, pd);
                    MyApplication.getInstance().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + file)));
                    //sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse(String.valueOf(file))));
//                    //获取ContentResolve对象，来操作插入视频
//                    ContentResolver localContentResolver = getContentResolver();
//                    //ContentValues：用于储存一些基本类型的键值对
//                    ContentValues localContentValues = getVideoContentValues(MainActivity.this, file, System.currentTimeMillis());
//                    //insert语句负责插入一条新的纪录，如果插入成功则会返回这条记录的id，如果插入失败会返回-1。
//                    Uri localUri = localContentResolver.insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, localContentValues);
                    sleep(1000);
                    pd.dismiss(); // 结束掉进度条对话框
                    File sd1 = Environment.getExternalStorageDirectory();
                    String path1 = sd1.getPath() + "/"+context.getResources().getString(R.string.app_name);
//                    Toast.makeText(MyApplication.getInstance(),"下载完成!文件保存在"+path1+"文件夹下!",Toast.LENGTH_SHORT).show();
//                    LiveEventBus.get().with("download").post("下载完成!文件保存在"+path1+"文件夹下!");
                    onDownloadListener.onSuccess("下载完成!文件保存在"+path1+"文件夹下!");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    /**
     * 视频存在本地
     *
     * @param paramContext
     * @param paramFile
     * @param paramLong
     * @return
     */
    public static ContentValues getVideoContentValues(Context paramContext, File paramFile, long paramLong) {
        ContentValues localContentValues = new ContentValues();
        localContentValues.put("title", paramFile.getName());
        localContentValues.put("_display_name", paramFile.getName());
        localContentValues.put("mime_type", "video/3gp");
        localContentValues.put("datetaken", Long.valueOf(paramLong));
        localContentValues.put("date_modified", Long.valueOf(paramLong));
        localContentValues.put("date_added", Long.valueOf(paramLong));
        localContentValues.put("_data", paramFile.getAbsolutePath());
        localContentValues.put("_size", Long.valueOf(paramFile.length()));
        return localContentValues;
    }

    public static File getFileFromServer(String path, ProgressDialog pd) throws Exception {
        // 如果相等的话表示当前的sdcard挂载在手机上并且是可用的
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            URL url = new URL(path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            // 获取到文件的大小
            pd.setMax(conn.getContentLength());
            InputStream is = conn.getInputStream();
            File sd1 = Environment.getExternalStorageDirectory();
            String path1 = sd1.getPath() + "/lfmf";
            File myfile1 = new File(path1);
            if (!myfile1.exists()) {
                myfile1.mkdir();
            }
            File file = new File(myfile1, "cq_"+Calendar.getInstance().getTimeInMillis()+".mp4");
            FileOutputStream fos = new FileOutputStream(file);
            BufferedInputStream bis = new BufferedInputStream(is);
            byte[] buffer = new byte[1024];
            int len;
            int total = 0;
            while ((len = bis.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
                total += len;
                // 获取当前下载量
                pd.setProgress(total);
            }
            fos.close();
            bis.close();
            is.close();
            return file;
        } else {
            return null;
        }
    }


}
