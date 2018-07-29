package com.ken.expressquery.map.p;

import android.util.Log;

import com.ken.expressquery.map.OnLocationFinishListener;
import com.ken.expressquery.map.m.LocationImpl;
import com.ken.expressquery.map.m.LocationModel;
import com.ken.expressquery.map.v.LocationView;

/**
 * 定位MVP模式的Presenter层
 *
 * @author by ken on 2018/5/19
 * */
public class LocationPre {
    private LocationView locationView;
    private LocationModel locationModel;
    public LocationPre(LocationView mLocationView){
        this.locationView = mLocationView;
        this.locationModel = new LocationImpl();
    }
/**
 * 定位
 * */
    public void locationPre(){
        locationView.showDialog();
        locationModel.location(locationView.getActivity(), new OnLocationFinishListener() {

            @Override
            public void onLocationSuccess(String province, String city, String district, String street, String streetNum) {

                locationView.onSuccess(province,city,district,street,streetNum);
                locationView.dismissDialog();
            }

            @Override
            public void onLocationFailure(String str) {
                Log.e("LocationPre定位失败", str);
                locationView.onFailure(str);
                locationView.dismissDialog();
            }

        });
    }

    /**
    * 销毁定位
    * */
    public void onDestroy(){
        locationModel.destroy();
    }

}
