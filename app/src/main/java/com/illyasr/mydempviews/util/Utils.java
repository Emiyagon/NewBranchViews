package com.illyasr.mydempviews.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.view.View;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Handler;

public class Utils {


    public static List<String> numberList(int start, int end) {
        List<String> list = new ArrayList<>();

        for (int i = start; i <= end; i++) {
            list.add(i + "");
        }
        return list;
    }


    // 两次点击按钮之间的点击间隔不能少于1000毫秒
    private static final int MIN_CLICK_DELAY_TIME = 1000;
    private static long lastClickTime;



    /**
     *   是否是单击,如果多次点击则不执行这次操作
     * @return
     */
    public static boolean isFastClick() {
        boolean flag = false;
        long curClickTime = System.currentTimeMillis();
        if ((curClickTime - lastClickTime) >= MIN_CLICK_DELAY_TIME) {
            flag = true;
        }
        lastClickTime = curClickTime;
        return flag;
    }

        /**
         * 富文本适配
         */
    public static String getHtmlData(String bodyHTML) {
        String head = "<head>"
                + "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, user-scalable=no\"> "
                + "<style>img{max-width: 100%; width:auto; height:auto;}</style>"
                + "</head>";
        return "<html>" + head + "<body>" + bodyHTML + "</body></html>";
    }
    /**
     *   截取加密视频的正确url链接
     */
    public static String putNormalVideo(String url) {
        return url.replace(url.substring(url.indexOf("?")+1), "").replace("?", "").replace("\\","").replace(" ","");
    }


    /**
     * 设置一个view,在5s之后才可以继续点击
     * @param view
     * @param time  秒
     */
    public static void ShowViewCanClicked(final View view, int time) {
        view.setClickable(false);
        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                view.setClickable(true);
            }
        },time*1000);
    }

    //根据给定的宽高来定制裁剪图片的宽高
    public static Bitmap upImageSize(Context context, Bitmap bmp, int width, int height) {
        if (bmp == null) {
            return null;
        }
        // 计算比例
        float scaleX = (float) width / bmp.getWidth();// 宽的比例
        float scaleY = (float) height / bmp.getHeight();// 高的比例
        //新的宽高
        int newW = 0;
        int newH = 0;
        if (scaleX > scaleY) {
            newW = (int) (bmp.getWidth() * scaleX);
            newH = (int) (bmp.getHeight() * scaleX);
        } else if (scaleX <= scaleY) {
            newW = (int) (bmp.getWidth() * scaleY);
            newH = (int) (bmp.getHeight() * scaleY);
        }
        return Bitmap.createScaledBitmap(bmp, newW, newH, true);
    }

    /**
     * 获取视频总时长
     * @param mUri
     * @return
     */
    public static String getRingDuring(String mUri){
        String duration=null;
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();

        try {
            if (mUri != null) {
                HashMap<String, String> headers=null;
                if (headers == null) {
                    headers = new HashMap<String, String>();
                    headers.put("User-Agent", "Mozilla/5.0 (Linux; U; Android 4.4.2; zh-CN; MW-KW-001 Build/JRO03C) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 UCBrowser/1.0.0.001 U4/0.8.0 Mobile Safari/533.1");
                }
                mmr.setDataSource(mUri, headers);
            }

            duration = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        } catch (Exception ex) {
        } finally {
            mmr.release();
        }
        LogUtil.e("ryan","duration "+duration);
        return duration;
    }

    public static int getVideoLong(String url){
        try {
        MediaPlayer mediaPlayer = new MediaPlayer();

            mediaPlayer.setDataSource(url);
            mediaPlayer.prepare();
            mediaPlayer.getDuration();
            return  mediaPlayer.getDuration()/1000;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return 0;
    }

}
