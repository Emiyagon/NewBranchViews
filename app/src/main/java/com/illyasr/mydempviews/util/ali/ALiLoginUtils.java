package com.illyasr.mydempviews.util.ali;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;


import com.alipay.sdk.app.AuthTask;
import com.illyasr.mydempviews.alipay.AuthResult;
import com.illyasr.mydempviews.util.AppUtils;
import com.jeremyliao.liveeventbus.LiveEventBus;

import org.greenrobot.eventbus.EventBus;

import java.util.Map;

import androidx.appcompat.app.AlertDialog;

public class ALiLoginUtils {


    public Activity activity;

    /** 支付宝支付业务：入参app_id */
    public static final String APPID = "2019011262887338";
    /** 支付宝账户登录授权业务：入参pid值 */
    public static final String PID = "2088331946579388";
    /** 支付宝账户登录授权业务：入参target_id值 可自定义，保证唯一性即可*/
//    public static final String TARGET_ID = "保证唯一性即可";
    public static final String TARGET_ID = AppUtils.getUUID();

    /** 商户私钥，pkcs8格式 */
    /** 如下私钥，RSA2_PRIVATE 或者 RSA_PRIVATE 只需要填入一个 */
    /** 如果商户两个都设置了，优先使用 RSA2_PRIVATE */
    /** RSA2_PRIVATE 可以保证商户交易在更加安全的环境下进行，建议使用 RSA2_PRIVATE */
    /** 获取 RSA2_PRIVATE，建议使用支付宝提供的公私钥生成工具生成， */
    /** 工具地址：https://doc.open.alipay.com/docs/doc.htm?treeId=291&articleId=106097&docType=1 */
    public static final String RSA2_PRIVATE = "MIIEpAIBAAKCAQEAuIPopHsyFXZKJIKA9cDZaxXw51KUFEhKMztu32uhyYqTRV+ilbKCeT7t7IYCFbGyzgsDf7nxz1uX6VxeCn7PcAJaHgQpeenkpiX9xWafkfO4G2WrogKDSeZJWHaZCC7ds9EaixFSa8YmbZH6umEG5LMF0HgskxqS3IDTc86Scs2VkYFpjLqC2TGnsXJEoS6+3ZxD2lb5oMbCwj8Iec1cn5q4KGyZJTp2gX/rZfmNzdX/0fjAQecoS0PZ4avpIsSshmIYIkKs3yhO4aaK7VSUAv9xsFoTzqrAUSbMnKf1OJOEmqsTNQKEelMWJcCLn+QrvXxLo3cqoNGHvy+I0xDNBwIDAQABAoIBADx6eAGZo8PnHswyvmROzrTL30djCMhRHYkHXQL8O25c7oKZoyff9+YX21JgZkSIFyxz7bSq1Ph7dCJ4NRbJk+KJ0tHt2U5tfJV89F4Ekh77vIe2XnhezHuncLj/Po+xFNgAnOuiRNqwex4E8CKw9Np3V1uKWEnrNuHxk91sgxH/g75Mh6KnQBuM4VFwnI2VKhzWA3ddEPz9/wqxlmd6sHiVZilJuH4S7Iyqz1nTLNsFKXb0d66vxTU8YYlIoPagoe6QvoiDGtzwLZg90+uVYv0TsPICwYlbAVMWuvO9ag03ptKSCBuz1DdXAuQkEd8IXIARPhc039vOqJFhXj4v/rkCgYEA6gPSgQYMl6Xd/n0ho1UNmXUVmKcLZLB6AKpj0O2jTID6iI8yUYze/qUmNvQa+B67luKnfsn5x8Jr82CA+EjgM3F3dNGQihBJ16G0xABn+3gmGRgjGzARy0150PdyLM0ZV7st/LQTvwvlfXn91PsJC3xVuPhDAE0oAYL17FG22V0CgYEAydmYMb69nfEQyxsZLZBrkAvReKpnhi8hDmeZzPTe1A1YzZe6Ook3k8CpaLCGhKO+3aEy7hRvO0S3QtVf+IXg/IR9/B8WatZE8CjqLwUgAE13Op0avnXzy9B9RtbXluIVdb70n9UPI+AneJNFRQVuxAIDyKlhfKIGjEbnZZ5YBbMCgYEAiROmGefmNmp1iB3aN2eZ6LsZ5GptFRT+0ib9AyRAVI70sktXCUgAU59/rA7/QToXXkH0JSPAiY/MVirAQS/HDLs5YxdimE0ERCg3z9wBDN+LSYXHAqT8t17xrUNVAQM/BhHh1TXSn0Ujr4Vm93v5B15UhF4AOUv41DHXRg9zkQ0CgYEAu7A8J41P7av5TJKe/9I5YlDK72OsLYZnO2i0B3NuO0lJ2KoL/WAYMKhGHlV+T5me4aCBpgWURE0qBtg3i/SY9tUP/L6/bopcwd+Zmi0uy3k8s2DgPFebYVo1Ewi0oNUK1VjBSFmp4VcN/L0b/QkHLPUepZhyUJyscAWCJuhsuqcCgYBcChoKMZNWFIBNnEKZAAaabOUF4uz+Oa2xUo3j8UQtUqDEZOrIhz3iUdX4J4ec4CjYsDb1nO4l8kHmrOPg+QnMb1XFaoLFtiFaKfo4VPp+Go/9iRg+kKxzyqmYLd0aK6/Ego1zmugczLPndiCKZLhR1e9i3vB8QzBFFuL2f8LMuA==";

    public static final String RSA_PRIVATE = "";

    private static final int SDK_AUTH_FLAG = 2;

    public ALiLoginUtils(Activity activity){
        this.activity = activity;
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_AUTH_FLAG: {
                    @SuppressWarnings("unchecked")
                    AuthResult authResult = new AuthResult((Map<String, String>) msg.obj, true);
                    String resultStatus = authResult.getResultStatus();
                    String resultCode = authResult.getResultCode();
                    Log.e("ALiLoginActivity", "=====resultStatus=====" + resultStatus);
                    Log.e("ALiLoginActivity", "=====resultCode=====" + resultCode);

                    // 判断resultStatus 为“9000”且result_code
                    // 为“200”则代表授权成功，具体状态码代表含义可参考授权接口文档
                    if (TextUtils.equals(resultStatus, "9000") && TextUtils.equals(authResult.getResultCode(), "200")) {
                        // 获取alipay_open_id，调支付时作为参数extern_token 的value
                        // 传入，则支付账户为该授权账户
                        Log.e("ALiLoginActivity", "授权成功\n" + String.format("authCode:%s", authResult.getAuthCode()));
//                        Toast.makeText(activity,"授权成功\n" + String.format("authCode:%s", authResult.getAuthCode()), Toast.LENGTH_SHORT).show();
//                        EventBus.getDefault().post(new EventBusAlipay(authResult.getAuthCode()));
                        LiveEventBus.get().with("Alipay").post(authResult.getAuthCode());
                    } else {
                        // 其他状态值则为授权失败
                        Log.e("ALiLoginActivity", "授权失败\n" + String.format("authCode:%s", authResult.getAuthCode()));
//                        Toast.makeText(activity,"授权失败" + String.format("authCode:%s", authResult.getAuthCode()), Toast.LENGTH_SHORT).show();
                    }
                    break;
                }
                default:
                    break;
            }
        };
    };


    /**
     * 支付宝账户授权业务
     */
    public void authV2() {
        if (TextUtils.isEmpty(PID) || TextUtils.isEmpty(APPID)
                || (TextUtils.isEmpty(RSA2_PRIVATE) && TextUtils.isEmpty(RSA_PRIVATE))
                || TextUtils.isEmpty(TARGET_ID)) {
            new AlertDialog.Builder(activity).setTitle("警告")
                    .setMessage("需要配置PARTNER |APP_ID| RSA_PRIVATE| TARGET_ID")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialoginterface, int i) {
                        }
                    }).show();
            return;
        }

        /**
         * 这里只是为了方便直接向商户展示支付宝的整个支付流程；所以Demo中加签过程直接放在客户端完成；
         * 真实App里，privateKey等数据严禁放在客户端，加签过程务必要放在服务端完成；
         * 防止商户私密数据泄露，造成不必要的资金损失，及面临各种安全风险；
         *
         * authInfo的获取必须来自服务端；
         */
        boolean rsa2 = (RSA2_PRIVATE.length() > 0);
        Map<String, String> authInfoMap =
                OrderInfoUtil2_0.buildAuthInfoMap(PID, APPID, TARGET_ID, rsa2);
        String info = OrderInfoUtil2_0.buildOrderParam(authInfoMap);

        String privateKey = rsa2 ? RSA2_PRIVATE : RSA_PRIVATE;
        String sign = OrderInfoUtil2_0.getSign(authInfoMap, privateKey, rsa2);
        final String authInfo = info + "&" + sign;
        Log.e("ALiLoginActivity", "=====authInfo=====" + authInfo);
        Runnable authRunnable = new Runnable() {

            @Override
            public void run() {
                // 构造AuthTask 对象
                AuthTask authTask = new AuthTask(activity);
                // 调用授权接口，获取授权结果
                Map<String, String> result = authTask.authV2(authInfo, true);

                Message msg = new Message();
                msg.what = SDK_AUTH_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        // 必须异步调用
        Thread authThread = new Thread(authRunnable);
        authThread.start();
    }

    public void authV2(final String authInfo) {
        /**
         * 这里只是为了方便直接向商户展示支付宝的整个支付流程；所以Demo中加签过程直接放在客户端完成；
         * 真实App里，privateKey等数据严禁放在客户端，加签过程务必要放在服务端完成；
         * 防止商户私密数据泄露，造成不必要的资金损失，及面临各种安全风险；
         *
         * authInfo的获取必须来自服务端；
         */
//        boolean rsa2 = (RSA2_PRIVATE.length() > 0);
//        Map<String, String> authInfoMap = OrderInfoUtil2_0.buildAuthInfoMap(PID, APPID, TARGET_ID, rsa2);
//        String info = OrderInfoUtil2_0.buildOrderParam(authInfoMap);
//
//        String privateKey = rsa2 ? RSA2_PRIVATE : RSA_PRIVATE;
//        String sign = OrderInfoUtil2_0.getSign(authInfoMap, privateKey, rsa2);
////        final String authInfo = info + "&" + sign;
//        Log.e("ALiLoginActivity", "=====authInfo=====" + authInfo);
        Runnable authRunnable = new Runnable() {

            @Override
            public void run() {
                // 构造AuthTask 对象
                AuthTask authTask = new AuthTask(activity);
                // 调用授权接口，获取授权结果
                Map<String, String> result = authTask.authV2(authInfo, true);

                Message msg = new Message();
                msg.what = SDK_AUTH_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        // 必须异步调用
        Thread authThread = new Thread(authRunnable);
        authThread.start();
    }
}
