package com.illyasr.mydempviews.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;

import com.illyasr.mydempviews.R;
import com.illyasr.mydempviews.adapter.NoticeAdapter;

import java.util.ArrayList;
import java.util.List;

//
public class BarrageActivity extends AppCompatActivity implements View.OnClickListener {
    private Context mContext;

    //公告
    private RecyclerView notice;//公告
    private List<String> mDatas;//公告信息
    private boolean flag = false;//公告栏 信息控制切换页面时，公告是否滚动
    private long scrollTime = 2000;

    private int scrollB = 1 ;// 速度倍数系数,基数是10
    private int color = 0xffffffff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barrage);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        mContext = this;

        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();///获取前面的那个activity传过来的数据
        color = intent.getIntExtra("color", 0xffffffff);

        setData(bundle.getString("msg"),intent.getIntExtra("speed",1));

        init();
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); //隐藏状态栏
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);//设置屏幕常亮
//        verticalRollTextView.startAutoScroll();
    }

    /**
     * 公告
     */
    private void noticeFunction(){
        initNotice();
    }

    private void init() {
        notice = findViewById(R.id.user_notice);
        noticeFunction();//初始化公告
    }

    /**
     * 公告信息初始化
     */
    private void setData(String msg,int speed){
        mDatas = new ArrayList<>();
        mDatas.add(msg+" ");
        scrollB = speed;
    }


    /**
     * 公告栏 信息时间控制
     */
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what==0x00){
                notice.scrollBy((notice.getScrollX()+2)*3*scrollB,notice.getScrollY()*1);
                handler.sendEmptyMessageDelayed(0x00, 10);
                if (!flag) {
                    flag = true;
                }
            }
            else if(msg.what==0x01){//刷新页面数据
                handler.sendEmptyMessageDelayed(0x01, scrollTime);
            }
        }
    };

    /**
     * 初始化公告
     */
    private void initNotice(){
        notice = findViewById(R.id.user_notice);
        notice.setOnClickListener(this);
//        initData();
        NoticeAdapter noticeAdapter = new NoticeAdapter(color);
        noticeAdapter.setmDatas(mDatas);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext,LinearLayoutManager.HORIZONTAL,false);
        notice.setLayoutManager(linearLayoutManager);
        notice.setAdapter(noticeAdapter);
        noticeAdapter.notifyDataSetChanged();
        handler.sendEmptyMessageDelayed(0x00,10);
        handler.sendEmptyMessageDelayed(0x01,10);
        handler.sendEmptyMessageDelayed(0x02,5000);
    }

    @Override
    public void onClick(View v) {

    }
}
