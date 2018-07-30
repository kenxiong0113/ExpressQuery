package com.ken.expressquery.send.appointment.m;

import com.ken.expressquery.send.appointment.OnSendFinishCallBack;

/**
 * 预约寄送快递model层
 *
 * @author by ken on 2018/5/23
 */
public interface SendExpressModel {
    /**
     * 寄送请求参数
     */
    void send(
            String orderCode,
            String sendName,
            String sendPhone,
            String sendAddress,
            String receiveName,
            String receivePhone,
            String receiveAddress,
            String goodsType,
            String weight,
            String packageNum,
            String cost,
            String leave,
            String company,
            OnSendFinishCallBack onSendFinishCallBack);


}
