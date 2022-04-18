package com.illyasr.mydempviews.ui.activity.canender;

import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;

import com.illyasr.mydempviews.MainPresent;
import com.illyasr.mydempviews.R;
import com.illyasr.mydempviews.base.BaseFragment;
import com.illyasr.mydempviews.databinding.FragmentBaseMonthCalenderBinding;
import com.illyasr.mydempviews.view.dialog.TimeDialog;
import com.necer.calendar.BaseCalendar;
import com.necer.enumeration.CheckModel;
import com.necer.enumeration.DateChangeBehavior;
import com.necer.listener.OnCalendarChangedListener;

import org.joda.time.LocalDate;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class BaseMonthCalenderFragment extends BaseFragment<FragmentBaseMonthCalenderBinding, MainPresent> {

    @Override
    public int setContent() {
        return R.layout.fragment_base_month_calender;
    }

    @Override
    protected void initView() {

        mBindingView.monthCalendar.setOnCalendarChangedListener((baseCalendar, year, month, localDate, dateChangeBehavior) -> {
            mBindingView.tvResult.setText(year + "年" + month + "月" + "   当前页面选中 " + localDate);

            Log.d(TAG, "setOnCalendarChangedListener:::" + year + "年" + month + "月" + "   当前页面选中 " + localDate);
            Log.e(TAG, "baseCalendar::" + baseCalendar);
        });
        mBindingView.monthCalendar.setOnCalendarChangedListener((baseCalendar, year, month, localDate, dateChangeBehavior) -> {
            mBindingView.tvResult.setText(year + "年" + month + "月" + "   当前页面选中 " + localDate);

            Log.d(TAG, "setOnCalendarChangedListener:::" + year + "年" + month + "月" + "   当前页面选中 " + localDate);
            Log.e(TAG, "baseCalendar::" + baseCalendar);
        });

        mBindingView.stv1.setOnClickListener(v -> {
            new TimeDialog(getContext())
                    .setGetFinger(false)
                    .setOnDialogSelector((y, m, d) -> {
                        mBindingView.monthCalendar.jumpDate(y, m, d);
                    })
                    .show();
        });

            mBindingView.stvToday.setOnClickListener(v -> mBindingView.monthCalendar.toToday());
        mBindingView.cb.setOnCheckedChangeListener((buttonView, isChecked) -> {
            mBindingView.monthCalendar.setCheckMode(isChecked? CheckModel.SINGLE_DEFAULT_CHECKED:CheckModel.SINGLE_DEFAULT_UNCHECKED);
        });

    }
}