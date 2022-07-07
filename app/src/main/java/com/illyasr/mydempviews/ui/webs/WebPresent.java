package com.illyasr.mydempviews.ui.webs;


import androidx.databinding.library.baseAdapters.BuildConfig;

import com.illyasr.mydempviews.base.BasePresenter;

/**
 * TODO
 *
 * @author Administrator
 * @version 1.0
 * @date 2022/3/17 15:36
 */
public class WebPresent extends BasePresenter {
//    private static final String BASE_ADVERSE_ADDRESS = "http://192.168.1.195:8091/#";
    private static final String BASE_ADVERSE_ADDRESS = "https://ehealthy.cc/PUC-H5/#";
//    private static final String BASE_ADVERSE_ADDRESS = MyApplication.getInstance().isIsDebugEnv()?"http://192.168.1.195:8091/#":"https://ehealthy.cc/PUC-H5/#";
//     private static final String BASE_ADVERSE_ADDRESS = BuildConfig.WEB_BASE_HTTP_URI;
    public static final String USER_SERVER_AGREEMENT = BASE_ADVERSE_ADDRESS +"/userAgreement";//用户服务协议
    public static final String PRIVACY_POLICY = BASE_ADVERSE_ADDRESS +"/privacyPolicy";//隐私政策
    public static final String PRIVACY_HOME = BASE_ADVERSE_ADDRESS +"/home?pu_token=%s&staffId=%s";//主页
    public static final String PRIVACY_CAL = BASE_ADVERSE_ADDRESS +"/record?pu_token=%s&staffId=%s";//日历
    public static final String PRIVACY_COLLECT = BASE_ADVERSE_ADDRESS +"/collection?pu_token=%s&staffId=%s";//收藏

    public static final String ADD_NEW_PATIENT = BASE_ADVERSE_ADDRESS +"/?pu_token=%s&taskId=%s&orgId=%s&tdId=%s";//添加新患者
    public static final String WATCH_PATIENT = BASE_ADVERSE_ADDRESS +"/?pu_token=%s&patientId=%s&orgId=%s&tdId=%s";//查看患者详情
    public static final String HIS_DETAIL = BASE_ADVERSE_ADDRESS +"/Detail?pu_token=%s&patientId=%s";//历史
}
