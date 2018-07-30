package com.ken.expressquery.map.v;

import android.app.Activity;

/**
 * 定位MVP模式的View层
 *
 * @author by ken on 2018/5/19
 */
public interface LocationView {
    /**
     * 定位的activity
     *
     * @return activity
     */
    Activity getActivity();

    /***
     * 显示dialog
     * */
    void showDialog();

    /**
     * 隐藏dialog
     */
    void dismissDialog();

    /**
     * 定位成功
     *
     * @param province  省份
     * @param city      城市
     * @param district  城区
     * @param street    街道
     * @param streetNum 门牌号
     */
    void onSuccess(String province, String city, String district, String street, String streetNum);

    /**
     * 定位失败
     *
     * @param str 定位失败信息
     */
    void onFailure(String str);

}
