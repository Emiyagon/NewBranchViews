package com.illyasr.mydempviews.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import androidx.annotation.Nullable;

import com.illyasr.mydempviews.R;

public class ClockView2 extends View {
    private static final int DEFAULT_COLOR_LOWER = Color.parseColor("#1d953f");//下游颜色,lvse
    private static final int DEFAULT_COLOR_MIDDLE = Color.parseColor("#228fbd");//中间颜色,蓝色
    private static final int DEFAULT_COLOR_HIGH = Color.RED;//高的颜色
    private static final int DEAFAULT_COLOR_TITLE = Color.BLACK;//标题颜色
    private static final int DEFAULT_TEXT_SIZE_DIAL = 11;//转盘 字体大小
    private static final int DEFAULT_STROKE_WIDTH = 8;//线的宽度
    private static final int DEFAULT_RADIUS_DIAL = 128;//转盘半径
    private static final int DEAFAULT_TITLE_SIZE = 16;//标题大小
    private static final int DEFAULT_VALUE_SIZE = 28;//值的大小
    private static final int DEFAULT_ANIM_PLAY_TIME = 2000;//动画时间

    private int colorDialLower;//转盘下游颜色
    private int colorDialMiddle;//转盘中游颜色
    private int colorDialHigh;//转盘上游颜色
    private int textSizeDial;//转盘文字大小
    private int strokeWidthDial;//转盘中风宽度
    private String titleDial;//转盘标题
    private int titleDialSize;//转盘标题大小
    private int titleDialColor;//转盘标题颜色
    private int valueTextSize;//值的大小
    private int animPlayTime;//动画时间

    private int radiusDial;//转盘半径
    private int mRealRadius;//实际半径
    private float currentValue;//当前值

    private Paint arcPaint;//弧的画笔
    private RectF mRect;//矩形
    private Paint pointerPaint;//指针
    private Paint.FontMetrics fontMetrics;//字体度量
    private Paint titlePaint;//标题画笔
    private Path pointerPath;//指示器路径


    public ClockView2(Context context) {
        this(context, null);
    }

    public ClockView2(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ClockView2(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        //初始化属性
        initAttrs(context, attrs);
        initPaint();
    }
    private void initAttrs(Context context, AttributeSet attrs){
        //获得样式属性
        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.ClockView);
        colorDialLower = attributes.getColor(R.styleable.ClockView_color_dial_lower, DEFAULT_COLOR_LOWER);//转盘下游颜色
        colorDialMiddle = attributes.getColor(R.styleable.ClockView_color_dial_middle, DEFAULT_COLOR_MIDDLE);//转盘中游颜色
        colorDialHigh = attributes.getColor(R.styleable.ClockView_color_dial_high, DEFAULT_COLOR_HIGH);//转盘上游颜色
        textSizeDial = (int) attributes.getDimension(R.styleable.ClockView_text_size_dial, sp2px(DEFAULT_TEXT_SIZE_DIAL));//文字大小
        strokeWidthDial = (int) attributes.getDimension(R.styleable.ClockView_stroke_width_dial, dp2px(DEFAULT_STROKE_WIDTH));//线条宽度
        radiusDial = (int) attributes.getDimension(R.styleable.ClockView_radius_circle_dial, dp2px(DEFAULT_RADIUS_DIAL));//转盘半径周期
        titleDial = attributes.getString(R.styleable.ClockView_text_title_dial);//转盘标题
        titleDialSize = (int) attributes.getDimension(R.styleable.ClockView_text_title_size, dp2px(DEAFAULT_TITLE_SIZE));//转盘标题大小
        titleDialColor = attributes.getColor(R.styleable.ClockView_text_title_color, DEAFAULT_COLOR_TITLE);//转盘标题颜色
        valueTextSize = (int) attributes.getDimension(R.styleable.ClockView_text_size_value, dp2px(DEFAULT_VALUE_SIZE));//转盘值
        animPlayTime = attributes.getInt(R.styleable.ClockView_animator_play_time, DEFAULT_ANIM_PLAY_TIME);//动画时间
    }

    private void initPaint(){
        //圆弧画笔
        arcPaint = new Paint();
        arcPaint.setAntiAlias(true);//抗锯齿
        arcPaint.setStyle(Paint.Style.STROKE);//风格
        arcPaint.setStrokeWidth(strokeWidthDial);//转盘中风宽度

        //指针画笔
        pointerPaint = new Paint();
        pointerPaint.setAntiAlias(true);//抗锯齿
        pointerPaint.setTextSize(textSizeDial);//文字大小
        pointerPaint.setTextAlign(Paint.Align.CENTER);//排成一行 居中
        fontMetrics = pointerPaint.getFontMetrics();//获得字体度量

        //标题画笔
        titlePaint = new Paint();
        titlePaint.setAntiAlias(true);//抗锯齿
        titlePaint.setTextAlign(Paint.Align.CENTER);//排成一行 居中
        titlePaint.setFakeBoldText(true);//设置黑体

        //指针条
        pointerPath = new Path();
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);//获得测量宽的模式
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);//获得测量宽的大小
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);//获得测量高的模式
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);//获得测量高的大小

        int mWidth, mHeight;
        if (widthMode == MeasureSpec.EXACTLY){//精确的
            mWidth = widthSize;
        }else {
            mWidth = getPaddingLeft() + radiusDial * 2 + getPaddingRight();
            if (widthMode == MeasureSpec.AT_MOST){//大概
                mWidth = Math.min(mWidth, widthSize);
            }
        }

        if (heightMode == MeasureSpec.EXACTLY){//精确的
            mHeight = heightSize;
        }else {
            mHeight = getPaddingTop() + radiusDial * 2 + getPaddingBottom();
            if (heightMode == MeasureSpec.AT_MOST){//大概
                mHeight = Math.min(mHeight, heightSize);
            }
        }
        //设置测量的大小
        setMeasuredDimension(mWidth, mHeight);

        radiusDial = Math.min((getMeasuredWidth() - getPaddingLeft() - getPaddingRight()),
                (getMeasuredHeight() - getPaddingTop() - getPaddingBottom())) / 2;
        mRealRadius = radiusDial - strokeWidthDial / 2;//真实的半径
        mRect = new RectF(-mRealRadius, -mRealRadius, mRealRadius, mRealRadius);//矩形 左上右下
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawArc(canvas);//画弧
        drawPointerLine(canvas);//画指针线
        drawTitleDial(canvas);//画标题
        drawPointer(canvas);//画指针
    }
    //画弧
    private void drawArc(Canvas canvas){
        //画布转换
        canvas.translate(getPaddingLeft() + radiusDial, getPaddingTop() + radiusDial);
        arcPaint.setColor(colorDialLower);//转盘下游颜色
        canvas.drawArc(mRect, 135, 134, false, arcPaint);
        arcPaint.setColor(colorDialMiddle);//转盘中游颜色
        canvas.drawArc(mRect, 232, 162, false, arcPaint);
        arcPaint.setColor(colorDialHigh);//转盘高游颜色
        canvas.drawArc(mRect, 286, 119, false, arcPaint);
    }

    private float rollScrollDigree = 135;

    //画指针线
    private void drawPointerLine(Canvas canvas){
        //画布旋转
        canvas.rotate(rollScrollDigree);
        for (int i=0; i<101; i++){     //一共需要绘制101个表针

           /* if (i <= 20){
                pointerPaint.setColor(colorDialLower);
            }else if (i<= 80){
                pointerPaint.setColor(colorDialMiddle);
            }else {
                pointerPaint.setColor(colorDialHigh);
            }*/
            pointerPaint.setColor(0xffFFA500);

            if (i % 10 == 0){     //长表针
                pointerPaint.setStrokeWidth(6);// 整数针位
                canvas.drawLine(radiusDial, 0, radiusDial - strokeWidthDial - dp2px(15), 0, pointerPaint);

                drawPointerText(canvas, i);
            }else {    //短表针
                pointerPaint.setStrokeWidth(3);
                canvas.drawLine(radiusDial, 0, radiusDial - strokeWidthDial - dp2px(5), 0, pointerPaint);
            }
            canvas.rotate(2.7f);
        }
    }
    //画指针文字
    private void drawPointerText(Canvas canvas, int i){
        canvas.save();
        int currentCenterX = (int) (radiusDial - strokeWidthDial - dp2px(21) - pointerPaint.measureText(String.valueOf(i)) / 2);
        canvas.translate(currentCenterX, 0);
        canvas.rotate(360 - rollScrollDigree - 2.7f * i);        //坐标系总旋转角度为360度

        int textBaseLine = (int) (0 + (fontMetrics.bottom - fontMetrics.top) /2 - fontMetrics.bottom);
        canvas.drawText(String.valueOf(i/2), 0, textBaseLine, pointerPaint);
        canvas.restore();
    }
    //画标题的值
    private void drawTitleDial(Canvas canvas){
        titlePaint.setColor(titleDialColor);
        titlePaint.setTextSize(titleDialSize);
        canvas.rotate( -47.7f);       //恢复坐标系为起始中心位置
        if (!TextUtils.isEmpty(titleDial)){
            canvas.drawText(titleDial, 0, radiusDial*1 / 3, titlePaint);
        }

        if (currentValue <= 20){
            titlePaint.setColor(colorDialLower);
        }else if (currentValue <= 80){
            titlePaint.setColor(colorDialMiddle);
        }else {
            titlePaint.setColor(colorDialHigh);
        }
        titlePaint.setTextSize(valueTextSize);
        //  画参数
        canvas.drawText(currentValue/2 + "%", 0, radiusDial * 2/3, titlePaint);
    }
    //画旋转的指针
    private void drawPointer(Canvas canvas){
        int currentDegree = (int) (currentValue * 2.7 + rollScrollDigree);
        canvas.rotate(currentDegree);

        pointerPath.moveTo(radiusDial - strokeWidthDial - dp2px(12), 0);
        pointerPath.lineTo(0, -dp2px(5));
        pointerPath.lineTo(-12, 0);
        pointerPath.lineTo(0, dp2px(5));
        pointerPath.close();
        titlePaint.setColor(0xffA52A2A);
        canvas.drawPath(pointerPath,titlePaint);
    }

    //设置完成程度
    public void setCompleteDegree(float degree){

        ValueAnimator animator = ValueAnimator.ofFloat(0, degree);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                currentValue = (float)(Math.round((float) animation.getAnimatedValue() * 100)) / 100;
                invalidate();
            }
        });
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.setDuration(animPlayTime);
        animator.start();
    }
    protected int dp2px(int dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpVal, getResources().getDisplayMetrics());
    }

    protected int sp2px(int spVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spVal, getResources().getDisplayMetrics());
    }

    public void setNum(float degree){
        currentValue = degree;
        invalidate();
    }

    public void setCavalTitle(String title) {
        titleDial = title;
        invalidate();
    }

    public void setIts(float degree,String title){
        ValueAnimator animator = ValueAnimator.ofFloat(0, degree);
        animator.addUpdateListener(animation -> {
            titleDial = title;
            currentValue = (float)(Math.round((float) animation.getAnimatedValue() * 100)) / 100;
            invalidate();
        });
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.setDuration(animPlayTime);
        animator.start();
    }

    public void setNumAnimator(float degree){
        ValueAnimator animator = ValueAnimator.ofFloat(currentValue, degree);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                currentValue = (float)(Math.round((float) animation.getAnimatedValue() * 100)) / 100;
                invalidate();
            }
        });
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.setDuration(1000);
        animator.start();
    }
}
