package com.zhuosen.bilibili.biliplayer.zxing.camera;

import android.app.Activity;
import android.content.DialogInterface;

/**
 * TODO
 *在相机会手电筒可能被占用的情况下退出
 * @author qingshilin
 * @version 1.0
 * @date 2022/4/7 15:29
 */

public final class FinishListener implements DialogInterface.OnClickListener, DialogInterface.OnCancelListener {

    private final Activity activityToFinish;

    public FinishListener(Activity activityToFinish) {
        this.activityToFinish = activityToFinish;
    }

    @Override
    public void onCancel(DialogInterface dialogInterface) {
        run();
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
        run();
    }

    private void run() {
        activityToFinish.finish();
    }

}