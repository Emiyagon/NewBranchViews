package com.illyasr.mydempviews.util;

import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Html;
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
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 基于spannablestring和html方式的文字实现工具类
 */
public class StringUtil {

    /**
     *
     * @param inputStream
     * @return
     */
    public static String getString(InputStream inputStream) {
        InputStreamReader inputStreamReader = null;
        StringBuffer sb = new StringBuffer("");
        try {
            inputStreamReader = new InputStreamReader(inputStream, "utf-8");
            BufferedReader reader = new BufferedReader(inputStreamReader);

            String line;

            while ((line = reader.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    /**
     * 使用HTML实现 ￥200.00 (￥缩小)的样式
     */
    public static void delatedHtmlMuch(TextView textView, String num) {

//        String   allLong ="<strong><font color=\"#5c6d94\"> "+  name +"</font></strong>"+" <font color=\"#999999\"><small>回复</small></font> "+"<strong><font color=\"#5c6d94\">"+ replyName
//                + "</font></strong> : "+ replyM + "";
        String span = "<strong><font color=\"#F0250F\"><small>"+  "¥" +"</small></font></strong>" + "\t<strong><font color=\"#F0250F\"><normal>"+  num +"</normal></font></strong>";
        textView.setText(Html.fromHtml(span));

    }
    public static void delatedHtmlMuch(TextView textView, String num,int color) {

//        String   allLong ="<strong><font color=\"#5c6d94\"> "+  name +"</font></strong>"+" <font color=\"#999999\"><small>回复</small></font> "+"<strong><font color=\"#5c6d94\">"+ replyName
//                + "</font></strong> : "+ replyM + "";
        String span = "<strong><font color=\"#3F96F8\"><small>"+  "¥" +"</small></font></strong>" + "\t<strong><font color=\"#3F96F8\"><normal>"+  num +"</normal></font></strong>";
        textView.setText(Html.fromHtml(span));

    }
    /**
     * //保留两位小数正则
     * @param number
     * @return
     */
    public static boolean isOnlyPointNumber(String number) {
        Pattern pattern = Pattern.compile("^\\d+\\.?\\d{0,2}$");
        Matcher matcher = pattern.matcher(number);
        return matcher.matches();
    }


    /**
     * 字体删除线
     */
    public static SpannableString TextDeleteLine(String str,int posStart,int posEnd) {
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

    /**
     * 获取固定区间的文字
     */
    public static String SubsmainString(String str,int start, int end) {
        return str.substring(start, end);
    }


    /**
     *  截取两个字段之间的文字
     * @param str
     * @param strStart
     * @param strEnd
     * @return
     */
    public static String subString(String str, String strStart, String strEnd) {

        /* 找出指定的2个字符在 该字符串里面的 位置 */
        int strStartIndex = str.indexOf(strStart);
        int strEndIndex = str.indexOf(strEnd)+1;

        /* index为负数 即表示该字符串中没有该字符 */
        if (strStartIndex < 0) {
            return "字符串 :" + str + "中不存在 " + strStart + ", 无法截取目标字符串";
        }
        if (strEndIndex < 0) {
            return "字符串 :" + str + "中不存在 " + strEnd + ", 无法截取目标字符串";
        }
        /* 开始截取 */
        String result = str.substring(strStartIndex, strEndIndex);
        return result;
    }


    public static String returnWeekString(int w) {
        String string = "星期";
        if (w == 7) {
            string = string + "日";
        }else if (w == 1) {
            string = string + "一";
        }else if (w == 2) {
            string = string + "二";
        }else if (w == 3) {
            string = string + "三";
        }else if (w == 4) {
            string = string + "四";
        }else if (w == 5) {
            string = string + "五";
        }else if (w == 6) {
            string = string + "六";
        }

        return string;
    }


    public static String ListToString(String[] st) {
        StringBuilder sb = new StringBuilder();
        for (String s: st) {
            sb.append(s);
        }
        return sb.toString();

    }

}
