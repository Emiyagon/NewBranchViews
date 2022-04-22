package com.illyasr.mydempviews.ui.activity.bilibili;

import static com.illyasr.mydempviews.ui.activity.QrCodeActivity.IMAGE_URL;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.KeyEvent;

import com.bumptech.glide.Glide;
import com.illyasr.bilibili.biliplayer.media.IjkPlayerView;
import com.illyasr.mydempviews.MainPresent;
import com.illyasr.mydempviews.R;
import com.illyasr.mydempviews.base.BaseActivity;
import com.illyasr.mydempviews.databinding.ActivityBiliBiliBinding;

public class BiliBiliActivity extends BaseActivity<ActivityBiliBiliBinding, MainPresent> {


    public static void getToPlayVideo(Context context, String video,boolean full) {
        context.startActivity(new Intent(context,BiliBiliActivity.class)
                .putExtra("full",full)
                .putExtra("video",video));
    }
    private static final String VIDEO_URL = "http://flv2.bn.netease.com/videolib3/1611/28/nNTov5571/SD/nNTov5571-mobile.mp4";
    @Override
    protected void initData() {

        String video = getIntent().getStringExtra("video");
        //	以下为配置接口，选择需要的调用
        Glide.with(this).load(IMAGE_URL).fitCenter().into(mBindingView.ijkplayer.mPlayerThumb);	// 显示界面图
        mBindingView.ijkplayer
                .init()				// 初始化，必须先调用
                .setTitle("这是个标题")	// 设置标题，全屏时显示
                .setSkipTip(1000*60*1) 	// 设置跳转提示
                .enableOrientation()	// 使能重力翻转
//                .setVideoPath(VIDEO_URL)	// 设置视频Url，单个视频源可用这个
                .setVideoPath(video)	// 设置视频Url，单个视频源可用这个
//                .alwaysFullScreen()//直接全屏,不推荐
//                .setVideoSource(null, VIDEO_URL, VIDEO_URL, VIDEO_URL, null)	// 设置视频Url，多个视频源用这个
                .setMediaQuality(IjkPlayerView.MEDIA_QUALITY_HIGH)	// 指定初始视频源
                .enableDanmaku()		// 使能弹幕功能
//                .setDanmakuSource(getResources().openRawResource(R.raw.comments))	// 添加弹幕资源，必须在enableDanmaku()后调用
                .start();	// 启动播放


    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_bili_bili;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mBindingView.ijkplayer.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mBindingView.ijkplayer.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBindingView.ijkplayer.onDestroy();
    }



    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mBindingView.ijkplayer.configurationChanged(newConfig);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (mBindingView.ijkplayer.handleVolumeKey(keyCode)) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        if (mBindingView.ijkplayer.onBackPressed()) {
            return;
        }
        super.onBackPressed();
    }

}