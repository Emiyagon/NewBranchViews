package com.illyasr.mydempviews.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.illyasr.mydempviews.MainPresent;
import com.illyasr.mydempviews.R;
import com.illyasr.mydempviews.base.BaseActivity;
import com.illyasr.mydempviews.databinding.ActivityHealthyBinding;
import com.illyasr.mydempviews.view.dialog.InputDialog;
import com.illyasr.mydempviews.view.ioswheel.OnItemSelectedListener;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.lxj.xpopup.interfaces.OnInputConfirmListener;
import com.lxj.xpopup.interfaces.SimpleCallback;

public class HealthyActivity extends BaseActivity<ActivityHealthyBinding, MainPresent> {

    private double height = 1.5;//2
    private double weight = 40;//3


    @Override
    protected void initData() {

        mBindingView.wleel2.setItems(getNumberList(150, 220));
        mBindingView.wleel2.setInitPosition(0);
        mBindingView.wleel3.setItems(getNumberList(40, 220));
        mBindingView.wleel3.setInitPosition(0);

        mBindingView.wleel2.setListener((index, s) -> {
            height = Double.parseDouble(s)/100;
            mBindingView.tvHeight.setText(s);

            onBelow();
        });
        mBindingView.wleel3.setListener((index, s) -> {
            weight = Double.parseDouble(s);
            mBindingView.tvWeight.setText(s);
            onBelow();
        });

        mBindingView.tvWeight.setOnLongClickListener(v -> {
            new InputDialog(HealthyActivity.this,2)

                    .setOnDialogListener(new InputDialog.OnDialogListener() {
                        @Override
                        public void onEnter(String s) {
                            weight = Double.parseDouble(s);
                            mBindingView.tvWeight.setText(s);
                            onBelow();
                        }

                        @Override
                        public void onDismiss() {

                        }
                    })
                    .show();
            return true;
        });
        mBindingView.tvHeight.setOnLongClickListener(v -> {
            new InputDialog(HealthyActivity.this,2)
                    .setOnDialogListener(new InputDialog.OnDialogListener() {
                        @Override
                        public void onEnter(String s) {
                            height = Double.parseDouble(s)/100;
                            mBindingView.tvHeight.setText(s);

                            onBelow();
                        }

                        @Override
                        public void onDismiss() {

                        }
                    })
                    .show();
            return true;
        });

    }

    private void onBelow() {
        double bmi = weight/Math.pow(height,2);
        mBindingView.stv1.setText( String.format("您的 BMI 指数是 :%f,%n 健康状况 : %s",bmi,bmi>=28?"肥胖":(bmi>=24?"超重":bmi>=18.5?"正常":"偏瘦")));
//        mBindingView.clock.setCompleteDegree((float) bmi*2);
//        mBindingView.clock.setCavalTitle(bmi>=28?"肥胖":bmi>=24?"超重":bmi>=18.5?"正常":"偏瘦");
        mBindingView.clock.setIts((float) bmi*2,bmi>=28?"肥胖":bmi>=24?"超重":bmi>=18.5?"正常":"偏瘦");
    }


    @Override
    protected int setLayoutId() {
        return R.layout.activity_healthy;
    }
}