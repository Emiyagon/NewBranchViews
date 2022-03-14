package com.illyasr.mydempviews.phone;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.illyasr.mydempviews.R;
import com.illyasr.mydempviews.databinding.DialogShowBinding;
import com.illyasr.mydempviews.databinding.DialogShowTxlBinding;

import java.util.ArrayList;
import java.util.List;

public class PhoneDialog extends Dialog {
    private Context context;
    private final View contentView;
    private final DialogShowTxlBinding binding;
    private List<PhoneDto> list = new ArrayList<>();

    private OnDialogClick click;
    public interface OnDialogClick {
        void OnPut(String name, String tele);
    }

    public PhoneDialog setClick(OnDialogClick click) {
        this.click = click;
        return this;
    }

    public PhoneDialog(@NonNull Context context, List<PhoneDto> list) {
        super(context, R.style.MyPopupDialog);
        this.context = context;
        this.list = list;
        binding = DataBindingUtil.inflate(LayoutInflater.from(context),
                 R.layout.dialog_show_txl, null, false);
        contentView = binding.getRoot();
        binding.recyclerView.setAdapter(new PhoneAdapter(context,list).setListener((name, tele) -> {
            if (click != null) {
                click.OnPut(name,tele);
            }
            dismiss();
        }));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(contentView);
        setCanceledOnTouchOutside(true);// 点击外部是否取消
        setCancelable(true);
        Window window = getWindow();
//        WindowManager.LayoutParams params = win.getAttributes();
//        win.setSoftInputMode(params.softInputMode);
        window.setWindowAnimations(R.style.AnimBottom);
        window.getDecorView().setPadding(0, 0, 0, 0);
        // 获取Window的LayoutParams
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.width = ViewGroup.LayoutParams.MATCH_PARENT;
        attributes.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
        // 一定要重新设置, 才能生效
        window.setAttributes(attributes);
    }
}
