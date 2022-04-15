package com.illyasr.mydempviews.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.illyasr.mydempviews.MainPresent;
import com.illyasr.mydempviews.R;
import com.illyasr.mydempviews.base.BaseActivity;
import com.illyasr.mydempviews.databinding.ActivityPlayBinding;
import com.illyasr.mydempviews.view.ioswheel.OnItemSelectedListener;

public class PlayActivity extends BaseActivity<ActivityPlayBinding, MainPresent> {


    @Override
    protected void initData() {

        mBindingView.wleel2.setItems(getNumberList(1,11));
        mBindingView.wleel2.setListener((index, s) -> {
            mBindingView.stv21.setText(s + "--"+ (index+1));
            mBindingView.stv1.setText(MinesNum(index+1));
            mBindingView.stv22.setText(canNum(index + 1));
        });
    }

    private String MinesNum(int cun) {
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i < 500; i++) {
            if (i % cun == 0 || i / 10 == cun || i % 10 == cun // 到这里适配到100以内
                    || i / 10 % 10 == cun || i / 100 == cun) {
                sb.append(i + " ");
            }
        }

        return sb.toString();
    }

    private String canNum(int cun) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 500; i++) {
            if (i % cun != 0 || i / 10 != cun || i % 10 != cun // 到这里适配到100以内
                    || i / 10 % 10 != cun || i / 100 != cun) {
                sb.append(i + " ");
            }
        }
        return sb.toString();
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_play;
    }
}