package com.illyasr.mydempviews.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.illyasr.mydempviews.MyApplication;


public class WebUtil {

    /*
| 标题 | 约定方法名 | 参数 | 调用方式 |
| :----------: | :------: | :------: | :------: |
| 退出登陆 | logOut | {} | js => native |
| 隐藏底部tabbar | changeTabBar | {type: 0 / type: 1} `0隐藏 1显示` | js => native |
| 显示菜单栏编辑按钮 | showEditBtn | {type: 0 / type: 1}`1显示 0隐藏` | native => js |
| 购物车编辑 | editCart | {type: 0 / type: 1}`1编辑 0完成` | native => js |
| 调用微信分享(分享好友) | shareToFriend | {} | js => native |
| 调用微信分享(分享朋友圈) | shareToCircle | {} | js => native |
| 调用微信分享(分享好友)成功 | shareToFriendSuccess | {} | native => js |
| 调用微信分享(分享朋友圈)成功 | shareToCircleSuccess | {} | native => js |
| 关联登陆 | appLogin | type: `wx` // 微信 <br> type:`alipay` // 支付宝| js => native |
| 关联登陆结果 | appLoginResult | {type: 'SUCCESS' <br> type: 'FAIL'} <br> `SUCCESS绑定成功、FAIL失败或取消`| native => js |
| 调用支付 | payOrder |  { <br> type: alipay `支付宝` <br> type: wxpay `微信` <br> type: wallet `余额` <br>  data: {`支付参数，余额支付传空对象`} <br> } | js => native |
| 调用支付结果 | payOrderResult |  {type: 'SUCCESS' <br> type: 'FAIL'} <br> `SUCCESS支付成功、FAIL失败或取消`}| native => js |
| 绑定支付宝/微信 |  bindWx  0-绑定 1-解绑 bindZfb  0-绑定 1-解绑
    */


    /**
     *  跳转自带浏览器的方法
     * @param url
     */


    public static final String testpath = "?debug";

    public static final String apps = "http://zxserver.f3322.net/study/apphome/toAppHomePage";

//    public static final String Base_html = "https://www.chaoqick.com/web/";
//    public static final String Base_html = "https://test.chaoqick.com/web/";
public static final String Base_html = MyApplication.isTestEnvironment?"https://retest.chaoqick.com/web/":"https://www.chaoqick.com/web/";
//public static final String Base_html ="http://192.168.1.122:8080";

    public static final String My_Setting =  Base_html+"#/set";//个人中心设置页

    public static final String MALL_URL = Base_html+"#/";

    public static final String ALL_List_0 = Base_html+"#/orderList?tabIndex=0";//个人中心订单列表 0-全部 1-待付款 2-待发货 3-待收货 4-待评价
    public static final String ALL_List_1 = Base_html+"#/orderList?tabIndex=1";//个人中心订单列表 0-全部 1-待付款 2-待发货 3-待收货 4-待评价
    public static final String ALL_List_2 = Base_html+"#/orderList?tabIndex=2";//个人中心订单列表 0-全部 1-待付款 2-待发货 3-待收货 4-待评价
    public static final String ALL_List_3 = Base_html+"#/orderList?tabIndex=3";//个人中心订单列表 0-全部 1-待付款 2-待发货 3-待收货 4-待评价
    public static final String ALL_List_4 = Base_html+"#/orderList?tabIndex=4";//个人中心订单列表 0-全部 1-待付款 2-待发货 3-待收货 4-待评价



    public static final String My_Help = Base_html+"#/help";//帮助

    public static final String Apply_Joining = Base_html+"#/applyJoin";//申请加盟 ``

    public static final String My_Activities = Base_html+"#/activities";//我的活动

    public static final String My_Refund = Base_html+"#/refundList";//退款

    public static final String BASE_Login = Base_html+"#/register";//登录页

    public static final String setSafePayPass = MALL_URL+"setSafepay";

    public static final String GAME_URL = "https://b.u.mgd5.com/c/upuj/x-2n/index.html";

    /**
     * 订单详情
     */
    public static final String OrderDetails = MALL_URL+"orderDetail?order_id=";//  https://www.chaoqick.com/web/#/orderDetail?order_id=291


    /**
     * 隐私协议
     */
    public static final String Privacy_agreement = "https://oia.chaoqick.com/privacy";


    /**
     *
     */
    public static final String download_yyb = "https://a.app.qq.com/o/simple.jsp?pkgname=com.zhuosen.chaoqijiaoyu#opened";


    public static final String LOGIN_OUT_URL = "www.baidu.com";


    public static final String BindWX = "web.chaoqick.com/asset?type=bindWx";
    public static final String BindZFB = "web.chaoqick.com/asset?type=bindZfb";


}
