package com.ken.expressquery.send.bean;


import cn.bmob.v3.BmobObject;

/**
 * 预约寄件返回数据
 *
 * @author by ken on 2018/5/29
 */
public class Result extends BmobObject {

    /**
     * EBusinessID : 1237100
     * resultSuccess : true
     * Order : {"OrderCode":"012657018199"," ShipperCode ":" SF "," LogisticCode ":""}
     * ResultCode : 100
     * Reason :
     */
    private String EBusinessID;
    private boolean Success;
    private OrderBean Order;
    private String ResultCode;
    private String Reason;

    public String getEBusinessID() {
        return EBusinessID;
    }

    public void setEBusinessID(String EBusinessID) {
        this.EBusinessID = EBusinessID;
    }

    public boolean isSuccess() {
        return Success;
    }

    public void setSuccess(boolean success) {
        Success = success;
    }

    public OrderBean getOrder() {
        return Order;
    }

    public void setOrder(OrderBean order) {
        Order = order;
    }

    public String getResultCode() {
        return ResultCode;
    }

    public void setResultCode(String resultCode) {
        ResultCode = resultCode;
    }

    public String getReason() {
        return Reason;
    }

    public void setReason(String reason) {
        Reason = reason;
    }

    public static class OrderBean {
        /**
         * OrderCode : 012657018199
         * ShipperCode  :  SF
         * LogisticCode  :
         */

        private String OrderCode;
        private String ShipperCode;
        private String LogistsicCode;

        public String getLogistsicCode() {
            return LogistsicCode;
        }

        public void setLogistsicCode(String logistsicCode) {
            this.LogistsicCode = logistsicCode;
        }

        public String getShipperCode() {
            return ShipperCode;
        }

        public void setShipperCode(String shipperCode) {
            this.ShipperCode = shipperCode;
        }


        public String getOrderCode() {
            return OrderCode;
        }

        public void setOrderCode(String OrderCode) {
            this.OrderCode = OrderCode;
        }
    }
}
