package com.illyasr.mydempviews.view.rainbow;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by 符柱成
 *
 * 自定义TextView之彩色字体
 */
@SuppressLint("AppCompatCustomView")
public class ColourfulFontTextview extends TextView {

    int TextViewWidth;                      //TextView的宽度
    private LinearGradient mLinearGradient;     //渲染器
    private Paint paint;
    private int[] rainbow =  new int[]{Color.BLUE, Color.YELLOW, Color.RED, Color.GREEN, Color.GRAY};
    private int[] rainbows=  new int[]{0xffFFF0F5, 0xffFF1493, 0xffFF00FF, 0xff9400D3, 0xff9932CC, 0xff8A2BE2 , 0xff1E90FF , 0xffF0F8FF,
    0xff00BFFF,  0xff00FFFF  ,0xff00FF7F ,0xff00FF00  ,0xff7FFF00 , 0xffFFFF00 , 0xffFFA500 ,0xffFF4500 ,0xffFF0000 ,0xffF5F5F5
    };

    public ColourfulFontTextview(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //在 onSizeChanged 方法中获取到宽度，并对各个类进行初始化
        if (TextViewWidth == 0) {
            TextViewWidth = getMeasuredWidth();
            if (TextViewWidth > 0) {
                //得到 父类 TextView 中写字的那支笔
                paint = getPaint();
                //初始化线性渲染器
                mLinearGradient = new LinearGradient(0, 0, TextViewWidth, 0,
                        rainbows, null, Shader.TileMode.CLAMP);
                //把渲染器给笔套上
                paint.setShader(mLinearGradient);

            }
        }
    }
}