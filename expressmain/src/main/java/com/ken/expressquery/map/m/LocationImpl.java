package com.ken.expressquery.map.m;

import android.content.Context;
import android.util.Log;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.ken.expressquery.map.OnLocationFinishListener;

/**
 * @author by ken on 2018/3/13.
 * 高德地图定位功能
 */

public  class LocationImpl  implements LocationModel{
    //省份//城市//城区//街道//门牌号
    private String province;
    private String city;
    private String district;
    private String street;
    private String streetNum;
    /*** 经度*/
    private double longitude;
    /*** 纬度*/
    private double latitude;
    /**
     * 声明AMapLocationClient类对象
     */
    private AMapLocationClient mLocationClient = null;
    /**
     * 声明定位回调监听器
     */
    private AMapLocationListener mLocationListener = null;
    /**
     *  声明AMapLocationClientOption对象
     */

    private AMapLocationClientOption mLocationOption = null;
    @Override
    public void location(Context mContext, final OnLocationFinishListener onLocationFinishListener){
        //初始化定位
        mLocationClient = new AMapLocationClient(mContext);
        //初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();

        /*** 设置定位场景，目前支持三种场景（签到、出行、运动，默认无场景）*/
        mLocationOption.setLocationPurpose(AMapLocationClientOption.AMapLocationPurpose.SignIn);
        if(null != mLocationClient){
            mLocationClient.setLocationOption(mLocationOption);
            //设置场景模式后最好调用一次stop，再调用start以保证场景模式生效
            mLocationClient.stopLocation();
            mLocationClient.startLocation();
        }
//        高精度定位
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //获取最近3s内精度最高的一次定位结果：
//设置setOnceLocationLatest(boolean b)接口为true，启动定位时SDK会返回最近3s内精度最高的一次定位结果。
// 如果设置其为true，setOnceLocation(boolean b)接口也会被设置为true，反之不会，默认为false。
        mLocationOption.setOnceLocationLatest(true);
//设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //单位是毫秒，默认30000毫秒，建议超时时间不要低于8000毫秒。
        mLocationOption.setHttpTimeOut(10000);

        //设置定位回调监听
        mLocationListener = new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                if (aMapLocation != null){
                    if (aMapLocation.getErrorCode() == 0){
//                        省份
                        province = aMapLocation.getProvince();
//                        城市
                        city = aMapLocation.getCity();
//                        城区
                        district = aMapLocation.getDistrict();
//                        街道
                        street = aMapLocation.getStreet();
//                        街道门牌号
                        streetNum = aMapLocation.getStreetNum();


                        longitude = aMapLocation.getLongitude();
                        latitude = aMapLocation.getLatitude();
                        onLocationFinishListener.onLocationSuccess(province,city,district,street,streetNum);
                        Log.e("LocationImpl", province + city);
                    }else {
                        onLocationFinishListener.onLocationFailure(aMapLocation.getErrorInfo());
                        Log.e("LocationImpl", "aMapLocation.getErrorCode():" +
                                aMapLocation.getErrorCode()+aMapLocation.getErrorInfo());
                    }
                }
            }
        };
        /** 设置定位监听回调*/
        mLocationClient.setLocationListener(mLocationListener);
    }
    @Override
    public void destroy(){
        mLocationClient.onDestroy();
    }

}
