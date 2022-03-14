package com.illyasr.mydempviews.util;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

public class AnimationUtil {
    private static boolean ismHiddenActionstart = false;
    private static AnimationUtil mInstance;


    public static AnimationUtil with() {
        if (mInstance == null) {
            synchronized (AnimationUtil.class) {
                if (mInstance == null) {
                    mInstance = new AnimationUtil();
                }
            }
        }
        return mInstance;
    }

    /**
     * 从控件所在位置移动到控件的底部
     *
     * @param v
     * @param Duration 动画时间
     */
    public static void moveToViewBottom(final View v, long Duration) {
        if (v.getVisibility() != View.VISIBLE)
            return;
        if (ismHiddenActionstart)
            return;
        TranslateAnimation mHiddenAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                0.0f, Animation.RELATIVE_TO_SELF, 1.0f);
        mHiddenAction.setDuration(Duration);
        v.clearAnimation();
        v.setAnimation(mHiddenAction);
        mHiddenAction.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                ismHiddenActionstart = true;
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                v.setVisibility(View.GONE);
                ismHiddenActionstart = false;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    /**
     * 从控件的底部移动到控件所在位置
     *
     * @param v
     * @param Duration 动画时间
     */
    public static void bottomMoveToViewLocation(View v, long Duration) {
        if (v.getVisibility() == View.VISIBLE)
            return;
        v.setVisibility(View.VISIBLE);
        TranslateAnimation mShowAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        mShowAction.setDuration(Duration);
        v.clearAnimation();
        v.setAnimation(mShowAction);
    }

    /**
     * 从控件所在位置移动到控件的顶部
     *
     * @param v
     * @param Duration 动画时间
     */
    public static void moveToViewTop(final View v, long Duration) {
        if (v.getVisibility() != View.VISIBLE)
            return;
        if (ismHiddenActionstart)
            return;
        TranslateAnimation mHiddenAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                0.0f, Animation.RELATIVE_TO_SELF, -1.0f);
        mHiddenAction.setDuration(Duration);
        v.clearAnimation();
        v.setAnimation(mHiddenAction);
        mHiddenAction.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                ismHiddenActionstart = true;
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                v.setVisibility(View.GONE);
                ismHiddenActionstart = false;
            }
            @Override
            public void onAnimationRepeat(Animation animation) {}
        });
    }

    /**
     * 从控件的顶部移动到控件所在位置
     *
     * @param v
     * @param Duration 动画时间
     */
    public static void topMoveToViewLocation(View v, long Duration) {
        if (v.getVisibility() == View.VISIBLE)
            return;
        v.setVisibility(View.VISIBLE);
        TranslateAnimation mShowAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                -1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        mShowAction.setDuration(Duration);
        v.clearAnimation();
        v.setAnimation(mShowAction);
    }

    /**
     *  从控件位置移动到控件左边
     * @param v
     * @param Duration
     */
    public static void moveToViewLeft(final View v, long Duration) {
        //----------
        if (v.getVisibility() != View.VISIBLE)
            return;
        if (ismHiddenActionstart)
            return;

        TranslateAnimation mHiddenAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF,
                0.0f,Animation.RELATIVE_TO_SELF, -1.0f, Animation.RELATIVE_TO_SELF,0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f);//隐藏到左边
        mHiddenAction.setDuration(Duration);
        v.clearAnimation();
        v.setAnimation(mHiddenAction);
        mHiddenAction.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                ismHiddenActionstart = true;
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                v.setVisibility(View.GONE);
                ismHiddenActionstart = false;
            }
            @Override
            public void onAnimationRepeat(Animation animation) {}
        });
        //---------
    }

    /**
     *  从左边移动到控件位置
     * @param v
     * @param Duration
     */
    public static void leftMoveToViewLocation(View v, long Duration) {
        if (v.getVisibility() == View.VISIBLE)
            return;
        v.setVisibility(View.VISIBLE);
        TranslateAnimation mShowAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, -1.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                0.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        mShowAction.setDuration(Duration);
        v.clearAnimation();
        v.setAnimation(mShowAction);
    }

    /**
     *  移动到控件右边
     * @param v
     * @param Duration
     */
    public static void moveToViewRight(final View v, long Duration) {
        //----------
        if (v.getVisibility() != View.VISIBLE)
            return;
        if (ismHiddenActionstart)
            return;

        TranslateAnimation mHiddenAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF,
                0.0f,Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF,0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f);//隐藏到左边
        mHiddenAction.setDuration(Duration);
        v.clearAnimation();
        v.setAnimation(mHiddenAction);
        mHiddenAction.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                ismHiddenActionstart = true;
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                v.setVisibility(View.GONE);
                ismHiddenActionstart = false;
            }
            @Override
            public void onAnimationRepeat(Animation animation) {}
        });
        //---------
    }

    /**
     *  从右边移动到控件位置
     * @param v
     * @param Duration
     */
    public static void rightMoveToViewLocation(View v, long Duration) {
        if (v.getVisibility() == View.VISIBLE)
            return;
        v.setVisibility(View.VISIBLE);
        TranslateAnimation mShowAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 1.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                0.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        mShowAction.setDuration(Duration);
        v.clearAnimation();
        v.setAnimation(mShowAction);
    }
}
