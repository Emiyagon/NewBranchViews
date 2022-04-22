package com.illyasr.mydempviews.view.spin;

import static android.view.KeyEvent.ACTION_DOWN;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.LinearLayout;

/**
 * TODO
 * 展开之后花瓣布局的item,但是这里有个巨大的问题,就是当里面的view有点击事件之后会直接拦截掉,导致无法关闭
 * 目前想到的办法是直接把点击事件甩出去,小化之后点击事件反馈给父view
 * @author qingshilin
 * @version 1.0
 * @date 2022/4/21 17:04
 */
public class SMItemLayout extends LinearLayout {

    public SMItemLayout(Context context) {
        this(context, null);
    }

    public SMItemLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SMItemLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOrientation(VERTICAL);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.e("TAG", "onTouchEvent"+event.toString());
        return super.onTouchEvent(event);
    }


    /**
        点击到设置了事件的view,出现这个情况
     E/TAG: onInterceptTouchEventMotionEvent { action=ACTION_DOWN, actionButton=0, id[0]=0, x[0]=244.0, y[0]=360.0, toolType[0]=TOOL_TYPE_FINGER, buttonState=0, classification=NONE, metaState=0, flags=0x0, edgeFlags=0x0, pointerCount=1, historySize=0, eventTime=208177910, downTime=208177910, deviceId=2, source=0x1002, displayId=0 }
     E/TAG: onInterceptTouchEventMotionEvent { action=ACTION_MOVE, actionButton=0, id[0]=0, x[0]=244.0, y[0]=360.0, toolType[0]=TOOL_TYPE_FINGER, buttonState=0, classification=NONE, metaState=0, flags=0x0, edgeFlags=0x0, pointerCount=1, historySize=0, eventTime=208177959, downTime=208177910, deviceId=2, source=0x1002, displayId=0 }
     E/TAG: onInterceptTouchEventMotionEvent { action=ACTION_UP, actionButton=0, id[0]=0, x[0]=244.0, y[0]=360.0, toolType[0]=TOOL_TYPE_FINGER, buttonState=0, classification=NONE, metaState=0, flags=0x0, edgeFlags=0x0, pointerCount=1, historySize=0, eventTime=208177968, downTime=208177910, deviceId=2, source=0x1002, displayId=0 }

        点击到空白或者不拦截的地方
     E/TAG: onInterceptTouchEvent MotionEvent { action=ACTION_DOWN, actionButton=0, id[0]=0, x[0]=237.0, y[0]=618.0, toolType[0]=TOOL_TYPE_FINGER, buttonState=0, classification=NONE, metaState=0, flags=0x0, edgeFlags=0x0, pointerCount=1, historySize=0, eventTime=208233967, downTime=208233967, deviceId=2, source=0x1002, displayId=0 }
     E/TAG: onTouchEvent MotionEvent { action=ACTION_MOVE, actionButton=0, id[0]=0, x[0]=237.0, y[0]=618.0, toolType[0]=TOOL_TYPE_FINGER, buttonState=0, classification=NONE, metaState=0, flags=0x0, edgeFlags=0x0, pointerCount=1, historySize=1, eventTime=208233980, downTime=208233967, deviceId=2, source=0x1002, displayId=0 }
     E/TAG: onTouchEvent MotionEvent { action=ACTION_MOVE, actionButton=0, id[0]=0, x[0]=237.0, y[0]=618.0, toolType[0]=TOOL_TYPE_FINGER, buttonState=0, classification=NONE, metaState=0, flags=0x0, edgeFlags=0x0, pointerCount=1, historySize=0, eventTime=208234007, downTime=208233967, deviceId=2, source=0x1002, displayId=0 }
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        Log.e("TAG", "onInterceptTouchEvent"+ev.toString());
        switch (ev.getAction()) {
            case KeyEvent.ACTION_DOWN:
                return true;
//                break;
            case KeyEvent.ACTION_UP:
//                return true;
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }
}
