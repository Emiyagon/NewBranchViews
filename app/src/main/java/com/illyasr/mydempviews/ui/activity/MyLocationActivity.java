package com.illyasr.mydempviews.ui.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.illyasr.bilibili.biliplayer.utils.NetWorkUtils;
import com.illyasr.mydempviews.MainPresent;
import com.illyasr.mydempviews.MyApplication;
import com.illyasr.mydempviews.R;
import com.illyasr.mydempviews.base.BaseActivity;
import com.illyasr.mydempviews.databinding.ActivityMyLocationBinding;
import com.illyasr.mydempviews.util.GpsUtil;
import com.illyasr.mydempviews.util.PhoneUtil;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import androidx.core.app.ActivityCompat;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * 获取一些基本信息(当前定位,手机状态等)
 */
public class MyLocationActivity extends BaseActivity<ActivityMyLocationBinding, MainPresent> {


    private Location location;

    @Override
    protected int setLayoutId() {
        return R.layout.activity_my_location;
    }

    private StringBuffer sb = new StringBuffer();
    @Override
    protected void initData() {

        String[] messions = new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION
        };
        if (EasyPermissions.hasPermissions(this, messions)) {
            GetLoc();
         /*
            startLocation(this, new OnLocationListenter() {
                @Override
                public void getLatitudeAndLongitude(double longitude, double latitude) {
//                    aMapLocationClient.startLocation();
                    GetLoc(longitude,latitude);
                }
            });*/

        } else {
            //获取位置失败
            if (onLocationListenter != null) {
                onLocationListenter.getLatitudeAndLongitude(0, 0);
            }
            EasyPermissions.requestPermissions(this, getResources().getString(R.string.toast_1), 100, messions);
        }


    }


    //放入经纬度就可以了
    public String getAddress(double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude,
                    longitude, 1);
            if (addresses.size() > 0) {
                Address address = addresses.get(0);
                String data = address.toString();
                int startCity = data.indexOf("1:\"") + "1:\"".length();
                int endCity = data.indexOf("\"", startCity);
                String city = data.substring(startCity, endCity);

                int startPlace = data.indexOf("feature=") + "feature=".length();
                int endplace = data.indexOf(",", startPlace);
                String place = data.substring(startPlace, endplace);
                return city + place ;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "获取失败";
    }
    private void GetLoc() {
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        Criteria criteria = new Criteria();
        // 设置查询条件
        criteria.setAccuracy(Criteria.ACCURACY_FINE); // 设置准确而非粗糙的精度
        criteria.setPowerRequirement(Criteria.POWER_LOW); // 设置相对省电而非耗电，一般高耗电量会换来更精确的位置信息
        criteria.setAltitudeRequired(false); // 不需要提供海拔信息
        criteria.setSpeedRequired(false); // 不需要速度信息
        criteria.setCostAllowed(false); // 不能产生费用
        //getBestProvider 只有允许访问调用活动的位置供应商将被返回
        String providerName = lm.getBestProvider(criteria, true);
        if (providerName != null) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "没有权限", Toast.LENGTH_SHORT).show();
                //获取位置失败
                if (onLocationListenter != null) {
                    onLocationListenter.getLatitudeAndLongitude(0, 0);
                }
                return;
            }

            location = lm.getLastKnownLocation(providerName);
//            location = lm.getCurrentLocation(providerName);

            //获取维度信息
            double latitude = location.getLatitude();
            //获取经度信息
            double longitude = location.getLongitude();

            sb = new StringBuffer();
            sb.append("私人信息\n");
            sb.append("手机型号 : "+ PhoneUtil.getSystemModel()+"\n");
            sb.append("手机系统语言 : "+ PhoneUtil.getSystemLanguage()+"\n");
            sb.append("手机系统版本号 : "+ PhoneUtil.getSystemVersion()+"\n");
            sb.append("手机厂商 : "+ PhoneUtil.getDeviceBrand()+"\n");
            sb.append("手机网络状态 : "+ NetWorkUtils.getNetworkTypeName(MyApplication.getInstance()) +"\n");
            sb.append("网络可用状态 : "+
                    (isNetworkConnected(MyApplication.getInstance())||isWifiConnected(MyApplication.getInstance())||isMobileConnected(MyApplication.getInstance())||ping())
                    +"\n");
            sb.append("当前纬度  lat = " +latitude +"\n");
            sb.append("当前经度  log = " + longitude +"\n");
            sb.append("当前位置   "+getAddress(latitude,longitude) +"\n");
//            sb.append("当前位置   "+aMapLocationListener.);

            mBindingView. tvMsg.setText(sb.toString());
//            Log.i("获取经纬度", "定位方式： " + providerName + "  维度：" + latitude + "  经度：" + longitude);
        } else {
            Toast.makeText(this, "1.请检查网络连接 \n2.请打开我的位置", Toast.LENGTH_SHORT).show();
            sb = new StringBuffer();
            sb.append("私人信息\n");
            sb.append("手机型号 : "+ PhoneUtil.getSystemModel()+"\n");
            sb.append("手机系统语言 : "+ PhoneUtil.getSystemLanguage()+"\n");
            sb.append("手机系统版本号 : "+ PhoneUtil.getSystemVersion()+"\n");
            sb.append("手机厂商 : "+ PhoneUtil.getDeviceBrand()+"\n");
            sb.append("手机网络状态 : "+ NetWorkUtils.getNetworkTypeName(MyApplication.getInstance()) +"\n");
            sb.append("网络可用状态 : "+
                    (isNetworkConnected(MyApplication.getInstance())||isWifiConnected(MyApplication.getInstance())||isMobileConnected(MyApplication.getInstance())||ping())
                    +"\n");
            sb.append("当前纬度  lat = " +0 +"\n");
            sb.append("当前经度  log = " + 0 +"\n");
            sb.append("当前位置  定位失败 " +"\n");

           mBindingView. tvMsg.setText(sb.toString());
        }




    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    //----------------------- 高德-*---------------------------------------
    public OnLocationListenter onLocationListenter;
    public AMapLocationClient aMapLocationClient;

    /**
     * 高德地图参数配置
     *
     * @return
     */
    public AMapLocationClientOption parameterConfiguration() {
        //初始化AMapLocationClientOption对象
        AMapLocationClientOption mLocationOption = new AMapLocationClientOption();
        //设置定位模式（采取高精度定位模式）
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置单次定位
        mLocationOption.setOnceLocation(true);
        //获取3s内最精确的一次定位结果
        mLocationOption.setOnceLocationLatest(true);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //设置是否允许模拟位置,默认为true，允许模拟位置
//        mLocationOption.setMockEnable(true);
        //单位是毫秒，默认30000毫秒，建议超时时间不要低于8000毫秒。
//        mLocationOption.setHttpTimeOut(20000);
        return mLocationOption;
    }

    /**
     * 开始定位
     *
     * @return
     */
    public void startLocation(Context context, OnLocationListenter onLocationListenter) {
        this.onLocationListenter = onLocationListenter;
        //初始化定位
        try {
            aMapLocationClient = new AMapLocationClient(context);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //设置定位成功监听
        aMapLocationClient.setLocationOption(parameterConfiguration());
        //成功结果监听
        aMapLocationClient.setLocationListener(aMapLocationListener);
        //检测是否有GPS权限



    }

    /**
     * 定位监听
     */
    public interface OnLocationListenter {
        /**
         * 获取经纬度
         *
         * @param longitude 经度
         * @param latitude  纬度
         */
        void getLatitudeAndLongitude(double longitude, double latitude);
    }

    AMapLocationListener aMapLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            if (onLocationListenter != null) {
                aMapLocationClient.onDestroy();
                onLocationListenter.getLatitudeAndLongitude(aMapLocation.getLongitude(), aMapLocation.getLatitude());
            }
        }
    };



    //
    /**
     * 定位回调监听器
     */
    public AMapLocationListener mLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation amapLocation) {
            if (!GpsUtil.isGpsEnabled(getApplicationContext())) {
               showToast("GPS不可用");
            } else {
                if (amapLocation != null) {
                    if (amapLocation.getErrorCode() == 0) {
                        //定位成功回调信息，设置相关消息
                        amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
                        double currentLat = amapLocation.getLatitude();//获取纬度
                        double currentLon = amapLocation.getLongitude();//获取经度
//                        LatLonPoint latLonPoint = new LatLonPoint(currentLat, currentLon);  // latlng形式的
                        /*currentLatLng = new LatLng(currentLat, currentLon);*/   //latlng形式的
                        Log.i("currentLocation", "currentLat : " + currentLat + " currentLon : " + currentLon);
                        amapLocation.getAccuracy();//获取精度信息
                    } else {
                        //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                        Log.e("AmapError", "location Error, ErrCode:"
                                + amapLocation.getErrorCode() + ", errInfo:"
                                + amapLocation.getErrorInfo());
                    }
                }
            }
        }
    };
}
