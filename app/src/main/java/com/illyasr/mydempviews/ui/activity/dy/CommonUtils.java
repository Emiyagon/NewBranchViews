package com.illyasr.mydempviews.ui.activity.dy;

import android.os.AsyncTask;
import android.util.Log;

import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.cache.model.CacheMode;
import com.zhouyou.http.callback.CallBack;
import com.zhouyou.http.exception.ApiException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *  链接重定向工具类
 */
public class CommonUtils {
    public static String DOU_YIN_BASE_URL = "https://www.iesdouyin.com/web/api/v2/aweme/iteminfo/?item_ids=";

    public static String HUO_SHAN_BASE_URL = " https://share.huoshan.com/api/item/info?item_id=";

    public static String DOU_YIN_DOMAIN = "douyin";

    public static String HUO_SHAN_DOMAIN = "huoshan";

    /**
     * 将长链接转为短链接(调用的新浪的短网址API)
     * 目前用不到,问题不大,大概率最好用服务器返回的短连接是最好的,安全可靠
     * @param url
     * 需要转换的长链接url
     * @return 返回转换后的短链接
     */
    public static String convertSinaShortUrl(String url) {
        try {
            // 调用新浪API

//            HttpPost post = new HttpPost("http://api.t.sina.com.cn/short_url/shorten.json");
            List params = new LinkedList();
            // 必要的url长链接参数
//            params.add(new BasicNameValuePair("url_long", url));
            // 必要的新浪key
//            params.add(new BasicNameValuePair("source", "3271760578"));
//            post.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));

            EasyHttp.post("short_url/shorten.json")
                    .cacheTime(300)//缓存300s 单位s
                    .baseUrl("http://api.t.sina.com.cn/")
                    .cacheKey("cachekey")//缓存key
                    .cacheMode(CacheMode.CACHEANDREMOTE)//设置请求缓存模式
                    .params("url_long",url)
                    .params("source","3271760578")
//                    .upJson(gson.toJson(map))
                    .execute(new CallBack<String>() {
                        @Override
                        public void onStart() {

                        }

                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(ApiException e) {

                        }

                        @Override
                        public void onSuccess(String s) {

                        }
                    });


        } catch (Exception e) {

        }

        return "";
    }

    /**
     *  短连接重定向为长链接
     * @param originUrl
     * @param onSuccessListener
     */
    public static void redirectUrl(String originUrl,final OnSuccessListener onSuccessListener) {
        new AsyncTask<String, Integer, String>() {
            @Override
            protected String doInBackground(String... strings) {
                try {
                HttpURLConnection conn  = (HttpURLConnection) new URL(originUrl).openConnection();
                conn.setInstanceFollowRedirects(false);
                conn.setConnectTimeout(5000);
                String url = conn.getHeaderField("Location");
                conn.disconnect();
                return url;
            } catch (IOException e) {
                e.printStackTrace();
                    return "";
                }
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                onSuccessListener.doLogic(s);

            }
        }.execute();
    }

    /**
     * 短连接重定向为长链接
     * @param url
     * @return
     */
    public static String getLocation(String url) {
        try {
            URL serverUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) serverUrl.openConnection();
//            conn.setRequestMethod("GET");
            conn.setInstanceFollowRedirects(false);
//            conn.setRequestProperty("User-agent", "ua");//模拟手机连接
            conn.connect();
            String location = conn.getHeaderField("Location");
            return location;
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("TAG", e.toString());
        }
        return "";
    }
    public static String matchNo(String redirectUrl) {
        List<String> results = new ArrayList<>();
        Pattern p = Pattern.compile("video/([\\w/\\.]*)/");
        Matcher m = p.matcher(redirectUrl);
        while (!m.hitEnd() && m.find()) {
            results.add(m.group(1));
        }
        return results.size()>0? results.get(0):"";
    }

    public static String hSMatchNo(String redirectUrl) {
        List<String> results = new ArrayList<>();
        Pattern p = Pattern.compile("item_id=([\\w/\\.]*)&");
        Matcher m = p.matcher(redirectUrl);
        while (!m.hitEnd() && m.find()) {
            results.add(m.group(1));
        }
        return results.get(0);
    }

    public static String httpGet(String urlStr) throws Exception {
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-Type", "text/json;charset=utf-8");
        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
        StringBuffer buf = new StringBuffer();
        String inputLine = in.readLine();
        while (inputLine != null) {
            buf.append(inputLine).append("\r\n");
            inputLine = in.readLine();
        }
        in.close();
        return buf.toString();
    }



    public interface OnSuccessListener{
        void doLogic(String s);
    }

}
