package com.illyasr.mydempviews.util;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.ListView;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class NoDoubleClick {

    public static class lifecycleCallbacks implements Application.ActivityLifecycleCallbacks {

        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            fixViewMutiClickInShortTime(activity);
        }

        @Override
        public void onActivityStarted(Activity activity) {

        }

        @Override
        public void onActivityResumed(Activity activity) {

        }

        @Override
        public void onActivityPaused(Activity activity) {

        }

        @Override
        public void onActivityStopped(Activity activity) {

        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

        }

        @Override
        public void onActivityDestroyed(Activity activity) {

        }
    }

    //防止短时间内多次点击，弹出多个activity 或者 dialog ，等操作
    private static void fixViewMutiClickInShortTime(final Activity activity) {
        activity.getWindow().getDecorView().getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                proxyOnlick(activity.getWindow().getDecorView(),5);
            }
        });
    }

    private static void proxyOnlick(View view, int recycledContainerDeep) {
        if (view.getVisibility() == View.VISIBLE) {
            boolean forceHook = recycledContainerDeep == 1;
            if (view instanceof ViewGroup) {
                boolean existAncestorRecycle = recycledContainerDeep > 0;
                ViewGroup p = (ViewGroup) view;
                if (!(p instanceof AbsListView || p instanceof ListView) || existAncestorRecycle) {
                    getClickListenerForView(view);
                    if (existAncestorRecycle) {
                        recycledContainerDeep++;
                    }
                } else {
                    recycledContainerDeep = 1;
                }
                int childCount = p.getChildCount();
                for (int i = 0; i < childCount; i++) {
                    View child = p.getChildAt(i);
                    proxyOnlick(child, recycledContainerDeep);
                }
            } else {
                getClickListenerForView(view);
            }
        }
    }

    /**
     * 通过反射  查找到view 的clicklistener
     * @param view
     */
    public static void getClickListenerForView(View view) {
        try {
            Class viewClazz = Class.forName("android.view.View");
            //事件监听器都是这个实例保存的
            Method listenerInfoMethod = viewClazz.getDeclaredMethod("getListenerInfo");
            if (!listenerInfoMethod.isAccessible()) {
                listenerInfoMethod.setAccessible(true);
            }
            Object listenerInfoObj = listenerInfoMethod.invoke(view);

            Class listenerInfoClazz = Class.forName("android.view.View$ListenerInfo");

            Field onClickListenerField = listenerInfoClazz.getDeclaredField("mOnClickListener");

            if (null != onClickListenerField) {
                if (!onClickListenerField.isAccessible()) {
                    onClickListenerField.setAccessible(true);
                }
                View.OnClickListener mOnClickListener = (View.OnClickListener) onClickListenerField.get(listenerInfoObj);
                if (!(mOnClickListener instanceof ProxyOnclickListener)) {
                    //自定义代理事件监听器
                    View.OnClickListener onClickListenerProxy = new ProxyOnclickListener(mOnClickListener);
                    //更换
                    onClickListenerField.set(listenerInfoObj, onClickListenerProxy);
                }else{
                    Log.e("OnClickListenerProxy", "setted proxy listener ");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//自定义的代理事件监听器


    static class ProxyOnclickListener implements View.OnClickListener {
        private View.OnClickListener onclick;

        private int MIN_CLICK_DELAY_TIME = 1000;

        private long lastClickTime = 0;

        public ProxyOnclickListener(View.OnClickListener onclick) {
            this.onclick = onclick;
        }

        @Override
        public void onClick(View v) {
            //点击时间控制
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
                lastClickTime = currentTime;
                Log.e("OnClickListenerProxy", "OnClickListenerProxy"+this);
                if (onclick != null) onclick.onClick(v);
            }
        }
    }
}
