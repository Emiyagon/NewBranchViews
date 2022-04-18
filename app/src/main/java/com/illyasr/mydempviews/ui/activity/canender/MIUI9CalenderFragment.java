package com.illyasr.mydempviews.ui.activity.canender;

import android.util.Log;
import android.view.View;

import com.illyasr.mydempviews.MainPresent;
import com.illyasr.mydempviews.R;
import com.illyasr.mydempviews.base.BaseFragment;
import com.illyasr.mydempviews.databinding.FragmentBaseMiui9CalenderBinding;
import com.illyasr.mydempviews.view.dialog.TimeDialog;
import com.necer.enumeration.CalendarState;
/**
 * TODO
 * 这个是可以放别的控件的,现在模拟只放了几张图片看效果罢了,实际情况是要放rv的一般
 * 这个默认是可以折叠的,里面也有外部调用的折叠收起的方法
 * @author qingshilin
 * @version 1.0
 * @date 2022/4/17 00:42
 */
public class MIUI9CalenderFragment extends BaseFragment<FragmentBaseMiui9CalenderBinding, MainPresent> {
    @Override
    public int setContent() {
        return R.layout.fragment_base_miui9_calender;
    }

    @Override
    protected void initView() {
        mBindingView.miui9Calendar.setOnCalendarChangedListener((baseCalendar, year, month, localDate, dateChangeBehavior) -> {
            mBindingView.tvResult.setText(year + "年" + month + "月" + "   当前页面选中 " + localDate);
            Log.e(TAG, "baseCalendar::" + baseCalendar);
        });
        mBindingView.miui9Calendar.setOnCalendarMultipleChangedListener((baseCalendar, year, month, currPagerCheckedList,
                                                                         totalCheckedList, dateChangeBehavior) -> {
            mBindingView.tvResult.setText(year + "年" + month + "月" + " 当前页面选中 " + currPagerCheckedList.size() +
                    "个  总共选中" + totalCheckedList.size() + "个");
            Log.d(TAG, year + "年" + month + "月");
            Log.d(TAG, "当前页面选中：：" + currPagerCheckedList);
            Log.d(TAG, "全部选中：：" + totalCheckedList);
            Log.e(TAG, "baseCalendar::" + baseCalendar);
        });
        mBindingView.miui9Calendar.setOnCalendarScrollingListener(dy -> {
            Log.d(TAG, "onCalendarScrolling：：" + dy);
        });

        mBindingView.stv1.setOnClickListener(v -> {
            new TimeDialog(getContext())
                    .setGetFinger(false)
                    .setOnDialogSelector((y, m, d) -> {
                        mBindingView.miui9Calendar.jumpDate(y, m, d);
                    })
                    .show();
        });

        mBindingView.stvToday.setOnClickListener(v -> mBindingView.miui9Calendar.toToday());
        mBindingView.stv2.setOnClickListener(v -> {
            if (mBindingView.miui9Calendar.getCalendarState() == CalendarState.WEEK) {
                mBindingView.miui9Calendar.toMonth();
            } else {
                mBindingView.miui9Calendar.toWeek();
            }
        });

    }
}
