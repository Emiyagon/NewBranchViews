package com.illyasr.mydempviews.view;


import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.illyasr.mydempviews.R;

/**
 *  Created by bullet
 */
public class MProgressDialog extends Dialog {
    private Context context=null;
    private  MProgressDialog progressDialog = null;
    private  MProgressDialog progressDialogc = null;
    public MProgressDialog(Context context) {
        super(context);
        this.context=context;
    }

    public static final String P1 = "loading.json";
    public static final String P2= "custom_load.json";
    public static final String P3 = "rotate.json";
    public static final String P4 = "laugh.json";
    public static final String P5 = "hourglass.json";
    public static final String P6 = "spinner.json";
    public static final String P7 = "spinner_upload.json";


    public MProgressDialog(Context context, int theme) {
        super(context, theme);
    }

    /*
     * 固定样式的等待dialog
     * */
    public  MProgressDialog createLoadingDialog( String msg) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.progressdialog, null);
        LinearLayout layout = (LinearLayout) v.findViewById(R.id.dialog_view);
//        ImageView spaceshipImage =  v.findViewById(R.id.img);
        TextView tipTextView = (TextView) v.findViewById(R.id.tipTextView);
        // 加载动画
        Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(
                context, R.anim.progressdialog_anim);
        // 使用ImageView显示动画
//        spaceshipImage.startAnimation(hyperspaceJumpAnimation);
        tipTextView.setText(msg);

        LottieAnimationView lav = v.findViewById(R.id.animation_view);
       /* lav.setAnimation("spinner.json");
        lav.loop(true);*/
        progressDialog = new MProgressDialog(context, R.style.myprogressdialog);// 创建自定义样式dialog

        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(true);// 不可以用“返回键”取消
        progressDialog.setContentView(layout, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));
        return progressDialog;

    }




}

