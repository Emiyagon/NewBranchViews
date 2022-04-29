package com.illyasr.mydempviews.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.core.util.TimeUtils;
import androidx.databinding.DataBindingUtil;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.illyasr.mydempviews.R;
import com.illyasr.mydempviews.databinding.DialogCityBinding;
import com.illyasr.mydempviews.util.TimeUtil;
import com.illyasr.mydempviews.util.ToastUtils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * TODO
 *
 * @author qingshilin
 * @version 1.0
 * @date 2022/4/15 17:31
 */
public class TimeDialog extends Dialog {

    private View contentView;
    private DialogCityBinding binding;
    private Context context;
    private int dialogGravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
    private boolean onTouchOutside = true;// 点击外部是否取消,true可以,false不可以
    private boolean cancerAble = true;//是否响应返回键,true响应,false不响应
    private int styleAnim = R.style.AnimBottom;
    private Gson gson = new Gson();
    private int year,month,day;
    private Calendar ca;

    public TimeDialog setOnTouchOutside(boolean onTouchOutside) {
        this.onTouchOutside = onTouchOutside;
        return this;
    }

    public TimeDialog setCancerAble(boolean cancerAble) {
        this.cancerAble = cancerAble;
        return this;
    }

    public TimeDialog(@NonNull Context context) {
        super(context, R.style.MyPopupDialog);
        this.context = context;
        contentView = LayoutInflater.from(context).inflate(R.layout.dialog_city, null, false);
        binding = DataBindingUtil.bind(contentView);
        initView();


    }


    private void initView() {
        binding.tvToast.setText("选择时间");
        ca = Calendar.getInstance();
        year = ca.get(Calendar.YEAR);
        month = ca.get(Calendar.MONTH)+1;
        day = ca.get(Calendar.DATE);

        binding.wleel1.setItems(getNumberList(1900,2199));
        binding.wleel2.setItems(getNumberList(1,12));
        binding.wleel3.setItems(getNumberList(1,31));

        binding.wleel1.setInitPosition(ca.get(Calendar.YEAR)-1900);
        binding.wleel2.setInitPosition(ca.get(Calendar.MONTH));
        binding.wleel3.setInitPosition(ca.get(Calendar.DATE));

        binding.wleel1.setListener((index,s) -> {
            year = index+1900;
            setMyDay(year,month);
        });
        binding.wleel2.setListener((index,s) -> {
            month = index+1;
            setMyDay(year,month);

        });
        binding.wleel3.setListener((index,s) -> {
            day = index+1;
        });

        binding.tvToast.setOnClickListener(v -> {
            if (onDialogSelector != null) {
                onDialogSelector.onSec(year,month,day);
            }
            dismiss();
        });
    }

    OnDialogSelector onDialogSelector;
    public interface OnDialogSelector{
        void onSec(int y,int m ,int d);
    }

    public TimeDialog setOnDialogSelector(OnDialogSelector onDialogSelector) {
        this.onDialogSelector = onDialogSelector;
        return this;
    }

    private void setMyDay(int year, int month) {
        switch (month) {
            default:
                binding.wleel3.setItems(getNumberList(1,31));
                break;
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                binding.wleel3.setItems(getNumberList(1,31));
                break;
            case 4:
            case 6:
            case 9:
            case 11:
                binding.wleel3.setItems(getNumberList(1,30));
                break;
            case 2:
                binding.wleel3.setItems(getNumberList(1,TimeUtil.isRunYear(year)?29:28));
                break;
        }
    }





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(contentView);
        setCanceledOnTouchOutside(onTouchOutside);// 点击外部是否取消
        setCancelable(cancerAble);
        Window window = getWindow();
        window.setWindowAnimations(styleAnim);
        window.getDecorView().setPadding(0, 0, 0, 0);
        // 获取Window的LayoutParams
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.width = ViewGroup.LayoutParams.MATCH_PARENT;
        attributes.gravity = dialogGravity;
        // 一定要重新设置, 才能生效
        window.setAttributes(attributes);

    }

    float startY;
    float moveY = 0;
    private boolean isGetFinger = false;

    public TimeDialog setGetFinger(boolean getFinger) {
        isGetFinger = getFinger;
        return this;
    }
    /**
     *  这里设置了响应手势
     * @param ev
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (! isGetFinger){
                    return true;
                }
                moveY = Math.abs(ev.getY() - startY);
                if (styleAnim ==  R.style.AnimBottom){
                    contentView.scrollBy(0, -(int) moveY);
                    startY = ev.getY();
                    if (contentView.getScrollY() > 0 && isGetFinger) {
                        contentView.scrollTo(0, 0);
                    }
                }else {
                    contentView.scrollBy(0, (int) moveY);
                    startY = ev.getY();
                    if (contentView.getScrollY() < 0 && isGetFinger) {
                        contentView.scrollTo(0, 0);
                    }
                }

                break;
            case MotionEvent.ACTION_UP:
                if (! isGetFinger){
                    return super.onTouchEvent(ev);
                }

                if (Math.abs(contentView.getScrollY()) - this.getWindow().getAttributes().height / 4 >= 0 && moveY > 0 && isGetFinger) {
                    this.dismiss();
                }
                contentView.scrollTo(0, 0);
                break;
        }
        return super.onTouchEvent(ev);
    }

    public List<String> getNumberList(int index0, int index1) {
        List<String> list = new ArrayList<>();
        for (int i = index0; i <= index1; i++) {
            if (i<10){
                list.add("0"+i );
            }else {
                list.add(i + "");
            }

        }
        return list;
    }

}
