package com.illyasr.mydempviews.ui.activity.canender;

import android.util.Log;

import com.illyasr.mydempviews.MainPresent;
import com.illyasr.mydempviews.R;
import com.illyasr.mydempviews.base.BaseFragment;
import com.illyasr.mydempviews.databinding.FragmentBaseMiui10CalenderBinding;
import com.illyasr.mydempviews.view.dialog.TimeDialog;
import com.necer.calendar.BaseCalendar;
import com.necer.entity.CalendarDate;
import com.necer.entity.Lunar;
import com.necer.enumeration.CalendarState;
import com.necer.enumeration.DateChangeBehavior;
import com.necer.listener.OnCalendarChangedListener;
import com.necer.mylunar.MyXuanri;
import com.necer.utils.CalendarUtil;

import org.joda.time.LocalDate;

/**
 * TODO
 *
 * @author qingshilin
 * @version 1.0
 * @date 2022/4/17 00:42
 */
public class MIUI10CalenderFragment extends BaseFragment<FragmentBaseMiui10CalenderBinding, MainPresent> {
    @Override
    public int setContent() {
        return R.layout.fragment_base_miui10_calender;
    }

    @Override
    protected void initView() {
//        mBindingView.miui9Calendar.setScrollEnable(false);//这个方法是禁止左右滑动改变日期的
// 目前没有发现哪个方法可以设置禁止手势然后让其禁止上下波动

        mBindingView.miui9Calendar.setOnCalendarChangedListener((baseCalendar, year, month, localDate, dateChangeBehavior) -> {
            mBindingView.tvResult.setText(year + "年" + month + "月" + "   当前页面选中 " + localDate);
            Log.e(TAG, "baseCalendar::" + baseCalendar);
        });

        mBindingView.miui9Calendar.setOnCalendarChangedListener((baseCalendar, year, month, localDate, dateChangeBehavior) -> {
            if (localDate != null) {
                CalendarDate calendarDate = CalendarUtil.getCalendarDate(localDate);
                Lunar lunar = calendarDate.lunar;
                mBindingView.tvData.setText(localDate.toString("yyyy年MM月dd日"));
                mBindingView.tvDesc.setText(lunar.chineseEra+
                        "年" + lunar.lunarMonthStr + lunar.lunarDayStr+new MyXuanri().getDateBaZiString(year,month,localDate.getDayOfMonth()));
            } else {
                mBindingView.tvData.setText("");
                mBindingView.tvDesc.setText("");
            }
        });
      /*  mBindingView.miui9Calendar.setOnCalendarMultipleChangedListener((baseCalendar, year, month, currPagerCheckedList,
                                                                         totalCheckedList, dateChangeBehavior) -> {
            mBindingView.tvResult.setText(year + "年" + month + "月" + " 当前页面选中 " + currPagerCheckedList.size() +
                    "个  总共选中" + totalCheckedList.size() + "个");
            Log.d(TAG, year + "年" + month + "月");
            Log.d(TAG, "当前页面选中：：" + currPagerCheckedList);
            Log.d(TAG, "全部选中：：" + totalCheckedList);
            Log.e(TAG, "baseCalendar::" + baseCalendar);
        });*/
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
