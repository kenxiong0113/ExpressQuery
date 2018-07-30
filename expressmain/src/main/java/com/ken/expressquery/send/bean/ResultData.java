package com.ken.expressquery.send.bean;

import com.ken.expressquery.model.User;

import cn.bmob.v3.BmobObject;

/**
 * 寄件订单实体类,后台可查
 *
 * @author by ken on 2018/7/2
 */
public class ResultData extends BmobObject {

    private SendExpressOrder sendId;
    private User userId;
    private String shipperCode;
    private String logistsicCode;

    public ResultData(SendExpressOrder sendId, User userId, String shipperCode, String logistsicCode) {
        this.sendId = sendId;
        this.userId = userId;
        this.shipperCode = shipperCode;
        this.logistsicCode = logistsicCode;
    }

    public ResultData() {
    }

    public SendExpressOrder getSendId() {
        return sendId;
    }

    public void setSendId(SendExpressOrder sendId) {
        this.sendId = sendId;
    }

    public User getUserId() {
        return userId;
    }

    public void setUserId(User userId) {
        this.userId = userId;
    }

    public String getShipperCode() {
        return shipperCode;
    }

    public void setShipperCode(String shipperCode) {
        this.shipperCode = shipperCode;
    }

    public String getLogistsicCode() {
        return logistsicCode;
    }

    public void setLogistsicCode(String logistsicCode) {
        this.logistsicCode = logistsicCode;
    }
}
