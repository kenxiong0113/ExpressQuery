package com.ken.expressquery.send.myorder.m;

import com.ken.base.bean.User;
import com.ken.expressquery.send.bean.SendExpressOrder;
import com.ken.expressquery.send.myorder.OnOrderFinishCallback;

public interface IModelOrder {
    /**
     * 插入订单数据
     */
    void insertResult(String com, String no, SendExpressOrder order, User user, OnOrderFinishCallback onOrderFinishCallback);

    /**
     * 查询订单数据
     */
    void queryResult(User user, OnOrderFinishCallback onOrderFinishCallback);
}
