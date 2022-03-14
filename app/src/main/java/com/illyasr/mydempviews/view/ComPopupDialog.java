package com.illyasr.mydempviews.view;

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

import androidx.annotation.Nullable;

import com.illyasr.mydempviews.R;


public abstract class ComPopupDialog extends Dialog {
    private View contentView;
    private int styleAnim = R.style.AnimBottom;
    private int dialogGravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;

    private boolean isGetFinger = false;


    private CompopupInterface compopupInterface;

    public ComPopupDialog setCompopupInterface(CompopupInterface compopupInterface) {
        this.compopupInterface = compopupInterface;
        return this;
    }

    public interface CompopupInterface{
        void onDismiss();
    };

    public ComPopupDialog setGetFinger(boolean getFinger) {
        isGetFinger = getFinger;
        return this;
    }

    public ComPopupDialog(Context context, int layoutId) {
        super(context, R.style.MyPopupDialog);
        contentView = LayoutInflater.from(context).inflate(layoutId, null, false);
        initView();
        initEvent();
    }

    /**
     *
     * @param context
     * @param layoutId
     * @param styleAnim
     * @param dialogGravity  按照你想要的方向,参考原始数据就可以了
     */
    public ComPopupDialog(Context context, int layoutId ,int styleAnim,int dialogGravity) {
        super(context, R.style.MyPopupDialog);
        this.styleAnim = styleAnim;
        this.dialogGravity = dialogGravity;
        contentView = LayoutInflater.from(context).inflate(layoutId, null, false);
        initView();
        initEvent();
    }


    public ComPopupDialog(Context context, int layoutId,int style) {
        super(context,style);
        contentView = LayoutInflater.from(context).inflate(layoutId, null, false);
        initView();
        initEvent();
    }
    public View getContentView() {
        return contentView;
    }


    private boolean onTouchOutside = true;// 点击外部是否取消,true可以,false不可以
    private boolean cancerAble = true;//是否响应返回键,true响应,false不响应

    public ComPopupDialog setOnTouchOutside(boolean onTouchOutside) {
        this.onTouchOutside = onTouchOutside;
        return this;
    }

    public ComPopupDialog setCancerAble(boolean cancerAble) {
        this.cancerAble = cancerAble;
        return this;
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

    public abstract void initView();

    public abstract void initEvent();

    float startY;
    float moveY = 0;

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

    @Override
    public void setOnDismissListener(@Nullable OnDismissListener listener) {
        super.setOnDismissListener(listener);
        Log.e("TAG", "我在妈妈这里消失啦!");
        if (compopupInterface != null) {
            compopupInterface.onDismiss();
        }
    }
}
