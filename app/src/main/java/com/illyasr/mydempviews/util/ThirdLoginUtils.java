package com.illyasr.mydempviews.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.alipay.sdk.app.AuthTask;
import com.illyasr.mydempviews.alipay.AuthResult;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;

import java.util.Map;

/**
 *  第三方登陆工具类
 *  这些都是示例,后面接入的时候再挨个放出来
 *  目前有qq,微信,微博,支付宝登陆
 * @author qingshilin
 * @version 1.0
 * @date 2022/6/21 14:30
 */
public class ThirdLoginUtils {

    public void QQLogin(Activity context){
  /*      Tencent  mTencent = Tencent.createInstance("APPID", context);
        if (mTencent == null) {
            Log.e("TAG", "腾讯实例创建失败");
            context.finish();
        }

//  这个 loginListener  要从外面传过来的,不是内部实现  目前没东西所以只能这样
        mTencent.login(this, "all", loginListener);*/
    }


    /**
     *  微信登录
     * @param context 全局变量,理论上最好传application
     * @param appid 从微信那里申请的appid
     */
    public void VxLogin(Context context, String appid) {
          IWXAPI api;
        //通过WXAPIFactory工厂获取IWXApI的示例
        api = WXAPIFactory.createWXAPI(context,appid , true);
        //将应用的appid注册到微信
        api.registerApp(appid);
        SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";//
//                req.scope = "snsapi_login";//提示 scope参数错误，或者没有scope权限
        req.state = "wechat_sdk_微信登录";
        api.sendReq(req);
        // 这里只是调用,实际操作还是在官方那边的名为wxapi的文件夹下的文件里面
        // 具体操作之前的文件都是可以直接拿来用的,异步通知对应地方的文件方可
    }

    private static final int SDK_AUTH_FLAG = 2;
    public void ZfbLogin(Activity activity,final String authInfo) {
        /**
         * 这里只是为了方便直接向商户展示支付宝的整个支付流程；所以Demo中加签过程直接放在客户端完成；
         * 真实App里，privateKey等数据严禁放在客户端，加签过程务必要放在服务端完成；
         * 防止商户私密数据泄露，造成不必要的资金损失，及面临各种安全风险；
         * authInfo的获取必须来自服务端；
         */
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
//                        EventBus.getDefault().post(new EventBusAlipay(authResult.getAuthCode()));//这个换成其他通知
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
     * 当前 DEMO 应用的回调页，第三方应用可以使用自己的回调页。
     * 建议使用默认回调页：https://api.weibo.com/oauth2/default.html
     */
    public static final String REDIRECT_URL = "http://www.sina.com";
    /**
     * WeiboSDKDemo 应用对应的权限，第三方开发者一般不需要这么多，可直接设置成空即可。
     * 详情请查看 Demo 中对应的注释。
     */
    public static final String SCOPE =
            "email,direct_messages_read,direct_messages_write,"
                    + "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
                    + "follow_app_official_microblog," + "invitation_write";

    /**
     * 微博登录
     */
    private void WeiBLogin(String WeiB_appkey) {
       /* AuthInfo authInfo = new AuthInfo(this, WeiB_appkey, REDIRECT_URL, SCOPE);
        mWBAPI = WBAPIFactory.createWBAPI(this);
        mWBAPI.registerApp(this, authInfo);
        mWBAPI.authorizeClient(new WbAuthListener() {
            @Override
            public void onComplete(Oauth2AccessToken token) {

                //调用微博的用户请求地址，获取用户信息，新版的微博，用户信息将不再直接返回，必须调用以下的接口才能返回用户的详细信息 https://api.weibo.com/2/users/show.json
                Map<String, Object> map = new HashMap<>();
                map.put("access_token", token.getAccessToken());
                map.put("uid", token.getUid());
                Set<Map.Entry<String, Object>> entries = map.entrySet();
                StringBuffer absUrl = new StringBuffer();
                int size = 0;
                for (Map.Entry<String, Object> item : entries) {
                    if (size == 0) {
                        absUrl.append("https://api.weibo.com/2/users/show.json").append("?").append(item.getKey()).append("=").append(item.getValue());
                        size++;
                    } else {
                        absUrl.append("&").append(item.getKey()).append("=").append(item.getValue());
                    }
                }

                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(absUrl.toString())
                        .build();
                client.newCall(request).enqueue(new okhttp3.Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.isSuccessful()) {
                            String result = response.body().string();
                            if (result == null) {
                                return;
                            }
                            WeiBEntity weib = new Gson().fromJson(result, WeiBEntity.class);
                            if (weib == null) {
                                return;
                            }
                            userInfoT = new UserInfoT();
                            if (weib.getScreen_name() != null && !weib.getScreen_name().isEmpty()) {
                                userInfoT.setNickname(userInfoT.getNickname());
                            }
                            if (weib.getAvatar_hd() != null && !weib.getAvatar_hd().isEmpty()) {
                                userInfoT.setHeadurl(weib.getAvatar_hd());
                            }
                            if (weib.getGender() != null && !weib.getGender().isEmpty()) {
                                //性别，m：男、f：女、n：未知
                                if (weib.getGender().equals("m")) {
                                    userInfoT.setGender(1);
                                } else if (weib.getGender().equals("f")) {
                                    userInfoT.setGender(2);
                                } else if (weib.getGender().equals("n")) {
                                    userInfoT.setGender(1);
                                }
                            }
                            if (weib.getDescription() != null && !weib.getDescription().isEmpty()) {
                                userInfoT.setDistrict(weib.getDescription());
                            }


                            // todo  上传数据  这里是将第三方的平台的用户信息传递给后台，



                        }

                        @Override
                        protected void onError(Call call, int statusCode, String errMessage) {
                            errMessage.toString();

                        }
                    });


                }


            }
            @Override
            public void onError(UiError error) {
                Toast.makeText(LoginActivity.this, "微博授权出错", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel() {
                Toast.makeText(LoginActivity.this, "微博授权取消", Toast.LENGTH_SHORT).show();
            }
        }); */
        }


}
