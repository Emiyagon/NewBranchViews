package com.illyasr.mydempviews.ui.activity.canender;

import android.util.Log;

import com.illyasr.mydempviews.MainPresent;
import com.illyasr.mydempviews.R;
import com.illyasr.mydempviews.base.BaseFragment;
import com.illyasr.mydempviews.databinding.FragmentBaseEmiuiCalenderBinding;
import com.illyasr.mydempviews.view.dialog.TimeDialog;
import com.necer.calendar.BaseCalendar;
import com.necer.entity.CalendarDate;
import com.necer.entity.Lunar;
import com.necer.enumeration.CalendarState;
import com.necer.enumeration.DateChangeBehavior;
import com.necer.listener.OnCalendarChangedListener;
import com.necer.listener.OnCalendarMultipleChangedListener;
import com.necer.utils.CalendarUtil;

import org.joda.time.LocalDate;

import java.util.List;

/**
 * TODO
 *
 * @author qingshilin
 * @version 1.0
 * @date 2022/4/17 01:34
 */
public class EMIUICalenderFragment extends BaseFragment<FragmentBaseEmiuiCalenderBinding, MainPresent> {
    @Override
    public int setContent() {
        return R.layout.fragment_base_emiui_calender;
    }

    @Override
    protected void initView() {


        mBindingView.emuiCalendar.setOnCalendarChangedListener((baseCalendar, year, month, localDate, dateChangeBehavior) -> {
            mBindingView.tvResult.setText(year + "年" + month + "月" + "   当前页面选中 " + localDate);
//            mBindingView.tvData.setText(year + "年" + month + "月" + "   当前页面选中 " + localDate);
            Log.d(TAG, "当前页面选中：：" + localDate);
            Log.e(TAG, "baseCalendar::" + baseCalendar);
        });
        mBindingView.emuiCalendar.setOnCalendarMultipleChangedListener((baseCalendar, year, month, currPagerCheckedList, totalCheckedList, dateChangeBehavior) -> {
            mBindingView.tvResult.setText(year + "年" + month + "月" + " 当前页面选中 " + currPagerCheckedList.size() + "个  总共选中" + totalCheckedList.size() + "个");
//            mBindingView.tvData.setText(year + "年" + month + "月" + " 当前页面选中 " + currPagerCheckedList.size() + "个  总共选中" + totalCheckedList.size() + "个");
            Log.d(TAG, year + "年" + month + "月");
            Log.d(TAG, "当前页面选中：：" + currPagerCheckedList);
            Log.d(TAG, "全部选中：：" + totalCheckedList);
        });
        mBindingView.emuiCalendar.setOnCalendarChangedListener((baseCalendar, year, month, localDate, dateChangeBehavior) -> {
            if (localDate != null) {
                CalendarDate calendarDate = CalendarUtil.getCalendarDate(localDate);
                Lunar lunar = calendarDate.lunar;
                mBindingView.tvData.setText(localDate.toString("yyyy年MM月dd日"));
                mBindingView.tvDesc.setText(lunar.chineseEra + lunar.animals + "年" + lunar.lunarMonthStr + lunar.lunarDayStr);
            } else {
                mBindingView.tvData.setText("");
                mBindingView.tvDesc.setText("");
            }
        });

        //----
        mBindingView.stv1.setOnClickListener(v -> {
            new TimeDialog(getContext())
                    .setGetFinger(false)
                    .setOnDialogSelector((y, m, d) -> {
                        mBindingView.emuiCalendar.jumpDate(y, m, d);
                    })
                    .show();
        });

        mBindingView.stvToday.setOnClickListener(v -> mBindingView.emuiCalendar.toToday());
        mBindingView.stv2.setOnClickListener(v -> {
            if (mBindingView.emuiCalendar.getCalendarState() == CalendarState.WEEK) {
                mBindingView.emuiCalendar.toMonth();
            } else {
                mBindingView.emuiCalendar.toWeek();
            }
        });
    }
}
