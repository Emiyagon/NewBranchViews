package com.illyasr.mydempviews.ui.activity.canender;

import android.util.Log;
import android.view.View;

import com.illyasr.mydempviews.MainPresent;
import com.illyasr.mydempviews.R;
import com.illyasr.mydempviews.base.BaseFragment;
import com.illyasr.mydempviews.databinding.FragmentBaseMyCalenderBinding;
import com.illyasr.mydempviews.ui.activity.canender.painter.LigaturePainter;
import com.illyasr.mydempviews.ui.activity.canender.painter.StretchPainter;
import com.illyasr.mydempviews.ui.activity.canender.painter.TicketPainter;
import com.illyasr.mydempviews.view.dialog.TimeDialog;
import com.necer.entity.CalendarDate;
import com.necer.entity.Lunar;
import com.necer.enumeration.CalendarState;
import com.necer.enumeration.CheckModel;
import com.necer.utils.CalendarUtil;

import org.joda.time.LocalDate;

import java.util.HashMap;
import java.util.Map;

/**
 * TODO
 *
 * @author qingshilin
 * @version 1.0
 * @date 2022/4/17 01:58
 */
public class MyCalenderFragment extends BaseFragment<FragmentBaseMyCalenderBinding, MainPresent> {
    @Override
    public int setContent() {
        return R.layout.fragment_base_my_calender;
    }

    @Override
    protected void initView() {

        mBindingView.miui10Calendar.setOnCalendarChangedListener((baseCalendar, year, month, localDate, dateChangeBehavior) -> {
            mBindingView.tvResult.setText(year + "年" + month + "月" + "   当前页面选中 " + localDate);
            Log.e(TAG, "baseCalendar::" + baseCalendar);
        });

        mBindingView.miui10Calendar.setOnCalendarChangedListener((baseCalendar, year, month, localDate, dateChangeBehavior) -> {
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
        mBindingView.miui10Calendar.setOnCalendarScrollingListener(dy -> {
            Log.d(TAG, "onCalendarScrolling：：" + dy);
        });

        mBindingView.stv1.setOnClickListener(v -> {
            new TimeDialog(getContext())
                    .setGetFinger(false)
                    .setOnDialogSelector((y, m, d) -> {
                        mBindingView.miui10Calendar.jumpDate(y, m, d);
                    })
                    .show();
        });

        mBindingView.stvToday.setOnClickListener(v -> mBindingView.miui10Calendar.toToday());
        mBindingView.stv2.setOnClickListener(v -> {
            if (mBindingView.miui10Calendar.getCalendarState() == CalendarState.WEEK) {
                mBindingView.miui10Calendar.toMonth();
            } else {
                mBindingView.miui10Calendar.toWeek();
            }
        });

        // 本来未知,猜测是因为这句话才湿的这一切可以点击
        mBindingView.miui10Calendar.setCheckMode(CheckModel.MULTIPLE);
        LigaturePainter painter = new LigaturePainter(getContext());
        mBindingView.miui10Calendar.setCalendarPainter(painter);

        mBindingView.st1.setOnClickListener(v -> {
            Line(v);
        });
        mBindingView.st2.setOnClickListener(v -> {
            Ticket(v);
        });
        mBindingView.st3.setOnClickListener(v -> {
            Unknown(v);
        });
    }

    public void Line(View view) {
        LigaturePainter painter = new LigaturePainter(getContext());
        mBindingView.miui10Calendar.setCalendarPainter(painter);
    }

    public void Ticket(View view) {
        TicketPainter ticketPainter = new TicketPainter(getContext(), mBindingView.miui10Calendar);

        Map<LocalDate, String> priceMap = new HashMap<>();
        priceMap.put(new LocalDate("2021-12-11"), "￥350");
        priceMap.put(new LocalDate("2022-01-21"), "￥350");
        priceMap.put(new LocalDate("2022-02-21"), "￥350");
        priceMap.put(new LocalDate("2022-03-20"), "￥350");
        priceMap.put(new LocalDate("2022-04-01"), "￥350");
        priceMap.put(new LocalDate("2022-04-09"), "￥350");
        priceMap.put(new LocalDate("2022-04-20"), "￥350");
        priceMap.put(new LocalDate("2022-04-30"), "￥350");

        ticketPainter.setPriceMap(priceMap);

        mBindingView.miui10Calendar.setCalendarPainter(ticketPainter);
    }
    public void Unknown(View view) {
        StretchPainter stretchPainter = new StretchPainter(getContext());
        mBindingView.miui10Calendar.setCalendarPainter(stretchPainter);
    }
}
