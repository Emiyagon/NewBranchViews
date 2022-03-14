package com.illyasr.mydempviews.util;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;

/**
 * 描述:
 * 作者:LF
 * 创建日期: 2021/4/3 11:16
 * 备注:
 */
public class ClickUtils {
    private static long lastClickTime = 0;
    private static String mClassName;

    private static boolean isFast(String className) {
        long curClickTime = System.currentTimeMillis();
        boolean flag = false;

        if(className.equals(mClassName)){//同一个页面下
            if ((curClickTime - lastClickTime) < 1500) {//两次点击按钮之间的点击间隔不能少于1000毫秒
                flag = true;
            }
        }
        mClassName = className;
        lastClickTime = curClickTime;
        return flag;
    }
    public static boolean isFast(Activity activity) {
        return isFast(activity.getClass().getName());
    }
    public static boolean isFast(Fragment fragment) {
        return isFast(fragment.getClass().getName());
    }
}
