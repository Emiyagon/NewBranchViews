package com.illyasr.mydempviews.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.illyasr.mydempviews.MainPresent;
import com.illyasr.mydempviews.R;
import com.illyasr.mydempviews.adapter.SpeedAdapter;
import com.illyasr.mydempviews.base.BaseActivity;
import com.illyasr.mydempviews.databinding.ActivityMainTextBinding;
import com.illyasr.mydempviews.databinding.DialogChooseWheelBinding;
import com.illyasr.mydempviews.databinding.DialogColorsBinding;
import com.illyasr.mydempviews.util.DonwloadSaveImg;
import com.illyasr.mydempviews.view.ComClickDialog;
import com.illyasr.mydempviews.view.ComPopupDialog;
import com.illyasr.mydempviews.view.MyAlertDialog;
import com.illyasr.mydempviews.view.ioswheel.OnItemSelectedListener;
import com.larswerkman.holocolorpicker.ColorPicker;
import com.larswerkman.holocolorpicker.OpacityBar;
import com.larswerkman.holocolorpicker.SVBar;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivityText extends BaseActivity<ActivityMainTextBinding, MainPresent> {

    private int textspeed = 1;
    private int textColor = 0xffffffff;
    @Override
    protected int setLayoutId() {
        return R.layout.activity_main_text;
    }

    @Override
    protected void initData() {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(RecyclerView.HORIZONTAL);
        mBindingView.speed.setLayoutManager(manager);
        mBindingView.speed.setAdapter(new SpeedAdapter(this, new int[]{1, 2, 3, 4, 5, 10})
                .setOnSpeedClicklistener((pos, speed) -> {
                    textspeed = speed;
                }));
        mBindingView.colorText.setOnClickListener(v -> {
            new ComClickDialog(MainActivityText.this, R.layout.dialog_colors) {

                DialogColorsBinding binding;

                @Override
                public void initView() {
                    binding = DataBindingUtil.bind(getContentView());
                    binding.colorPicker.addSVBar(binding.svBar);
                    binding.colorPicker.addOpacityBar(binding.opacityBar);
                    binding.colorPicker.addSaturationBar(binding.saturBar);
                    binding.colorPicker.addValueBar(binding.valueBar);
                }

                @Override
                public void initEvent() {
                    binding.colorPicker.setOnColorChangedListener(color -> {
                        Log.e("TAG", "color = " + color);
                    });

                    binding.dismiss.setOnClickListener(v12 -> dismiss());
                    binding.enter.setOnClickListener(v1 -> {
                        dismiss();
                        Log.e("TAG", "color = " + binding.colorPicker.getColor());
                        mBindingView.colorText.setTextColor(binding.colorPicker.getColor());
                        textColor = binding.colorPicker.getColor();
                    });
                }
            }.show();
        });


        mBindingView.mButton.setOnClickListener(v -> {
            if (TextUtils.isEmpty(mBindingView.text.getText().toString())){
                showToast("请输入弹幕!");
                return;
            }

            if (textspeed>=10){
                new MyAlertDialog(this).builder()
                        .setTitle("提示")
                        .setMsg("滚动速度过快,需要修改一下速度吗?")
                        .setPositiveButton("修改", v13 -> {
                            ItemClick(v13);
                        })
                        .setNegativeButton("不修改,头铁!", v14 -> {
                            gotoIt();
                }).show();
                return;
            }
            gotoIt();

        });
    }

    private void gotoIt() {
        Intent it=new Intent();
        it.setClass(MainActivityText.this,BarrageActivity.class);//
        it.putExtra("msg", mBindingView.text.getText().toString().trim());//
        it.putExtra("speed", textspeed);
        it.putExtra("color", textColor);
        startActivity(it);
    }

    public void ItemClick(View view) {
        new ComPopupDialog(MainActivityText.this, R.layout.dialog_choose_wheel) {
            DialogChooseWheelBinding binding;
            @Override
            public void initView() {
                binding = DataBindingUtil.bind(getContentView());
                binding.wleel2.setItems(getNumberList(1,20));
                binding.wleel2.setInitPosition(textspeed-1);
            }

            @Override
            public void initEvent() {
                binding.wleel2.setListener((index,s) -> {
                    textspeed = index + 1;
                    mBindingView.tvSpeed.setText(textspeed+"");
                });
                binding.tvToast.setOnClickListener(v -> dismiss());
            }
        }
                .setCancerAble(true)
                .setGetFinger(true)
                .setOnTouchOutside(true)
                .show();
    }



}
