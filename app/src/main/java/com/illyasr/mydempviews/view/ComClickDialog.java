package com.illyasr.mydempviews.view;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.illyasr.mydempviews.R;

/**
 * Created by Spring on 2017/6/26.
 * dialog.setCancelable( );     Cancelable可撤销 是否可消失
 * dialog.setCanceledOnTouchOutside( );  点击其他是否消失
 */

public abstract class ComClickDialog extends Dialog {

    private View contentView;
    private boolean candismissout = true;

    public ComClickDialog(Context context, int layoutId, int themeResId) {
        super(context, themeResId);
        contentView = LayoutInflater.from(context).inflate(layoutId, null, false);
        initView();
        initEvent();
    }

     public ComClickDialog(Context context, int layoutId) {
//        super(context, R.style.Dialog_Transparent);
        super(context, R.style.MyDialog);
        contentView = LayoutInflater.from(context).inflate(layoutId, null, false);
        initView();
        initEvent();
    }
    public ComClickDialog(Context context, int layoutId, boolean cancel) {
//        super(context, R.style.Dialog_Transparent);
        super(context, R.style.MyDialog);
        contentView = LayoutInflater.from(context).inflate(layoutId, null, false);
        candismissout = cancel;
        initView();
        initEvent();
    }
    /**
     *  仿popupwindow样式dialog
     * @param context
     * @param layoutId
     * @param other
     */
    public ComClickDialog(Context context, int layoutId, String other) {
        super(context, R.style.Dialog_Transparent);
        contentView = LayoutInflater.from(context).inflate(layoutId, null, false);
        initView();
        initEvent();
        Window window = getWindow();
        window.setWindowAnimations(R.style.AnimBottom);

    }


    public View getContentView() {
        return contentView;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(contentView);
        setCanceledOnTouchOutside(true);
        setCancelable(candismissout);
        Window win = getWindow();
        WindowManager.LayoutParams params = win.getAttributes();
        win.setSoftInputMode(params.softInputMode);
//        requestWindowFeature(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
    }



    public abstract void initView();

    public abstract void initEvent();
}

