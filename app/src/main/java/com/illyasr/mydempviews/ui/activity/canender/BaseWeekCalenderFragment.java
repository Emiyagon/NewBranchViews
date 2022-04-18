package com.illyasr.mydempviews.ui.activity.canender;

import android.util.Log;

import com.illyasr.mydempviews.MainPresent;
import com.illyasr.mydempviews.R;
import com.illyasr.mydempviews.base.BaseFragment;
import com.illyasr.mydempviews.databinding.FragmentBaseWeekCalenderBinding;
import com.illyasr.mydempviews.view.dialog.TimeDialog;
import com.necer.calendar.BaseCalendar;
import com.necer.enumeration.CheckModel;
import com.necer.enumeration.DateChangeBehavior;
import com.necer.listener.OnCalendarChangedListener;
import com.necer.listener.OnCalendarMultipleChangedListener;

import org.joda.time.LocalDate;

import java.util.List;

/**
 * TODO
 *
 * @author qingshilin
 * @version 1.0
 * @date 2022/4/17 00:19
 */
public class BaseWeekCalenderFragment extends BaseFragment<FragmentBaseWeekCalenderBinding, MainPresent> {
    @Override
    public int setContent() {
        return R.layout.fragment_base_week_calender;
    }

    @Override
    protected void initView() {
        mBindingView.cb.setOnCheckedChangeListener((buttonView, isChecked) -> {
            mBindingView.weekCalendar.setCheckMode(isChecked? CheckModel.SINGLE_DEFAULT_CHECKED:CheckModel.SINGLE_DEFAULT_UNCHECKED);
        });

        mBindingView.weekCalendar.setOnCalendarChangedListener((baseCalendar, year, month, localDate, dateChangeBehavior) -> {
            mBindingView.tvResult.setText(year + "年" + month + "月" + "   当前页面选中 " + localDate);
        });

        mBindingView.weekCalendar.setOnCalendarMultipleChangedListener((baseCalendar, year, month,
                                                                        currPagerCheckedList, totalCheckedList, dateChangeBehavior) -> {
            mBindingView.tvResult.setText(year + "年" + month + "月" + " 当前页面选中 " + currPagerCheckedList.size() + "个  总共选中" + totalCheckedList.size() + "个");

            Log.d(TAG, year + "年" + month + "月");
            Log.d(TAG, "当前页面选中：：" + currPagerCheckedList);
            Log.d(TAG, "全部选中：：" + totalCheckedList);
        });

        mBindingView.stv1.setOnClickListener(v -> {
            new TimeDialog(getContext())
                    .setGetFinger(false)
                    .setOnDialogSelector((y, m, d) -> {
                        mBindingView.weekCalendar.jumpDate(y, m, d);
                    })
                    .show();
        });

        mBindingView.stvToday.setOnClickListener(v -> mBindingView.weekCalendar.toToday());
    }
}
