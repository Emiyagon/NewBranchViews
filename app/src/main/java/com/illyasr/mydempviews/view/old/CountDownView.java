package com.illyasr.mydempviews.view.old;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.illyasr.mydempviews.R;

import static com.illyasr.mydempviews.util.ConvertUtils.sp2px;


/**
 * Created by bullet on 2018/7/23.
 */

public class CountDownView extends View {
    //圆轮颜色
    private int mRingColor;
    //中心颜色
    private int mInnerColor = 0x00000000;
    //圆轮宽度
    private float mRingWidth;
    //圆轮进度值文本大小
    private int mRingProgessTextSize;
    //宽度
    private int mWidth;
    //高度
    private int mHeight;
    private Paint mPaint;
    //圆环的矩形区域
    private RectF mRectF;
    // 不显示计数器单独显示一串文字
    private String octionText = "跳过";
    private boolean isShowGdText = false;// 是否显示中间字体
    private boolean isEnd = false;// 是否结束

    public boolean isIsend() {
        return isEnd;
    }


    public CountDownView setOctionText(String octionText) {
        this.octionText = octionText;
        return this;
    }

    public CountDownView setShowGdText(boolean showGdText) {
        isShowGdText = showGdText;
        return this;
    }

    //
    private int mProgessTextColor;
    private int mCountdownTime;
    private float mCurrentProgress;
    private OnCountDownFinishListener mListener;

    public CountDownView(Context context) {
        this(context, null);
    }

    public CountDownView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CountDownView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CountDownView);
        mRingColor = a.getColor(R.styleable.CountDownView_ringColor, context.getResources().getColor(R.color.colorAccent));
        mInnerColor = a.getColor(R.styleable.CountDownView_innerColor, context.getResources().getColor(R.color.transparent));
        mRingWidth = a.getFloat(R.styleable.CountDownView_ringWidth, 4);
        mRingProgessTextSize = a.getDimensionPixelSize(R.styleable.CountDownView_progressTextSize, sp2px(context, 20));
        mProgessTextColor = a.getColor(R.styleable.CountDownView_progressTextColor, context.getResources().getColor(R.color.colorAccent));
        mCountdownTime = a.getInteger(R.styleable.CountDownView_countdownTime, 10);
        a.recycle();
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setAntiAlias(true);
        this.setWillNotDraw(false);
    }

    public void setCountdownTime(int mCountdownTime) {
        this.mCountdownTime = mCountdownTime;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
        mRectF = new RectF(0 + mRingWidth / 2, 0 + mRingWidth / 2,
                mWidth - mRingWidth / 2, mHeight - mRingWidth / 2);

    }


    private int getMinNum(int a1, int a2) {
        if (a1 > a2) {
            return a2;
        } else {
            return a1;
        }
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        /**
         * 圆心 默认是透明的
         */
        //颜色
        mPaint.setColor(mInnerColor);
        //空心
        mPaint.setStyle(Paint.Style.FILL);
        //宽度
        //圆形略小于圆环,虽然这样画是圆环直接盖在圆上,美观起见
        canvas.drawCircle(mWidth / 2, mHeight / 2, getMinNum(mWidth, mHeight) / 2 - mRingWidth / 3, mPaint);
        /**
         *圆环
         */
        //颜色
        mPaint.setColor(mRingColor);
        //空心
        mPaint.setStyle(Paint.Style.STROKE);
        //宽度
        mPaint.setStrokeWidth(mRingWidth);
        canvas.drawArc(mRectF, -90, mCurrentProgress - 360, false, mPaint);


        //绘制文本
        Paint textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setTextAlign(Paint.Align.CENTER);
        String text = isShowGdText ? octionText : mCountdownTime - (int) (mCurrentProgress / 360f * mCountdownTime) + "";
        textPaint.setTextSize(mRingProgessTextSize);
        textPaint.setColor(mProgessTextColor);

        //文字居中显示
        Paint.FontMetricsInt fontMetrics = textPaint.getFontMetricsInt();
        int baseline = (int) ((mRectF.bottom + mRectF.top - fontMetrics.bottom - fontMetrics.top) / 2);
        //text 是文字内容， x 和 y 是文字的坐标。但需要注意：这个坐标并不是文字的左上角，而是一个与左下角比较接近的位置。大概在这里：
        canvas.drawText(text, mRectF.centerX(), baseline, textPaint);
    }

    private ValueAnimator getValA(long countdownTime) {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 100);
        valueAnimator.setDuration(countdownTime);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.setRepeatCount(0);
        return valueAnimator;
    }
    /**
     * 开始倒计时
     */
    public void startCountDown() {
        setClickable(false);
        isEnd = false;
        ValueAnimator valueAnimator = getValA(mCountdownTime * 1000);
        valueAnimator.addUpdateListener(animation -> {
            float i = Float.valueOf(String.valueOf(animation.getAnimatedValue()));
            mCurrentProgress = (int) (360 * (i / 100f));
            invalidate();
        });
        valueAnimator.start();
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                //倒计时结束回调
                if (mListener != null&& !isEnd) {
                    mListener.countDownFinished();
                }
                setClickable(true);
            }

        });
    }

    /*
     * 终止倒计时
     * */
    public void stopCountDown(){
        isEnd = true;
        ValueAnimator valueAnimator = getValA(mCountdownTime * 1000);
        if (mCurrentProgress > 0) {
            valueAnimator.end();
        }
        //倒计时结束回调
        if (mListener != null) {
            mListener.countDownFinished();
        }
    }

    public void setAddCountDownListener(OnCountDownFinishListener mListener) {
        this.mListener = mListener;
    }
    public interface OnCountDownFinishListener {
        void countDownFinished();
    }


}