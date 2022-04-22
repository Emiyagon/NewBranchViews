package com.illyasr.bilibili.biliplayer.zxing.camera;

import com.google.zxing.ResultPoint;
import com.google.zxing.ResultPointCallback;
import com.illyasr.bilibili.biliplayer.zxing.config.ViewfinderView;

/**
 * TODO
 *
 * @author qingshilin
 * @version 1.0
 * @date 2022/4/7 15:51
 */

public final class ViewfinderResultPointCallback implements ResultPointCallback {

    private final ViewfinderView viewfinderView;

    public ViewfinderResultPointCallback(ViewfinderView viewfinderView) {
        this.viewfinderView = viewfinderView;
    }

    @Override
    public void foundPossibleResultPoint(ResultPoint point) {
        viewfinderView.addPossibleResultPoint(point);
    }

}
