package com.illyasr.mydempviews.util;

import android.graphics.Color;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.SubscriptSpan;
import android.text.style.SuperscriptSpan;
import android.text.style.UnderlineSpan;

/**
 *  spannablestring工具类
 */
public class SpannableUtil {
    /**
     * 字体删除线
     */
    public static SpannableString TextDeleteLine(String str, int posStart, int posEnd) {
        /*
        SpannableString spannableString = new SpannableString("为文字设置删除线");
        StrikethroughSpan strikethroughSpan = new StrikethroughSpan();
        spannableString.setSpan(strikethroughSpan, 5, spannableString.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        textView.setText(spannableString);
         */
        SpannableString spannableString = new SpannableString(str);
        StrikethroughSpan strikethroughSpan = new StrikethroughSpan();
        spannableString.setSpan(strikethroughSpan, posStart, posEnd, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        return spannableString;
    }

    /**
     * 下划线
     */
    public static SpannableString TextUnderLine(String str,int posStart,int posEnd) {
        /*SpannableString spannableString = new SpannableString("为文字设置下划线");
        UnderlineSpan underlineSpan = new UnderlineSpan();
        spannableString.setSpan(underlineSpan, 5, spannableString.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        textView.setText(spannableString);*/
        SpannableString spannableString = new SpannableString(str);
        UnderlineSpan underlineSpan = new UnderlineSpan();
        spannableString.setSpan(underlineSpan, posStart,posEnd, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        return spannableString;
    }

    /**
     * 设置上标,和浮起状态一样
     */
    public static SpannableString TextUpFly(String str,int posStart,int posEnd) {
        /*SpannableString spannableString = new SpannableString("为文字设置下划线");
     SuperscriptSpan superscriptSpan = new SuperscriptSpan();
spannableString.setSpan(superscriptSpan, 5, spannableString.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        textView.setText(spannableString);*/
        SpannableString spannableString = new SpannableString(str);
        SuperscriptSpan superscriptSpan = new SuperscriptSpan();
        spannableString.setSpan(superscriptSpan, posStart,posEnd, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        return spannableString;
    }

    /**
     * 设置下标,和上标对应
     */
    public static SpannableString TextDownUnder(String str,int posStart,int posEnd) {
        /*SpannableString spannableString = new SpannableString("为文字设置下划线");
SubscriptSpan subscriptSpan = new SubscriptSpan();
spannableString.setSpan(subscriptSpan, 5, spannableString.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        textView.setText(spannableString);*/
        SpannableString spannableString = new SpannableString(str);
        SubscriptSpan subscriptSpan = new SubscriptSpan();
        spannableString.setSpan(subscriptSpan, posStart, posEnd, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        return spannableString;
    }

    /**
     * 字号改变 ,示例是山形文字
     * @param str
     * @param size
     * @param posStart
     * @param posEnd
     * @return
     */
    public static SpannableString StringSizeChange(String str,float size,int posStart,int posEnd) {
        /*
        * SpannableString spannableString = new SpannableString("万丈高楼平地起");

RelativeSizeSpan sizeSpan01 = new RelativeSizeSpan(1.2f);
RelativeSizeSpan sizeSpan02 = new RelativeSizeSpan(1.4f);
RelativeSizeSpan sizeSpan03 = new RelativeSizeSpan(1.6f);
RelativeSizeSpan sizeSpan04 = new RelativeSizeSpan(1.8f);
RelativeSizeSpan sizeSpan05 = new RelativeSizeSpan(1.6f);
RelativeSizeSpan sizeSpan06 = new RelativeSizeSpan(1.4f);
RelativeSizeSpan sizeSpan07 = new RelativeSizeSpan(1.2f);

spannableString.setSpan(sizeSpan01, 0, 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
spannableString.setSpan(sizeSpan02, 1, 2, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
spannableString.setSpan(sizeSpan03, 2, 3, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
spannableString.setSpan(sizeSpan04, 3, 4, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
spannableString.setSpan(sizeSpan05, 4, 5, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
spannableString.setSpan(sizeSpan06, 5, 6, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
spannableString.setSpan(sizeSpan07, 6, 7, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
textView.setText(spannableString);
        * */
        SpannableString spannableString = new SpannableString(str);
        RelativeSizeSpan sizeSpan = new RelativeSizeSpan(size);
        spannableString.setSpan(sizeSpan,posStart,posEnd,Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        return spannableString;
    }


    /**
     * 文字颜色改变
     * @param str 输入字
     * @param color 颜色,示例 "#0099EE"
     * @param posStart 开始改变位置(第一个是0开始计算)
     * @param posEnd 结束改变位置
     */
    public static SpannableString TextColorChange(String str,String color,int posStart,int posEnd) {
         /*  SpannableString spannableString = new SpannableString("设置文字的前景色为淡蓝色");
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#0099EE"));
        spannableString.setSpan(colorSpan, 9, spannableString.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        textView.setText(spannableString);*/
        SpannableString spannableString = new SpannableString(str);
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor(color));
        spannableString.setSpan(colorSpan, posStart, posEnd, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        return spannableString;
    }

    /**
     * 文字背景色改变
     * @param str
     * @param color
     * @param posStart
     * @param posEnd
     * @return
     */
    public static SpannableString TextBackgroundChange(String str,String color,int posStart,int posEnd) {
        /*
        SpannableString spannableString = new SpannableString("设置文字的背景色为淡绿色");
        BackgroundColorSpan colorSpan = new BackgroundColorSpan(Color.parseColor("#AC00FF30"));
        spannableString.setSpan(colorSpan, 9, spannableString.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        textView.setText(spannableString);
        */
        SpannableString spannableString = new SpannableString(str);
        BackgroundColorSpan colorSpan = new BackgroundColorSpan(Color.parseColor(color));
        spannableString.setSpan(colorSpan, posStart, posEnd, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        return spannableString;
    }

    /**
     * 粗体文本
     */
    public static SpannableString TextBlodChange(String str,int posStart,int posEnd) {
        SpannableString spannableString = new SpannableString(str);
        StyleSpan styleSpan_B  = new StyleSpan(Typeface.BOLD);
        spannableString.setSpan(styleSpan_B, posStart, posEnd, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        return spannableString;
    }


}
