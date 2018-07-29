package com.ken.expressquery.send.appointment;

/**
 * 寄送快递回调接口
 *
 * @author by ken on 2018/5/23
 * */
public interface OnSendFinishCallBack {
    /**
     * 预约成功回调
     * @param str 成功信息
     * */
    void onFinishSuccess(String str);
    /**
     * 预约失败回调
     * @param error 失败信息
     * */
    void onFinishFailure(String error);

    /**
     * 预约成功
     * @param data 成功返回json数据
     * */
    void resultSuccess(String data);
    /**
     * 预约失败
     * @param error 失败信息
     * */
    void resultFailure(String error);
}
