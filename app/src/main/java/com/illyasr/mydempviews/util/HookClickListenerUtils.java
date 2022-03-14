package com.illyasr.mydempviews.util;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class HookClickListenerUtils {
    private static HookClickListenerUtils mHookClickListenerUtils;

    private HookClickListenerUtils() {
    }

    public static HookClickListenerUtils getInstance() {
        synchronized ("getInstance") {
            if (mHookClickListenerUtils == null) {
                mHookClickListenerUtils = new HookClickListenerUtils();
            }
        }
        return mHookClickListenerUtils;
    }

    /***
     * 递归调用
     * @param decorView
     */
    public void hookDecorViewClick(View decorView) {
        if (decorView instanceof ViewGroup) {
            int count = ((ViewGroup) decorView).getChildCount();
            for (int i = 0; i < count; i++) {
                if (((ViewGroup) decorView).getChildAt(i) instanceof ViewGroup) {
                    hookDecorViewClick(((ViewGroup) decorView).getChildAt(i));
                } else {
                    hookViewClick(((ViewGroup) decorView).getChildAt(i));
                }
            }
        } else {
            hookViewClick(decorView);
        }
    }

    public void hookViewClick(View view) {
        try {
            Class viewClass = Class.forName("android.view.View");
            Method getListenerInfoMethod = viewClass.getDeclaredMethod("getListenerInfo");
            if (!getListenerInfoMethod.isAccessible()) {
                getListenerInfoMethod.setAccessible(true);
            }
            Object listenerInfoObject = getListenerInfoMethod.invoke(view);// 反射view中的getListenerInfo方法

            // 第二步：获取到view中的ListenerInfo中的mOnClickListener属性
            Class mListenerInfoClass = Class.forName("android.view.View$ListenerInfo");
            Field mOnClickListenerField = mListenerInfoClass.getDeclaredField("mOnClickListener");
            mOnClickListenerField.setAccessible(true);
            // 这里为何传入的View.OnClickListener为Field.get()的值
            mOnClickListenerField.set(listenerInfoObject, new HookClickListener((View.OnClickListener) mOnClickListenerField.get(listenerInfoObject)));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static class HookClickListener implements View.OnClickListener {

        private View.OnClickListener onClickListener;

        public HookClickListener(View.OnClickListener onClickListener) {
            this.onClickListener = onClickListener;
        }

        @Override public void onClick(View v) {
//            Toast.makeText(v.getContext(), "hook住点击事件了，禽兽", Toast.LENGTH_SHORT).show();
            if (onClickListener != null) {
                onClickListener.onClick(v);
            }
        }
    }

}
