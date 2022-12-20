package com.illyasr.mydempviews.ui.activity.mlkit;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.camera.view.PreviewView;

import com.illyasr.mydempviews.MainPresent;
import com.illyasr.mydempviews.R;
import com.illyasr.mydempviews.base.BaseFragment;
import com.illyasr.mydempviews.databinding.FragmentMlkitCodeBinding;
import com.illyasr.mydempviews.ui.activity.tts.CompassFragment;
import com.king.mlkit.vision.barcode.analyze.BarcodeScanningAnalyzer;
import com.king.mlkit.vision.camera.AnalyzeResult;
import com.king.mlkit.vision.camera.BaseCameraScan;
import com.king.mlkit.vision.camera.CameraScan;
import com.king.mlkit.vision.camera.analyze.Analyzer;
import com.king.mlkit.vision.camera.config.CameraConfig;

import org.jetbrains.annotations.Nullable;

import java.util.Calendar;

/**
 * TODO
 *
 * @author qingshilin
 * @version 1.0
 * @date 2022/12/8 14:58
 */
public class BarCodeFragment extends BaseFragment<FragmentMlkitCodeBinding, MainPresent> {

    private CameraScan mCameraScan;

    public static BarCodeFragment newInstance() {
        BarCodeFragment fragment = new BarCodeFragment();
        Bundle bundle = new Bundle();

        return fragment;
    }

    @Override
    public int setContent() {
        return R.layout.fragment_mlkit_code;
    }

    @Override
    protected void initView() {
        //获取CameraScan，扫码相关的配置设置。
        mCameraScan = createCameraScan(mBindingView.previewView)
                .setAnalyzer(createAnalyzer())
                .setOnScanResultCallback(result -> {

                });
        // CameraScan里面包含部分支持链式调用的方法，即调用返回是CameraScan本身的一些配置建议在startCamera之前调用。
        mCameraScan.setPlayBeep(true)//设置是否播放音效，默认为false
                .setVibrate(true)//设置是否震动，默认为false
                .setCameraConfig(new CameraConfig())//设置相机配置信息，CameraConfig可覆写options方法自定义配置
                .setNeedTouchZoom(true)//支持多指触摸捏合缩放，默认为true
                .setDarkLightLux(45f)//设置光线足够暗的阈值（单位：lux），需要通过{@link #bindFlashlightView(View)}绑定手电筒才有效
                .setBrightLightLux(100f)//设置光线足够明亮的阈值（单位：lux），需要通过{@link #bindFlashlightView(View)}绑定手电筒才有效
                .bindFlashlightView(mBindingView.ivFlashlight)//绑定手电筒，绑定后可根据光线传感器，动态显示或隐藏手电筒按钮
                .setOnScanResultCallback(result -> {
                    showToast(result.toString());
                })//设置扫码结果回调，需要自己处理或者需要连扫时，可设置回调，自己去处理相关逻辑
                .setAnalyzer(new BarcodeScanningAnalyzer())//设置分析器，如这里使用条码分析器，BarcodeScanningAnalyzer是mlkit-barcode-scanning中的
                .setAnalyzeImage(true)//设置是否分析图片，默认为true。如果设置为false，相当于关闭了扫码识别功能
                .startCamera();//启动预览（如果是通过直接或间接继承BaseCameraScanActivity或BaseCameraScanFragment实现的则无需调用这句）


        //设置闪光灯（手电筒）是否开启,需在startCamera之后调用才有效
        int h = mCalendar.get(Calendar.HOUR_OF_DAY);
        boolean isC = h > 6 && h < 18;
        mCameraScan.enableTorch(!isC);
    }

    /**
     * 创建{@link CameraScan}
     * @param previewView
     * @return
     */
    public <T> CameraScan<T> createCameraScan(PreviewView previewView){
        return new BaseCameraScan<T>(this,previewView);
    }

    /**
     * 创建分析器
     *
     * @return
     */
    @Nullable
    public Analyzer createAnalyzer() {
        return null;
    }
}
