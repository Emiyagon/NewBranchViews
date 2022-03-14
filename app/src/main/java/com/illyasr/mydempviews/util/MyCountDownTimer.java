package com.illyasr.mydempviews.util;

import android.graphics.Color;
import android.os.CountDownTimer;
import android.widget.TextView;

public class MyCountDownTimer extends CountDownTimer {
    TextView TimeText;

    public MyCountDownTimer(long millisInFuture, long countDownInterval, TextView tv) {
        super(millisInFuture, countDownInterval);
        TimeText = tv;
    }

    //计时过程
    @Override
    public void onTick(long l) {
        //防止计时过程中重复点击
        TimeText.setClickable(false);
        TimeText.setText("重新获取" + l / 1000 + "s");
        TimeText.setTextColor(Color.parseColor("#999999"));

    }

    //计时完毕的方法
    @Override
    public void onFinish() {
        //重新给Button设置文字
        TimeText.setText("重新获取");
        //设置可点击
        TimeText.setClickable(true);
        TimeText.setTextColor(Color.parseColor("#333333"));
    }
}