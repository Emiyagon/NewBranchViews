package com.illyasr.mydempviews.util;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.TextView;

/**
 * 博客: http://hackware.lucode.net
 * Created by hackware on 2016/6/26.
 */
public final class UIUtil {

    public static int dip2px(Context context, double dpValue) {
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * density + 0.5);
    }

    public static int getScreenWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    /**
     *
     * @param context
     * @param textView
     * @param iconLeft
     * @param iconRight
     * @param leftW
     * @param lfetH
     * @param rightW
     * @param rightH
     */
    public void setDrawableLR(Context context,TextView textView, int iconLeft, int iconRight, int leftW, int lfetH, int rightW, int rightH) {
        Drawable drawableLeft = context.getResources().getDrawable(iconLeft);
        drawableLeft.setBounds(0, 0, leftW, lfetH);
        Drawable drawableRight = context.getResources().getDrawable(iconRight);
        drawableRight.setBounds(0, 0, rightW, rightH);
        textView.setCompoundDrawables(drawableLeft, null, drawableRight, null);
    }
}