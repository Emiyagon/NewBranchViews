package com.illyasr.mydempviews;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

import com.readystatesoftware.chuck.ChuckInterceptor;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.cache.converter.SerializableDiskConverter;
import com.zhouyou.http.model.HttpHeaders;

import cat.ereza.customactivityoncrash.CustomActivityOnCrash;
import cat.ereza.customactivityoncrash.activity.DefaultErrorActivity;
import cat.ereza.customactivityoncrash.config.CaocConfig;

public class MyApplication extends MultiDexApplication {
    public static MyApplication myApp;
    public static boolean isTestEnvironment = true;
    public static MyApplication getInstance() {
        return myApp;
    }
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
        myApp=this;
//        MultiDex.install(MyApplication.getInstance());


    }

    @Override
    public void onCreate() {
        super.onCreate();
        myApp=this;
        // 初始化HttpClient.
        EasyHttp.init(this);//默认初始化,必须调用
        //设置请求头
        HttpHeaders headers = new HttpHeaders();
//        headers.put("sign", HttpUtils.sign);
//        headers.put("token",token);
        /**
        // accept: application/json, text/plain, x/*
        accept-encoding: gzip, deflate, br
        accept-language: zh-CN,zh;q=0.9
        cache-control: no-cache
        content-length: 53
        content-type: application/json;charset=UTF-8
        cookie: Hm_lvt_c54ff74d1e9b3a84834d70113b630139=1646300947; Hm_lpvt_c54ff74d1e9b3a84834d70113b630139=1646300947
        origin: https://parse.bqrdh.com
        pragma: no-cache
        referer: https://parse.bqrdh.com/smart
        sec-ch-ua: " Not A;Brand";v="99", "Chromium";v="98", "Google Chrome";v="98"
        sec-ch-ua-mobile: ?0
        sec-ch-ua-platform: "macOS"
        sec-fetch-dest: empty
        sec-fetch-mode: cors
        sec-fetch-site: same-origin
         x-request-id: 1499321640037781504
         user-agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/98.0.4758.109 Safari/537.36
         */
        headers.put("x-request-id","1499321640037781504");
        headers.put("accept","application/json, text/plain, */*");
        headers.put("accept-encoding","gzip, deflate, br");
        headers.put("content-type","application/json;charset=UTF-8");
        headers.put("cookie","Hm_lvt_c54ff74d1e9b3a84834d70113b630139=1646300947; Hm_lpvt_c54ff74d1e9b3a84834d70113b630139=1646300947");
        headers.put("origin","https://parse.bqrdh.com");
        headers.put("pragma","no-cache");
        headers.put("referer","https://parse.bqrdh.com/smart");
        headers.put("user-agent","Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/98.0.4758.109 Safari/537.36");
        EasyHttp.getInstance()
                .setCacheTime(-1)//-1表示永久缓存,单位:秒 ，Okhttp和自定义RxCache缓存都起作用
                .debug("RxEasyHttp", true)
                .addCommonHeaders(headers)//设置全局公共头
                .setReadTimeOut(60 * 1000)
                .setWriteTimeOut(60 * 1000)
                .setConnectTimeout(60 * 1000)
                .setRetryCount(3)//默认网络不好自动重试3次
                .setRetryDelay(500)//每次延时500ms重试
                .setRetryIncreaseDelay(500)//每次延时叠加500ms
//                .setBaseUrl(RetrofitUtils.BASE_URL)
                .setCacheDiskConverter(new SerializableDiskConverter())//默认缓存使用序列化转化
                .setCacheMaxSize(50 * 1024 * 1024)//设置缓存大小为50M
                .setCacheVersion(0)//缓存版本为1
                .addInterceptor(new ChuckInterceptor(myApp))
                .setCertificates();//信任所有证书;



        OnHit();
    }

    /**
     *  Android检测程序崩溃框架CustomActivityOnCrash
     */
    @SuppressLint("RestrictedApi")
    private void OnHit() {
        //整个配置属性，可以设置一个或多个，也可以一个都不设置
        CaocConfig.Builder.create()
                //程序在后台时，发生崩溃的三种处理方式
                //BackgroundMode.BACKGROUND_MODE_SHOW_CUSTOM: //当应用程序处于后台时崩溃，也会启动错误页面，
                //BackgroundMode.BACKGROUND_MODE_CRASH:      //当应用程序处于后台崩溃时显示默认系统错误（一个系统提示的错误对话框），
                //BackgroundMode.BACKGROUND_MODE_SILENT:     //当应用程序处于后台时崩溃，默默地关闭程序！这种模式我感觉最好
                .backgroundMode(CaocConfig.BACKGROUND_MODE_SILENT)
                .enabled(true)     //这阻止了对崩溃的拦截,false表示阻止。用它来禁用customactivityoncrash框架
                .showErrorDetails(true) //这将隐藏错误活动中的“错误详细信息”按钮，从而隐藏堆栈跟踪。
                .showRestartButton(true)    //是否可以重启页面
                .trackActivities(true)     //错误页面中显示错误详细信息
                .minTimeBetweenCrashesMs(2000)      //定义应用程序崩溃之间的最短时间，以确定我们不在崩溃循环中。比如：在规定的时间内再次崩溃，框架将不处理，让系统处理！
                .errorDrawable(R.mipmap.ic_launcher)     //崩溃页面显示的图标
                .restartActivity(MainActivity.class)      //重新启动后的页面(最好是闪屏页面,这样可塑性强,也不会出太多问题)
                .errorActivity(DefaultErrorActivity.class) //程序崩溃后显示的页面,可以自定义
                .eventListener(new CustomEventListener())//设置监听
                .apply();
        //如果没有任何配置，程序崩溃显示的是默认的设置
//        CustomActivityOnCrash.install(this);
    }
    /**
     * 监听程序崩溃/重启
     */
    private static final String TAG = "TAG";

    private static class CustomEventListener implements CustomActivityOnCrash.EventListener {
        //程序崩溃回调
        @Override
        public void onLaunchErrorActivity() {
            Log.e(TAG, "onLaunchErrorActivity()");
        }

        //重启程序时回调
        @Override
        public void onRestartAppFromErrorActivity() {
            Log.e(TAG, "onRestartAppFromErrorActivity()");
        }

        //在崩溃提示页面关闭程序时回调
        @Override
        public void onCloseAppFromErrorActivity() {
            Log.e(TAG, "onCloseAppFromErrorActivity()");
        }

    }
}
