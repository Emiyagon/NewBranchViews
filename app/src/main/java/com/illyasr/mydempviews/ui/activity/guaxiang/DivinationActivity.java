package com.illyasr.mydempviews.ui.activity.guaxiang;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.illyasr.mydempviews.R;
import com.illyasr.mydempviews.base.BaseActivity;
import com.illyasr.mydempviews.databinding.ActivityDivinationBinding;
import com.illyasr.mydempviews.util.StringUtil;

public class DivinationActivity extends BaseActivity<ActivityDivinationBinding,DivinationPresent> {

    private String[] strs = new String[6];
    @Override
    protected void initData() {
        mPresenter.init();//初始化数据
        mBindingView.stvTz.setOnClickListener(v -> {
            showDialog("");
            for (int i = 0; i < 6; i++) {
              
                strs[5 - i] = ((int) (Math.random() * 2) + "");
                handler.sendEmptyMessageAtTime(20, 2000);
            }

        });
    }
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 20:
                    dismissDialog();
//                    mBindingView.stv13.setText(yingbis.toString());
                    mBindingView.stv13.setText(StringUtil.ListToString(strs));
                    mBindingView.dash.setVisibility(View.VISIBLE);
                    mBindingView.dash.setDash(mBindingView.stv13.getText().toString().toCharArray());
                    mPresenter.showResult(mBindingView.stv13.getText().toString(), mBindingView.t1,
                            mBindingView.t2,mBindingView.t3, mBindingView.t4);
                    break;
            }
        }
    };

    @Override
    protected int setLayoutId() {
        return R.layout.activity_divination;

    }
}