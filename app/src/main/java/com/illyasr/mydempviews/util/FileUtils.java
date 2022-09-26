package com.illyasr.mydempviews.util;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

/**
 *  文件操作工具类
 */
public class FileUtils {
    private String path = Environment.getExternalStorageDirectory().toString() + "/chaoqi";
    /**
     * 保存图片
     * @param context
     * @param file
     */
    public static void saveImage(Context context, File file) {
        ContentResolver localContentResolver = context.getContentResolver();
        ContentValues localContentValues = getImageContentValues(context, file, System.currentTimeMillis());
        localContentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, localContentValues);

        Intent localIntent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
        final Uri localUri = Uri.fromFile(file);
        localIntent.setData(localUri);
        context.sendBroadcast(localIntent);
    }
    public static ContentValues getImageContentValues(Context paramContext, File paramFile, long paramLong) {
        ContentValues localContentValues = new ContentValues();
        localContentValues.put("title", paramFile.getName());
        localContentValues.put("_display_name", paramFile.getName());
        localContentValues.put("mime_type", "image/jpeg");
        localContentValues.put("datetaken", Long.valueOf(paramLong));
        localContentValues.put("date_modified", Long.valueOf(paramLong));
        localContentValues.put("date_added", Long.valueOf(paramLong));
        localContentValues.put("orientation", Integer.valueOf(0));
        localContentValues.put("_data", paramFile.getAbsolutePath());
        localContentValues.put("_size", Long.valueOf(paramFile.length()));
        return localContentValues;
    }

    /**
     * 保存视频
     * @param context
     * @param file
     */
    public static void saveVideo(Context context, File file) {
        //是否添加到相册
        ContentResolver localContentResolver = context.getContentResolver();
        ContentValues localContentValues = getVideoContentValues(context, file, System.currentTimeMillis());
        Uri localUri = localContentResolver.insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, localContentValues);
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, localUri));
    }

    public static ContentValues getVideoContentValues(Context paramContext, File paramFile, long paramLong) {
        ContentValues localContentValues = new ContentValues();
        localContentValues.put("title", paramFile.getName());
        localContentValues.put("_display_name", paramFile.getName());
        localContentValues.put("mime_type", "video/mp4");
        localContentValues.put("datetaken", Long.valueOf(paramLong));
        localContentValues.put("date_modified", Long.valueOf(paramLong));
        localContentValues.put("date_added", Long.valueOf(paramLong));
        localContentValues.put("_data", paramFile.getAbsolutePath());
        localContentValues.put("_size", Long.valueOf(paramFile.length()));
        return localContentValues;
    }


    public interface OnDownloadListener{
        void onStart();

        void onEnd();

        void onFailured();
    }
    public static void downMp4(Context context, String path,OnDownloadListener listener) {
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
                    listener.onStart();
                    File file = getFileFromServer(path, pd);
                    context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + file)));
                    //sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse(String.valueOf(file))));
//                    //获取ContentResolve对象，来操作插入视频
//                    ContentResolver localContentResolver = getContentResolver();
//                    //ContentValues：用于储存一些基本类型的键值对
//                    ContentValues localContentValues = getVideoContentValues(MainActivity.this, file, System.currentTimeMillis());
//                    //insert语句负责插入一条新的纪录，如果插入成功则会返回这条记录的id，如果插入失败会返回-1。
//                    Uri localUri = localContentResolver.insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, localContentValues);
                    sleep(1000);
                    pd.dismiss(); // 结束掉进度条对话框
                    listener.onEnd();
                } catch (Exception e) {
                    e.printStackTrace();
                    listener.onFailured();
                }
            }
        }.start();
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
            File file = new File(myfile1, UUID.randomUUID() +"x.mp4");
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



    /**
     *   获取本地内存地址(兼容Android Q)
     * @param context
     * @return
     */
    public static String getAbsultPath(Context context) {
        return context.getFilesDir().getAbsolutePath();
    }

    /**
     *  单个文件
     * @param assetsName
     * @param sdEndFileName
     * @throws Exception
     */
    public void sendAssetFileToSDCard(Context context,String assetsName,String sdEndFileName) throws Exception{
//        String fileName = "show.mp4"; //assets目录下的资源名及后缀
        String fileName = assetsName; //assets目录下的资源名及后缀
        InputStream inputStream = context.getAssets().open(fileName);
        //"/storage/emulated/0/ResProvider/video"(需要复制到那个目录下)
//        File file = new File(Environment.getExternalStorageDirectory().getAbsoluteFile() + "/ResProvider/video");
        File file = new File(getAbsultPath(context)  + sdEndFileName);
        Log.e("TAG","end path ="+file.getPath());
        if (!file.exists()) { //判断一下文件夹是否存在了，避免重复复制了
            Log.d("TAG", "file do not exists");
            file.mkdirs(); //不存在，创建一个新的文件夹
        }
        //最终文件路径为："/storage/emulated/0/ResProvider/video/show.mp4"
        FileOutputStream fileOutputStream = new FileOutputStream(file + File.separator + fileName);//File.separator就是"/"
        //这里开始拷贝
        int len = -1;
        byte[] buffer = new byte[1024];
        while ((len = inputStream.read(buffer)) != -1) {
            fileOutputStream.write(buffer, 0, len);
        }
        fileOutputStream.close();//用完了，记得关闭
        inputStream.close();
    }

    /**
     *  把所有assets文件都放入本地
     * @param assetsFilePath
     * @param sdEndFileName
     * @throws Exception
     */
    public void sendAllFilesToSDCard(Context context,String assetsFilePath,String sdEndFileName)
            throws Exception{
        String fileNames[] = context.getAssets().list(assetsFilePath);
        if (fileNames.length==0 ||  TextUtils.isEmpty(sdEndFileName)){
            throw new NullPointerException("文件素材错误");
//            return;
        }
        for ( String s:fileNames ) {
            sendAssetFileToSDCard(context,s, sdEndFileName);
        }

    }
}
