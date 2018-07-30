package com.ken.expressquery.send.appointment.p;

import com.ken.expressquery.send.appointment.OnSendFinishCallBack;
import com.ken.expressquery.send.appointment.m.SendExpressImpl;
import com.ken.expressquery.send.appointment.m.SendExpressModel;
import com.ken.expressquery.send.appointment.v.SendExpressView;

/**
 * 预约寄件Presenter层
 *
 * @author by ken on 2018/5/23
 */
public class SendExpressPre {
    private SendExpressView sendExpressView;
    private SendExpressModel sendExpress;

    public SendExpressPre(SendExpressView sendExpressView) {
        this.sendExpressView = sendExpressView;
        this.sendExpress = SendExpressImpl.getInstances();
    }

    public void send(
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
            String company) {
        sendExpress.send(orderCode,
                sendName,
                sendPhone,
                sendAddress,
                receiveName,
                receivePhone,
                receiveAddress,
                goodsType,
                weight,
                packageNum,
                cost,
                leave,
                company,
                new OnSendFinishCallBack() {

                    @Override
                    public void onFinishSuccess(String str) {
                        sendExpressView.onSendSuccess(str);
                    }

                    @Override
                    public void onFinishFailure(String error) {
                        sendExpressView.onSendFailure(error);
                    }

                    @Override
                    public void resultSuccess(String data) {
                        sendExpressView.resultSuccess(data);
                    }

                    @Override
                    public void resultFailure(String error) {
                        sendExpressView.resultFailure(error);
                    }
                });
    }
}
