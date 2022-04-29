package com.illyasr.mydempviews.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.illyasr.mydempviews.R;
import com.illyasr.mydempviews.databinding.DialogSendNumberBinding;

/**
 * TODO
 *
 * @author qingshilin
 * @version 1.0
 * @date 2022/4/25 10:06
 */
public class MyPasswordDialog extends Dialog {

    private Context context;
    private final View contentView;
    private int styleAnim = R.style.AnimBottom;
    private int dialogGravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
    private boolean onTouchOutside = true;// 点击外部是否取消,true可以,false不可以
    private boolean cancerAble = true;//是否响应返回键,true响应,false不响应
    private final DialogSendNumberBinding binding;

    private OnDialogInterface onDialogInterface;
    public interface OnDialogInterface {
        void onDismiss();
        void onEnter(String s);
    }

    public MyPasswordDialog setOnDialogInterface(OnDialogInterface onDialogInterface) {
        this.onDialogInterface = onDialogInterface;
        return this;
    }

    public MyPasswordDialog setOnTouchOutside(boolean onTouchOutside) {
        this.onTouchOutside = onTouchOutside;
        return this;
    }

    public MyPasswordDialog setCancerAble(boolean cancerAble) {
        this.cancerAble = cancerAble;
        return this;
    }

    public MyPasswordDialog(@NonNull Context context) {
        super(context, R.style.MyPopupDialog);
        this.context = context;
        contentView = LayoutInflater.from(context).inflate(R.layout.dialog_send_number, null, false);
        binding = DataBindingUtil.bind(contentView);
        initView();
    }

    private void initView() {
        binding.rvKeyboard.setAdapter(new KeyBoardAdapter(context)
                .setOnKeyBoardInterface(new KeyBoardAdapter.OnKeyBoardInterface() {
            @Override
            public void OnNumber(String number) {

            }
            @Override
            public void OnEnd(String result) {
                binding.tvNum.setText(TextUtils.isEmpty(result)?"0":result);
            }
        }));

        binding.dismiss.setOnClickListener(v -> {
            if (onDialogInterface != null) {
                onDialogInterface.onDismiss();
                dismiss();
            }
        });
        binding.enter.setOnClickListener(v -> {
            if (onDialogInterface != null) {
                onDialogInterface.onEnter(sendStr(binding.tvNum.getText().toString()));
            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(contentView);
        setCanceledOnTouchOutside(onTouchOutside);// 点击外部是否取消
        setCancelable(cancerAble);
        Window window = getWindow();
//        WindowManager.LayoutParams params = win.getAttributes();
//        win.setSoftInputMode(params.softInputMode);
        window.setWindowAnimations(styleAnim);
        window.getDecorView().setPadding(0, 0, 0, 0);
        // 获取Window的LayoutParams
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.width = ViewGroup.LayoutParams.MATCH_PARENT;
        attributes.gravity = dialogGravity;
        // 一定要重新设置, 才能生效
        window.setAttributes(attributes);

    }

    private String sendStr(String s) {
        //lastIndexOf获取最后一个.的索引，判断是否是字符串的最后一位
        if(s.lastIndexOf(".") == s.length()-1) {
//            System.out.println();
          s=  s.substring(0, s.length() - 1);
        }
        return s;
    }
}
