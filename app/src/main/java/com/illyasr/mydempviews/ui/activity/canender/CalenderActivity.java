package com.illyasr.mydempviews.ui.activity.canender;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import android.os.Bundle;
import android.view.View;

import com.illyasr.mydempviews.MainPresent;
import com.illyasr.mydempviews.R;
import com.illyasr.mydempviews.base.BaseActivity;
import com.illyasr.mydempviews.databinding.ActivityCalenderBinding;
import com.illyasr.mydempviews.ui.activity.GetVideoActivity;
import com.illyasr.mydempviews.util.DonwloadSaveImg;
import com.illyasr.mydempviews.view.MyAlertDialog;
import com.illyasr.mydempviews.view.spin.OnSpinMenuStateChangeListener;
import com.illyasr.mydempviews.view.spin.SMItemLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *   很多日历
 *   包含了你能想到的所有情况
 *   源码在下面(其实我已经直接放在这里了,github只是装个样子的)
 *   https://github.com/yannecer/NCalendar
 */
public class CalenderActivity extends BaseActivity<ActivityCalenderBinding, MainPresent> {


    private  String[] strs = {"单纯月历","单纯周历","MIUI9日历","MIUI10日历","EMIUI日历","日历自定义显示选中图形","测试拉伸","仿钉钉(自定义)"};
    private List<Fragment> fragments = new ArrayList<>();
    @Override
    protected void initData() {

        fragments.add(new BaseMonthCalenderFragment());
        fragments.add(new BaseWeekCalenderFragment());
        fragments.add(new MIUI9CalenderFragment());
        fragments.add(new MIUI10CalenderFragment());
        fragments.add(new EMIUICalenderFragment());//
        fragments.add(new MyCalenderFragment());//
        mBindingView.vp.setAdapter(new SelectorAdapter(getSupportFragmentManager(),fragments));


        mBindingView.tabLayout.setupWithViewPager(mBindingView.vp);

        mBindingView.tv1.setOnClickListener(v -> {
          new MyAlertDialog(this).builder()
                    .setTitle("提示")
                    .setMsg("所有的日历都是一个viewgroup\n里面是可以放别的view的\n但是只能有一个子view\n" +
                            "猜测博主写这个其实是参照了scrollview\n具体写法源码可见,网站已经写在注释里面了\n" +
                            "官方demo里面还有拉伸和模仿钉钉的,意义不大,就只是单纯的修改了选中样式,所以在此不再赘述")
                    .setPositiveButton("我明白了", v13 -> {



                    }).show();
        });


        // 设置标题
        mBindingView.spinMenu.setHintTextStrList(Arrays.asList(strs));
        //设置启动手势开启菜单
        mBindingView.spinMenu.setEnableGesture(true);

        //设置页面适配器
        mBindingView.spinMenu.setFragmentAdapter(new SelectorAdapter(getSupportFragmentManager(),fragments));

        //设置菜单改变时的监听器
        mBindingView.spinMenu.setOnSpinMenuStateChangeListener(new OnSpinMenuStateChangeListener() {
            @Override
            public void onMenuOpened() {
                // 打开时
            }

            @Override
            public void onMenuClosed() {
                //关闭时
            }
        });

    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_calender;
    }

    class SelectorAdapter extends FragmentPagerAdapter {
        List<Fragment> list;
        public SelectorAdapter(@NonNull FragmentManager fm,List<Fragment> list) {
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
            return strs[i];
        }
    }
}