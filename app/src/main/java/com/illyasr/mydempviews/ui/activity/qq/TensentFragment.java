package com.illyasr.mydempviews.ui.activity.qq;

import android.util.Log;
import android.view.View;

import com.illyasr.mydempviews.MainPresent;
import com.illyasr.mydempviews.R;
import com.illyasr.mydempviews.base.BaseFragment;
import com.illyasr.mydempviews.databinding.FragmentTensentBinding;
import com.jeremyliao.liveeventbus.LiveEventBus;

public class TensentFragment extends BaseFragment<FragmentTensentBinding, MainPresent> {
    @Override
    public int setContent() {
        return R.layout.fragment_tensent;
    }

    @Override
    protected void initView() {

        mBindingView.imgLeft.setOnClickListener(v -> {

            Log.e("TAG", "发送出去了1");
            mPresenter.maintitle.postValue(true);
        });
        mBindingView.imgLeft.setOnClickListener(v -> {
            Log.e("TAG", "发送出去了2");
            LiveEventBus.get().with("tensent").post(1);
        });

    }
}
