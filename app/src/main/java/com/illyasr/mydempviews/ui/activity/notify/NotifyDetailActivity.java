package com.illyasr.mydempviews.ui.activity.notify;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.illyasr.mydempviews.MainPresent;
import com.illyasr.mydempviews.R;
import com.illyasr.mydempviews.base.BaseActivity;
import com.illyasr.mydempviews.databinding.ActivityNotifyDetailBinding;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnInputConfirmListener;

public class NotifyDetailActivity extends BaseActivity<ActivityNotifyDetailBinding, MainPresent> {



    @Override
    protected void initData() {

    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_notify_detail;
    }



    private void showInput(TextView view) {
        new XPopup.Builder(this)
                .hasStatusBarShadow(false)
                //.dismissOnBackPressed(false)
//                .isDestroyOnDismiss(true) //对于只使用一次的弹窗，推荐设置这个
                .autoOpenSoftInput(true)
                .isDarkTheme(false)
//                .isViewMode(true)
//                        .setPopupCallback(new DemoXPopupListener())
//                        .autoFocusEditText(false) //是否让弹窗内的EditText自动获取焦点，默认是true
                //.moveUpToKeyboard(false)   //是否移动到软键盘上面，默认为true
                .asInputConfirm("提示", null, null, "请输入内容",
                        text -> {
                            view.setText(text);
                        })
                .show();
    }

}