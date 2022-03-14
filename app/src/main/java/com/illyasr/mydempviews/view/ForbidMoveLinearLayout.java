package com.illyasr.mydempviews.view;


import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

public class ForbidMoveLinearLayout extends LinearLayout {
    public ForbidMoveLinearLayout(Context context) {
        super(context);
    }

    public ForbidMoveLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ForbidMoveLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_MOVE){
            return true;
        }
        return super.onInterceptTouchEvent(ev);
    }
}
