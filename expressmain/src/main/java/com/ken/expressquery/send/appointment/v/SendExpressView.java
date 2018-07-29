package com.ken.expressquery.send.appointment.v;

/**
 * 预约寄件的View层
 *
 * @author by ken on 2018/5/23
 * */
public interface SendExpressView {

    void showSendDialog();
    void dismissSendDialog();
    /**
     * 预约成功
     * @param str 成功回调数据
     * */
    void onSendSuccess(String str);
    /**
     * 预约失败
     * @param error 失败回调信息
     * */
    void onSendFailure(String error);

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
