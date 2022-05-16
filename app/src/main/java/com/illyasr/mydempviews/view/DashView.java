package com.illyasr.mydempviews.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

/**
 * TODO
 * 卦象图
 * @author Administrator
 * @version 1.0
 * @date 2022/5/13 9:22
 */
public class DashView extends View {
    public char[] dash ;// 这个代表离卦 ☲
    private Paint mPaint;
    private Paint transPaint;
    private int height = 20;
    private int shopLineWidth = 80;
    private int whiteLineWidth = 20;
    private String TAG = "TAG";

    public void setDash(char[] dash) {
        this.dash = dash;
        invalidate();
    }

    public DashView(Context context) {
        super(context);
    }

    public DashView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        transPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
//        mPaint.setColor(0xff333333);
        mPaint.setColor(0xff666666);
        transPaint.setColor(0xff333333);
        mPaint.setStrokeWidth(height);
        transPaint.setStrokeWidth(height);
//        shopLineWidth=(getWidth()-whiteLineWidth)/2;
        mPaint.setPathEffect(new DashPathEffect(new float[]{shopLineWidth, whiteLineWidth}, 0));//这个是画虚线
//        transPaint.setPathEffect(new DashPathEffect(new float[]{80, 0}, 0));

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.e(TAG, "widthMeasureSpec = " + widthMeasureSpec + "\n heightMeasureSpec = " + heightMeasureSpec);
        // 获取宽-测量规则的模式和大小
//        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        // 获取高-测量规则的模式和大小
//        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        // 设置wrap_content的默认宽 / 高值
        // 默认宽/高的设定并无固定依据,根据需要灵活设置
        // 类似TextView,ImageView等针对wrap_content均在onMeasure()对设置默认宽 / 高值有特殊处理,具体读者可以自行查看
        int mWidth = 400;//宽度绝对不会设置默认的,理论上都是写死的
        int mHeight = 400;
        if (getLayoutParams().width == ViewGroup.LayoutParams.WRAP_CONTENT
                && getLayoutParams().height == ViewGroup.LayoutParams.WRAP_CONTENT) {
            setMeasuredDimension(mWidth, mHeight);
        // 宽 / 高任意一个布局参数为= wrap_content时，都设置默认值
        } else if (getLayoutParams().width == ViewGroup.LayoutParams.WRAP_CONTENT) {
            setMeasuredDimension(mWidth, heightSize);
        } else if (getLayoutParams().height == ViewGroup.LayoutParams.WRAP_CONTENT) {
            setMeasuredDimension(widthSize, mHeight);
        }
    }

    /**
     *  从这里可以拿到真实的宽高
     * @param changed
     * @param left
     * @param top
     * @param right
     * @param bottom
     */
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        Log.e(TAG, "left = " + left + "\n top = " + top);
        Log.e(TAG, "right = " + right + "\n bottom = " + bottom);
        shopLineWidth =( right-left-whiteLineWidth )/2;
        mPaint.setPathEffect(new DashPathEffect(new float[]{shopLineWidth, whiteLineWidth}, 0));//这个是画虚线
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        setLayerType(LAYER_TYPE_SOFTWARE, null);
       /*
        canvas.drawLine(0,height*1,shopLineWidth*2+whiteLineWidth,height*1,mPaint);
        canvas.drawLine(0,height*3,shopLineWidth*2+whiteLineWidth,height*3,mPaint);
        canvas.drawLine(0,height*5,shopLineWidth*2+whiteLineWidth,height*5,transPaint);
        */
        if (dash==null||dash.length==0){
            return;
        }
        for (int i = 0 ; i < dash.length ; i++) {
            if (TextUtils.equals(dash[i]+"","1")){//dash[i].equals("1")
                canvas.drawLine(0,height*(i+1+2*i),shopLineWidth*2+whiteLineWidth,height*(i+1+2*i),transPaint);
            }else {
                canvas.drawLine(0,height*(i+1+2*i),shopLineWidth*2+whiteLineWidth,height*(i+1+2*i), mPaint);
            }

        }
    }
}
