package com.ken.expressquery.send.myorder.p;

import com.ken.base.bean.User;
import com.ken.expressquery.send.bean.ResultData;
import com.ken.expressquery.send.bean.SendExpressOrder;
import com.ken.expressquery.send.myorder.OnOrderFinishCallback;
import com.ken.expressquery.send.myorder.m.IModelOrder;
import com.ken.expressquery.send.myorder.m.OrderImp;
import com.ken.expressquery.send.myorder.v.IViewOrder;

import java.util.List;

public class OrderPer {
    IModelOrder iModelOrder;
    IViewOrder iViewOrder;

    public OrderPer(IViewOrder iViewOrder) {
        this.iViewOrder = iViewOrder;
        this.iModelOrder = new OrderImp();
    }

    public void insert(String com, String no, SendExpressOrder order, User user) {
        iModelOrder.insertResult(com, no, order, user, new OnOrderFinishCallback() {
            @Override
            public void onSuccess(String str) {
                iViewOrder.onOrderSuccess(str);
            }

            @Override
            public void onFailure(String error) {

            }

            @Override
            public void onQuerySuccess(List<ResultData> list) {

            }
        });
    }

    public void query(User user) {
        iModelOrder.queryResult(user, new OnOrderFinishCallback() {
            @Override
            public void onSuccess(String str) {

            }

            @Override
            public void onFailure(String error) {
                iViewOrder.onOrderFailure(error);
            }

            @Override
            public void onQuerySuccess(List<ResultData> list) {
                iViewOrder.onOrderQuerySuccess(list);
            }
        });

    }
}
