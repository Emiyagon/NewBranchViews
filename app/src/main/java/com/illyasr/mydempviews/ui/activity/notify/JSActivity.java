package com.illyasr.mydempviews.ui.activity.notify;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.illyasr.mydempviews.MainPresent;
import com.illyasr.mydempviews.R;
import com.illyasr.mydempviews.base.BaseActivity;
import com.illyasr.mydempviews.databinding.ActivityJsactivityBinding;
import com.illyasr.mydempviews.ui.activity.HealthyActivity;
import com.illyasr.mydempviews.view.dialog.InputDialog;
import com.illyasr.mydempviews.view.dialog.TimeDialog;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.impl.InputConfirmPopupView;
import com.lxj.xpopup.interfaces.OnInputConfirmListener;

import java.util.Calendar;
import java.util.Date;

public class JSActivity extends BaseActivity<ActivityJsactivityBinding, MainPresent> {

    private long up = 0;
    private long next = 0;
    private long dayTime = 1*24*60*60*1000;
    private Calendar calendar = Calendar.getInstance();
    private Date startDate = null;
    private Date endDate = null;
    @Override
    protected void initData() {

        mBindingView.title.tvTitle.setText("日期计算");
//        init();
        //之前几天
        mBindingView.tvD1.setOnClickListener(v -> {
            init(number -> {
                mBindingView.tvD1.setText(String.format("之前%d天",number));
                up = number * dayTime;
                OnSetIt(startDate==null?new Date().getTime() - up : startDate.getTime()-up,mBindingView.tvD1a);

            });
        });
        //之后几天
        mBindingView.tvD2.setOnClickListener(v -> {
            init(it ->{
                mBindingView.tvD2.setText(String.format("之后%d天",it));
                next = dayTime * it;
                OnSetIt(startDate==null?new Date().getTime() + next:startDate.getTime()+next,mBindingView.tvD2a);
            } );
        });

        mBindingView.tvStart.setOnClickListener(v -> {
            new TimeDialog(JSActivity.this)
                    .setOnDialogSelector((y, m, d) -> {
                        calendar.set(y, m-1 , d);
                        startDate = new Date(calendar.getTimeInMillis());
                        mBindingView.tvStart.setText(String.format("%d/%d/%d/%s", calendar.get(Calendar.YEAR),
                                calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH),
                                showWeek(calendar.get(Calendar.DAY_OF_WEEK))));
                        sendMyTime();

                    }).show();
        });

        mBindingView.tvEnd.setOnClickListener(v -> {
            new TimeDialog(JSActivity.this)
                    .setOnDialogSelector((y, m, d) -> {
                        calendar.set(y, m-1 , d);
                        endDate = new Date(calendar.getTimeInMillis());
                        mBindingView.tvEnd.setText(String.format("%d/%d/%d/%s", calendar.get(Calendar.YEAR),
                                calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH),
                                showWeek(calendar.get(Calendar.DAY_OF_WEEK))));
                        sendMyTime();
                    }).show();
        });

    }

    private void sendMyTime() {
        if (startDate == null || endDate == null) {
            mBindingView.tvcancer.setText("0");
            return;
        }
        long time = (endDate.getTime() - startDate.getTime())/dayTime;
        mBindingView.tvcancer.setText(time+"");
    }

    private String showWeek(int weekDay) {
        switch (weekDay) {
            default:
                return "星期⼀";
            case Calendar.MONDAY:
                return "星期⼀";
            case Calendar.TUESDAY:
                return "星期二";
            case Calendar.WEDNESDAY:
                return "星期三";
            case Calendar.THURSDAY:
                return "星期四";
            case Calendar.FRIDAY:
                return "星期五";
            case Calendar.SATURDAY:
                return "星期六";
            case Calendar.SUNDAY:
                return "星期日";
        }
    }

    public void OnSetIt(long time,TextView view ){
        calendar.setTimeInMillis(time);
        view.setText(String.format("%d/%d/%d/%s",calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH)+1,calendar.get(Calendar.DAY_OF_MONTH),
                showWeek(calendar.get(Calendar.DAY_OF_WEEK)) ));
    }


    public interface OnDialogInterface {
        void OnIt(int number);
    }
    private void init(OnDialogInterface type) {
        new InputDialog(JSActivity.this,2)

                .setOnDialogListener(new InputDialog.OnDialogListener() {
                    @Override
                    public void onEnter(String s) {
//                        weight = Double.parseDouble(s);
//                        mBindingView.tvWeight.setText(s);
//                        onBelow();
                        if (type == null) {
                            return;
                        }
                        type.OnIt(Integer.parseInt(s));
                    }

                    @Override
                    public void onDismiss() {

                    }
                })
                .show();
     /*   XPopup.Builder builder=    new XPopup.Builder(getContext());
        builder.hasStatusBarShadow(false)
                .dismissOnBackPressed(false)
                .autoOpenSoftInput(true)
                .isDarkTheme(false)
        ;
        InputConfirmPopupView view =
                builder.asInputConfirm("提示", null, null, "请输入数字",
                        text -> {
                            if (type == null) {
                                return;
                            }
                            type.OnIt(Integer.parseInt(text));
                        });
        if (view != null) {
            view.show();
        }*/

    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_jsactivity;
    }
}