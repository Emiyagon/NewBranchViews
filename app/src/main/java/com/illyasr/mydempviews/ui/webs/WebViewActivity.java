package com.illyasr.mydempviews.ui.webs;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;


import com.illyasr.mydempviews.R;
import com.illyasr.mydempviews.base.BaseActivity;
import com.illyasr.mydempviews.databinding.ActivityWebViewBinding;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.channels.FileLock;

/**
 * addSuccess
 */
public class WebViewActivity extends BaseActivity<ActivityWebViewBinding,WebPresent> {


    private WebView webView;
    private String url;
    private int extra;

    private static final String PROCESS = "com.processkill.p2";

    @Override
    protected int setLayoutId() {
        return R.layout.activity_web_view;
    }

    @Override
    protected void initData() {

        mBindingView.setVm(mPresenter);
        webView = new WebView(this);
        mBindingView.frame.addView(webView);
        url = getIntent().getStringExtra("url");
        extra = getIntent().getIntExtra("extra", -1);
        initWebView();

    }


    @SuppressLint("JavascriptInterface")
    private void initWebView() {
        if (webView != null) {
            webView.clearCache(true);
        }

        webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        webView.loadUrl(TextUtils.isEmpty(url)?"file:///android_asset/nullpoint.html" :url);
        webView.setOverScrollMode(View.OVER_SCROLL_NEVER);
//        webView.setScrollBarStyle();
        webView.setHorizontalScrollBarEnabled(false);//水平不显示
        webView.setVerticalScrollBarEnabled(false); //垂直不显示
        WebSettings settings = webView.getSettings();
        //设置支持javascript
        settings.setJavaScriptEnabled(true);
        //禁用横向滚动条
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        // 设置UA
        String UA = settings.getUserAgentString();
        settings.setUserAgentString(UA+"");

        settings.setDefaultTextEncodingName("utf-8");//文本编码
//        webView.setDomStorageEnabled(true);//设置DOM存储已启用（注释后文本显示变成js代码）

        // android 5.0以上默认不支持Mixed Content
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE);
        }

        settings.setDomStorageEnabled(true);
        //设置自适应屏幕，两者合用
        settings.setUseWideViewPort(true); //将图片调整到适合webview的大小
        settings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
        settings.setLoadsImagesAutomatically(true);    //支持自动加载图片
        settings.setAllowFileAccess(true);    // 可以读取文件缓存
        settings.setAllowFileAccessFromFileURLs(true);
        settings.setAllowUniversalAccessFromFileURLs(true);
        String appCachePath = this.getApplicationContext().getCacheDir().getAbsolutePath();
        settings.setAppCachePath(appCachePath);
        settings.setAppCacheEnabled(true);    //开启H5(APPCache)缓存功能

//         webView.setDefaultHandler(new DefaultHandler());
        //缩放操作
        settings.setSupportZoom(true); //支持缩放，默认为true。是下面那个的前提。
        settings.setBuiltInZoomControls(true); //设置内置的缩放控件。若为false，则该WebView不可缩放
        settings.setDisplayZoomControls(false); //隐藏原生的缩放控件
        settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);

        settings.setBlockNetworkImage(false);

        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                setTitle(title);
            }

            @Override
            public void onProgressChanged(WebView view, int i) {
                super.onProgressChanged(view, i);
                showDialog("加载中");
                mBindingView.progressBar.setProgress(i);
                if (100 == i) {
                    dismissDialog();
                    mBindingView.progressBar.setVisibility(View.GONE);
                }
            }
        });
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
//                super.onReceivedSslError(view, handler, error);
                // TODO Auto-generated method stub
                // handler.cancel();// Android默认的处理方式
                handler.proceed();// 接受所有网站的证书
                // handleMessage(Message msg);// 进行其他处理
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                Log.e(TAG, "url start = " + url);
                showDialog("加载中...");
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                Log.e(TAG, "url end = " + url);
                dismissDialog();

            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return super.shouldOverrideUrlLoading(view, request);
            }

            @Nullable
            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
                return super.shouldInterceptRequest(view, request);
            }

        });

        /**
         *     h5和移动端交互
         */
        webView.addJavascriptInterface(new MyJavascriptInterface(this, code -> {
//            showToast(code.equals("200")?"新增成功!":"新增失败!");
//            SPUtil.putBoolean("webRefresh",true);
            Intent intent = new Intent("net.MYBROADCAST");
        //  将要广播的数据添加到Intent对象中
            intent.putExtra("webRefresh", 10);
            //  发送广播
            sendBroadcast(intent);
//            handler.sendEmptyMessageAtTime(code.equals("200")?10:20, 3000);
            handler.postDelayed(() -> {
                handler.sendEmptyMessage(code.equals("200") ? 10 : 20);
            },1500);
        }), "android");
//        handleWebviewDir(this);
    }

    private static void handleWebviewDir(Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
            return;
        }
        try {
            String suffix = "";
            String processName = getProcessName(context);
            if (!TextUtils.equals(context.getPackageName(), processName)) {//判断不等于默认进程名称
                suffix = TextUtils.isEmpty(processName) ? context.getPackageName() : processName;
                WebView.setDataDirectorySuffix(suffix);
                suffix = "_" + suffix;
            }
            tryLockOrRecreateFile(context,suffix);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @TargetApi(Build.VERSION_CODES.P)
    private static void tryLockOrRecreateFile(Context context,String suffix) {
        String sb = context.getDataDir().getAbsolutePath() +
                "/app_webview"+suffix+"/webview_data.lock";
        File file = new File(sb);
        if (file.exists()) {
            try {
                FileLock tryLock = new RandomAccessFile(file, "rw").getChannel().tryLock();
                if (tryLock != null) {
                    tryLock.close();
                } else {
                    createFile(file, file.delete());
                }
            } catch (Exception e) {
                e.printStackTrace();
                boolean deleted = false;
                if (file.exists()) {
                    deleted = file.delete();
                }
                createFile(file, deleted);
            }
        }
    }

    private static void createFile(File file, boolean deleted){
        try {
            if (deleted && !file.exists()) {
                file.createNewFile();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Handler handler = new Handler(){
        @SuppressLint("HandlerLeak")
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                default:
                    break;
                case 10:
                    finish();
                    break;
                case 20:
//                    showToast("加载失败,请重试!");
                    break;
            }
        }
    };
//    @Override
    protected void onBackClick() {
//        finish();
        if (webView.canGoBack()){
            webView.goBack();
            return;
        }
        finish();
//        System.exit(0);
        //        android.os.Process.killProcess(android.os.Process.myPid());
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
        if (webView != null) {
            ViewParent parent = webView.getParent();
            if (parent != null) {
                ((ViewGroup) parent).removeView(webView);
            }
            webView.removeAllViews();
            webView.destroy();
            webView = null;
        }
    }
    public static String getProcessName(Context context) {
        if (context == null) return null;
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo processInfo : manager.getRunningAppProcesses()) {
            if (processInfo.pid == android.os.Process.myPid()) {
                return processInfo.processName;
            }
        }
        return null;
    }
    public String getString(String s, String defValue) {
        return TextUtils.isEmpty(s) ? defValue : s;
    }


}