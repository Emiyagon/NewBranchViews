package com.illyasr.mydempviews.view.old;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Scroller;

import com.illyasr.mydempviews.R;

/**
 推拉门view,没什么用

 * Created by bullet on 2018/8/2.
 */

public class PullDoorView extends RelativeLayout {
    private Context mContext;
    //滑动管理器
    private Scroller mScroller;

    private int mScreenWidth = 0;

    private int mScreenHeigh = 0;
    //第一次按下距离
    private int mFristDownY = 0;

    private int mCurryY;
    //偏移量
    private int mScrollY;
    //是否滑动完成
    private boolean mFinishFlag = false;

    private ImageView mImgView;

    public PullDoorView(Context context) {
        super(context);
        mContext = context;
        setupView();
    }

    public PullDoorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        setupView();
    }
    public void showImgAnimation(){
        mScroller.startScroll(0,0,0,0,1000);
        invalidate();
        this.setVisibility(View.VISIBLE);
        mFinishFlag =false;

    }
    private void setupView() {
        // 这个Interpolator你可以设置别的 我这里选择的是有弹跳效果的Interpolator
        Interpolator polator = new BounceInterpolator();
        mScroller = new Scroller(mContext, polator);
        // 获取屏幕分辨率
        WindowManager wm = (WindowManager) (mContext.getSystemService(Context.WINDOW_SERVICE));
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        mScreenHeigh = dm.heightPixels;
        mScreenWidth = dm.widthPixels;

        // 这里你一定要设置成透明背景,不然会影响你看到底层布局
        this.setBackgroundColor(Color.argb(0, 0, 0, 0));
        mImgView = new ImageView(mContext);
        mImgView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT));
        mImgView.setScaleType(ImageView.ScaleType.FIT_XY);// 填充整个屏幕
        mImgView.setImageResource(R.mipmap.loading); // 默认背景
        addView(mImgView);
    }

    // 设置推动门背景
    public void setBgImage(int id) {
        mImgView.setImageResource(id);
    }

    // 设置推动门背景
    public void setBgImage(Drawable drawable) {
        mImgView.setImageDrawable(drawable);
    }

    // 推动门的动画
    public void startBounceAnim(int startY, int dy, int duration) {
        mScroller.startScroll(0, startY, 0, dy, duration);
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        switch (action) {
            default:
                break;
            case MotionEvent.ACTION_DOWN:
                mFristDownY = (int) event.getY();
                System.err.println("ACTION_DOWN=" + mFristDownY);
                return true;
            case MotionEvent.ACTION_MOVE:
                mCurryY = (int) event.getY();
                System.err.println("ACTION_MOVE=" + mCurryY);
                mScrollY = mCurryY - mFristDownY;
                // 只准上滑有效
                if (mScrollY < 0) {
                    scrollTo(0, -mScrollY);
                }
                System.err.println("------------- ACTION_MOVE " + mScrollY);
                break;
            case MotionEvent.ACTION_UP:
                mCurryY = (int) event.getY();
                mScrollY = mCurryY - mFristDownY;
                System.err.println("------------- ACTION_UP " + mScrollY);
                if (mScrollY < 0) {
                    if (Math.abs(mScrollY) > mScreenHeigh / 2) {

                        // 向上滑动超过半个屏幕高的时候 开启向上消失动画
                        startBounceAnim(this.getScrollY(), mScreenHeigh-this.getScrollY(), 500);
                        mFinishFlag = true;

                    } else {
                        // 向上滑动未超过半个屏幕高的时候 开启向下弹动动画
                        startBounceAnim(this.getScrollY(), -this.getScrollY(), 1000);

                    }
                }

                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void computeScroll() {

        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            Log.i("scroller", "getCurrX()= " + mScroller.getCurrX()
                    + "     getCurrY()=" + mScroller.getCurrY()
                    + "  getFinalY() =  " + mScroller.getFinalY());
            // 更新界面
            postInvalidate();
        } else {
            if (mFinishFlag) {
                this.setVisibility(View.GONE);
            }
        }
    }


}
