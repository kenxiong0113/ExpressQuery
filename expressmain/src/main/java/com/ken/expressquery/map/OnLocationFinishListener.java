package com.ken.expressquery.map;

/**
 * 定位完成回调接口
 *
 * @author by ken on 2018/5/18
 */
public interface OnLocationFinishListener {

    /**
     * 定位成功
     *
     * @param province  省份
     * @param city      城市
     * @param district  城区
     * @param street    街道
     * @param streetNum 门牌号
     */
    void onLocationSuccess(String province, String city, String district, String street, String streetNum);

    /**
     * 定位失败
     *
     * @param str 定位失败信息
     */
    void onLocationFailure(String str);

}
