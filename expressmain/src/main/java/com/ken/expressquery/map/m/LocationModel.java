package com.ken.expressquery.map.m;

import android.content.Context;

import com.ken.expressquery.map.OnLocationFinishListener;

/**
 * 定位接口 model层
 *
 * @author by ken on 2018/5/18
 */
public interface LocationModel {
    /**
     * 高德定位接口
     * @param mContext 定位的activity
     * @param onLocationFinishListener 定位回调接口
     * */
    void location(Context mContext, OnLocationFinishListener onLocationFinishListener);
    /**
     * 销毁定位
     */
    void destroy();
}
