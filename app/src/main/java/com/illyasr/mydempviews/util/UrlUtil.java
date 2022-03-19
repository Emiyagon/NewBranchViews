package com.illyasr.mydempviews.util;

import android.os.AsyncTask;
import android.util.Log;

import com.illyasr.mydempviews.ui.activity.dy.CommonUtils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * TODO
 *  url转换
 * @author qingshilin
 * @version 1.0
 * @date 2022/3/19 11:09
 */
public class UrlUtil {

    public static String shortToLong(String url) {
//        final String[] sendUrl = {""};
        //连接
        if (url.length()>30){
            return url;
        }

        return CommonUtils.getLocation(url);
    }

    public static boolean isLongUrl(String url) {
        return url.length() >= 30;
    }

}
