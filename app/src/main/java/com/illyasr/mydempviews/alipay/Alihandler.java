package com.illyasr.mydempviews.alipay;
/*
 *Create By KingMgg
 *
 *on 2018/11/21
 *
 *com.zhuos.refreshyouth.alipay
 */

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;


import com.illyasr.mydempviews.util.ToastUtils;
import com.jeremyliao.liveeventbus.LiveEventBus;

import org.greenrobot.eventbus.EventBus;

public class Alihandler extends Handler {
    private static final int SDK_PAY_FLAG = 1;
    @SuppressWarnings("unused")
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case SDK_PAY_FLAG: {
                PayResult payResult = new PayResult((String) msg.obj);
                /**
                 * 同步返回的结果必须放置到服务端进行验证，建议商户依赖异步通知
                 */
                String resultInfo = payResult.getResult();// 同步返回需要验证的信息

                String resultStatus = payResult.getResultStatus();
                // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                if (TextUtils.equals(resultStatus, "9000")) {
//                    ToastUtils.showToastCenter("支付成功");
                    LiveEventBus.get().with("PayResult").post(true);
                    //startActivity(new Intent(OrderDetailActivity.this,InfoBuySuccessActivity.class));
                } else {
                    // 判断resultStatus 为非"9000"则代表可能支付失败
                    // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                    if (TextUtils.equals(resultStatus, "8000")) {
                        ToastUtils.showToastCenter("支付结果确认中");
                    } else {
                        // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                        // ToastUtils.showToastCenter("支付失败");
                        LiveEventBus.get().with("PayResult").post(false);
                    }
                }
                break;
            }
        }
    }
}
