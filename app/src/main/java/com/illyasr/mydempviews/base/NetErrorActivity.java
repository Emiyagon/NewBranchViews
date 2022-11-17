package com.illyasr.mydempviews.base;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.illyasr.mydempviews.MainPresent;
import com.illyasr.mydempviews.R;
import com.illyasr.mydempviews.databinding.ActivityNetErrorBinding;

public class NetErrorActivity extends BaseActivity<ActivityNetErrorBinding, MainPresent> {


    @Override
    protected void initData() {
        initDete(mBindingView.webview);
    }

    private void initDete(WebView webView) {

        //是否可以后退
        webView.canGoBack();
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        //设置自适应屏幕，两者合用
        settings.setUseWideViewPort(true); //将图片调整到适合webview的大小
        settings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
        settings.setLoadsImagesAutomatically(true);    //支持自动加载图片
        settings.setAllowFileAccessFromFileURLs(true);
        settings.setAllowUniversalAccessFromFileURLs(true);
        String appCachePath = this.getApplicationContext().getCacheDir().getAbsolutePath();
        settings.setAppCachePath(appCachePath);
        settings.setAllowFileAccess(true);    // 可以读取文件缓存
        settings.setAppCacheEnabled(true);    //开启H5(APPCache)缓存功能
        //缩放操作
        settings.setSupportZoom(true); //支持缩放，默认为true。是下面那个的前提。
        settings.setBuiltInZoomControls(true); //设置内置的缩放控件。若为false，则该WebView不可缩放
        settings.setDisplayZoomControls(false); //隐藏原生的缩放控件
//        webView.loadUrl(url);
        // 载入内容
        webView.loadUrl("file:///android_asset/net_error.html");

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {

            }
        });

        webView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                return true;
            }

            @Override
            public void onLoadResource(WebView view, String url) {

                super.onLoadResource(view, url);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                Log.e(TAG, "start url = " + url);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                Log.e(TAG, "finish url = " + url);
                mBindingView.lLayoutBg.setBackgroundColor(url.contains("member")? 0xffe9bd2e:0xff000000);
//                writeData(view);
              /*  String js = "localStorage.setItem('" + Url.key + "','" + MApplication.mCache.getAsString(CoServiceConnectionnstant.TOKEN) + "');";
                String jsUrl = "javascript:(function({ var localStorage = window.localStorage; localStorage.setItem('" + Url.key + "','" + MApplication.mCache.getAsString(Constant.TOKEN) + "')})()";
                //根据不同版本，使用不同的 API 执行 Js
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    view.evaluateJavascript(js, null);
                } else {
                    view.loadUrl(jsUrl);
                }*/
            }
        });

        /**
         *   设置UA
         */
        String ua = webView.getSettings().getUserAgentString();//原来获取的UA
//        webView.getSettings().setUserAgentString(ua + "ChaoQiClient/" + BuildConfig.VERSION_NAME);//添加
//        webView.getSettings().setUserAgentString(ua.replace("Android","ChaoQiClient"));//替换


    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_net_error;
    }
}