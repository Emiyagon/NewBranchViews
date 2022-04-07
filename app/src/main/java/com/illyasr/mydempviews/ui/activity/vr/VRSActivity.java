
package com.illyasr.mydempviews.ui.activity.vr;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.BitmapFactory;
import android.os.Bundle;

import com.google.vr.sdk.widgets.pano.VrPanoramaEventListener;
import com.google.vr.sdk.widgets.pano.VrPanoramaView;
import com.illyasr.mydempviews.MainPresent;
import com.illyasr.mydempviews.R;
import com.illyasr.mydempviews.base.BaseActivity;
import com.illyasr.mydempviews.databinding.ActivityVrsactivityBinding;
import com.illyasr.mydempviews.util.BitmapUtil;

public class VRSActivity extends BaseActivity<ActivityVrsactivityBinding, MainPresent> {
    private VrPanoramaView.Options paNormalOptions;
    private static final String IMAGE_URL = "https://d22779be5rhkgh.cloudfront.net/_w9QbsGPj6JmY8pdkR2Pjw/thumbnails/360_180_source_102.jpg-video.vr.medium";
    @Override
    protected void initData() {
        initVrPaNormalView();
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_vrsactivity;
    }

    //初始化VR图片
    private void initVrPaNormalView() {

        paNormalOptions = new VrPanoramaView.Options();
        paNormalOptions.inputType = VrPanoramaView.Options.TYPE_STEREO_OVER_UNDER;
//        paNormalView.setFullscreenButtonEnabled (false); //隐藏全屏模式按钮
        mBindingView.paNormalView.setInfoButtonEnabled(false); //设置隐藏最左边信息的按钮
        mBindingView.paNormalView.setStereoModeButtonEnabled(false); //设置隐藏立体模型的按钮
        mBindingView.paNormalView.setEventListener(new VrPanoramaEventListener(){
            @Override
            public void onLoadSuccess() {//加载成功
                super.onLoadSuccess();
            }

            @Override
            public void onLoadError(String errorMessage) {//加载失败
                super.onLoadError(errorMessage);
            }

            @Override
            public void onClick() {//点击
                super.onClick();

            }

            @Override
            public void onDisplayModeChanged(int newDisplayMode) {
                // 改变显示模式时候触发(全屏模式和纸板模式)
                super.onDisplayModeChanged(newDisplayMode);
            }
        }); //设置监听
        //加载本地的图片源
        mBindingView.paNormalView.loadImageFromBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.andes), paNormalOptions);
        //设置网络图片源
//        mBindingView.paNormalView.loadImageFromByteArray();
        BitmapUtil.returnBitMap(IMAGE_URL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mBindingView.paNormalView.pauseRendering();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mBindingView.paNormalView.resumeRendering();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBindingView.paNormalView.shutdown();
    }
}