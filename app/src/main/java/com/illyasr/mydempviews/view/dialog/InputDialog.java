package com.illyasr.mydempviews.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.illyasr.mydempviews.R;
import com.illyasr.mydempviews.databinding.DialogInputBinding;

public class InputDialog extends Dialog {

    private DialogInputBinding binding;
    private final View contentView;


    private OnDialogListener onDialogListener;
    public interface OnDialogListener {
        void onEnter(String s);
        void onDismiss();
    }

    public InputDialog setOnDialogListener(OnDialogListener onDialogListener) {
        this.onDialogListener = onDialogListener;
        return this;
    }

    private int inputType = InputType. TYPE_CLASS_TEXT;// 默认全部可以


    public InputDialog(@NonNull Context context,int type) {
        super(context, R.style.MyDialog);
        switch (type) {
            default:
                inputType =InputType. TYPE_CLASS_TEXT;
                break;
            case 0:
                inputType =InputType. TYPE_CLASS_TEXT;
                break;
            case 1:// 只有字母
                inputType =InputType. TYPE_CLASS_TEXT;
                break;
            case 2://只有数字
                inputType =InputType. TYPE_CLASS_NUMBER;
                break;
            case 3://明文密码
                inputType =InputType. TYPE_TEXT_VARIATION_PASSWORD;
                break;
            case 4://加密密码
                inputType =InputType. TYPE_TEXT_VARIATION_VISIBLE_PASSWORD;
                break;
        }
        contentView = LayoutInflater.from(context).inflate(R.layout.dialog_input, null, false);
        binding = DataBindingUtil.bind(contentView);
        initView();

    }

    private void initView() {
        binding.etInput.setInputType(inputType);

        binding.enter.setOnClickListener(v -> {
            if (TextUtils.isEmpty(binding.etInput.getText().toString())){
                Toast.makeText(getContext(),"请先输入之后再点击确定",Toast.LENGTH_SHORT).show();
                return;
            }
            if (onDialogListener != null) {
                onDialogListener.onEnter(binding.etInput.getText().toString());
            }
            dismiss();
        });
        binding.dismiss.setOnClickListener(v -> {
            if (onDialogListener != null) {
                onDialogListener.onDismiss();
            }
            dismiss();
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(contentView);
        setCanceledOnTouchOutside(true);
        setCancelable(true);
        Window win = getWindow();
        WindowManager.LayoutParams params = win.getAttributes();
        win.setSoftInputMode(params.softInputMode);
//        requestWindowFeature(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
    }

}
