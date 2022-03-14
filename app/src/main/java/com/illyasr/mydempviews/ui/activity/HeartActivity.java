package com.illyasr.mydempviews.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.illyasr.mydempviews.MainPresent;
import com.illyasr.mydempviews.R;
import com.illyasr.mydempviews.adapter.TestAdapter;
import com.illyasr.mydempviews.base.BaseActivity;
import com.illyasr.mydempviews.base.adapters.BaseAdapter;
import com.illyasr.mydempviews.base.adapters.BaseHolder;
import com.illyasr.mydempviews.databinding.ActivityHeartBinding;
import com.illyasr.mydempviews.util.RxTimerUtil;
import com.illyasr.mydempviews.util.WaveUtil;
import com.illyasr.mydempviews.view.chart.ChartView;
import com.jeremyliao.liveeventbus.LiveEventBus;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

public class HeartActivity extends BaseActivity<ActivityHeartBinding, MainPresent> {


    private WaveUtil waveUtil1,waveUtil2,waveUtil3;

    @Override
    protected int setLayoutId() {
        return R.layout.activity_heart;
    }


    @Override
    protected void initData() {

        RxTimerUtil.interval(10 * 60 * 1000, number -> {
            LiveEventBus.get().with("Login").post(true);
        });


        RecyclerView rv = new RecyclerView(this);
        rv.setLayoutManager(new LinearLayoutManager(this));
//        rv.setAdapter(new TestAdapter(this,null,R.layout.downloading_layout));



        LiveEventBus.get().with("login",Boolean.class).observe(this, aBoolean -> {

        });
        LinkedList<String> xRawData = new LinkedList<>();
        xRawData.add("05-19");
        xRawData.add("05-20");
        xRawData.add("05-21");
        xRawData.add("05-22");
        xRawData.add("05-23");
        xRawData.add("05-24");
        xRawData.add("05-25");
        xRawData.add("05-26");
        xRawData.add("05-27");
        xRawData.add("05-28");
        xRawData.add("05-29");
        LinkedList<Double> yList1 = new LinkedList<>();
        LinkedList<Double> yList2 = new LinkedList<>();
        LinkedList<Double> yList3 = new LinkedList<>();
        LinkedList<Double> yList4 = new LinkedList<>();
        for (int i = 0; i < xRawData.size(); i++) {
            yList1.add(Math.random()*4);
            yList2.add(Math.random()*6);
            yList3.add(Math.random()*12);
            yList4.add(Math.random()*19);
        }

//        ChartView c1 = new ChartView(this);
//        ChartView c2 = new ChartView(this);
//        ChartView c3 = new ChartView(this);
//        ChartView c4 = new ChartView(this);
        mBindingView.c1.setPaintColor(0xffff0000);
        mBindingView.c2.setPaintColor(0xff00ff00);
        mBindingView.c3.setPaintColor(0xff0000ff);
        mBindingView.c4.setPaintColor(0xfffeeeed);
        mBindingView.c1.setData(yList1 , xRawData , 8 , 1);
        mBindingView.c2.setData(yList2 , xRawData , 18 , 3);
        mBindingView.c3.setData(yList3 , xRawData , 28 , 4);
        mBindingView.c4.setData(yList3 , xRawData , 28 , 3);

//        mBindingView.frame.addView(mBindingView. c1);
//        mBindingView.frame.addView( mBindingView.c2);
//        mBindingView.frame.addView( mBindingView.c3);
//        mBindingView.frame.addView( mBindingView.c4);

//        RxTimerUtil.timer(13000, number -> mBindingView.frame.removeView(mBindingView.c4));


        waveUtil1 = new WaveUtil();

        waveUtil1.showWaveData(mBindingView.waveView,mBindingView.waveView1,mBindingView.waveView2);
//        waveUtil2.showWaveData(mBindingView.waveView1);
//        waveUtil3.showWaveData(mBindingView.waveView2);


        mBindingView.tps.setOnClickListener(v -> {
         if (tag%2==1){
             waveUtil1.stop();
         }else{
             waveUtil1.showWaveData(mBindingView.waveView,mBindingView.waveView1,
                     mBindingView.waveView2,mBindingView.waveView3,mBindingView.waveView4);
         }
            tag++;
        });

    }

    private int tag = 1;//1是开始,0是停止
    @Override
    protected void onDestroy() {
        super.onDestroy();
        waveUtil1.stop();
    }
}