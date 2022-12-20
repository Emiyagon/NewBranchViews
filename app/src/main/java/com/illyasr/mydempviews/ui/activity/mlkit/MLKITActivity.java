package com.illyasr.mydempviews.ui.activity.mlkit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import android.os.Bundle;

import com.illyasr.mydempviews.MainPresent;
import com.illyasr.mydempviews.R;
import com.illyasr.mydempviews.base.BaseActivity;
import com.illyasr.mydempviews.databinding.ActivityMlkitBinding;
import com.illyasr.mydempviews.ui.activity.tts.CompassFragment;
import com.illyasr.mydempviews.ui.activity.tts.OClockFragment;
import com.illyasr.mydempviews.ui.activity.tts.TTSActivity;

import java.util.ArrayList;
import java.util.List;

public class MLKITActivity extends BaseActivity<ActivityMlkitBinding, MainPresent> {


    @Override
    protected void initData() {


        List<Fragment> list = new ArrayList<>();
        list.add(BarCodeFragment.newInstance());
//        list.add(FaceFragment.newInstance());
        list.add(BarCodeFragment.newInstance());
//        list.add(BarCodeFragment.newInstance());
//        list.add(BarCodeFragment.newInstance());
//        list.add(BarCodeFragment.newInstance());
        mBindingView.vpr.setAdapter(new SelectorAdapter(getSupportFragmentManager(),list));
// 绑定，要在viewpager设置完数据后，调用此方法，否则不显示 tabs文本
        mBindingView.tabLayout.setupWithViewPager(mBindingView.vpr);

    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_mlkit;
    }


    class SelectorAdapter extends FragmentPagerAdapter {
        List<Fragment> list;
        String[] tabs = {
                "条码识别"
//                ,"人脸检测"
                ,"图像标记"
//                ,"pose检测"
//                ,"自拍分割"
//                ,"文字识别"
        };
        public SelectorAdapter(@NonNull FragmentManager fm, List<Fragment> list) {
            super(fm);
            this.list = list;
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return list.get(position);
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int i) {
//            return super.getPageTitle(position);
//            mBindingView.tv1.setText(strs[i]);
            return tabs[i];
        }
    }
}