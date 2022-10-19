package com.illyasr.mydempviews.ui.activity.tts;

import android.os.Bundle;

import com.illyasr.mydempviews.MainPresent;
import com.illyasr.mydempviews.R;
import com.illyasr.mydempviews.base.BaseFragment;
import com.illyasr.mydempviews.databinding.FragmentOclockBinding;

/**
 * TODO
 *
 * @author qingshilin
 * @version 1.0
 * @date 2022/10/9 16:17
 */
public class OClockFragment extends BaseFragment<FragmentOclockBinding, MainPresent> {
    public static OClockFragment newInstance() {
        OClockFragment fragment = new OClockFragment();
        Bundle bundle = new Bundle();

        return fragment;
    }
    @Override
    public int setContent() {
        return R.layout.fragment_oclock;
    }

    @Override
    protected void initView() {

    }
}
