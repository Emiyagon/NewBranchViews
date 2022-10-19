package com.illyasr.mydempviews.ui.activity;

import static com.illyasr.mydempviews.util.BitmapUtil.drawable2Bitmap;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;

import com.illyasr.mydempviews.MainPresent;
import com.illyasr.mydempviews.R;
import com.illyasr.mydempviews.base.BaseActivity;
import com.illyasr.mydempviews.databinding.ActivityCuteBinding;
import com.illyasr.mydempviews.util.BitmapUtil;

public class CuteActivity extends BaseActivity<ActivityCuteBinding, MainPresent> {


    @Override
    protected void initData() {
//        BitmapUtil.saveBitmap(mBindingView.imgCut,"fire");

        Bitmap ar = BitmapUtil.drawable2Bitmap(mBindingView.imgCut.getDrawable());

        mBindingView.imgCut.setOnClickListener(v -> mBindingView.imgCut.setImageBitmap(cropBitmap(ar)));

    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_cute;
    }

    /**
     * 裁剪
     *
     * @param bitmap 原图
     * @return 裁剪后的图像
     */
    private Bitmap cropBitmap(Bitmap bitmap) {
        int w = bitmap.getWidth(); // 得到图片的宽，高
        int h = bitmap.getHeight();
        int cropWidth = w >= h ? h : w;// 裁切后所取的正方形区域边长
//        cropWidth /= 2;
//        int cropHeight = (int) (cropWidth / 1.2);
        int cropHeight = cropWidth;
        return Bitmap.createBitmap(bitmap, 0, 0, cropWidth, cropHeight, null, false);
    }
}