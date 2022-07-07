package com.illyasr.mydempviews.ui.webs;

import android.content.Context;
import android.util.Log;
import android.webkit.JavascriptInterface;

/**
 * TODO
 *
 * @author Administrator
 * @version 1.0
 * @date 2022/3/29 9:39
 */
public class MyJavascriptInterface {
    private Context context;

    private OnCodeInterface onCodeInterface;
    public interface OnCodeInterface {
        void inIt(String code);
    }

    public MyJavascriptInterface(Context context) {
        this.context = context;
        this.onCodeInterface = code -> {

        };
    }
    public MyJavascriptInterface(Context context,OnCodeInterface onCodeInterface) {
        this.context = context;
        this.onCodeInterface = onCodeInterface;
    }
    /**
     * 网页使用的js，方法无参数
     */
    @JavascriptInterface
    public void callAndroidForAnswer() {
        Log.e("TAG", "----无参");
    }

    /**
     * 网页使用的js，方法有参数，且参数名为data
     *
     *   网页js里的参数名
     */
    @JavascriptInterface
    public void callAndroidForAnswer(String msg) {
//        Log.e("TAG", "----有参 code =" + code);
        Log.e("TAG", "----有参 msg=" + msg);
        if (onCodeInterface != null) {
            onCodeInterface.inIt(msg);
        }
    }
}
