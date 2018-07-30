package com.ken.expressquery.send.myorder.m;

import com.ken.expressquery.model.User;
import com.ken.expressquery.send.bean.ResultData;
import com.ken.expressquery.send.bean.SendExpressOrder;
import com.ken.expressquery.send.myorder.OnOrderFinishCallback;
import com.orhanobut.logger.Logger;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * 订单模块的后台交互业务
 *
 * @author by ken on 2018/7/2
 */
public class OrderImp implements IModelOrder {
    @Override
    public void insertResult(String com, String no, SendExpressOrder order, User user, final OnOrderFinishCallback onOrderFinishCallback) {
        ResultData data = new ResultData();
        data.setShipperCode(com);
        data.setLogistsicCode(no);
        data.setUserId(user);
        data.setSendId(order);
        data.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    onOrderFinishCallback.onSuccess(s);
                } else {
                    onOrderFinishCallback.onFailure(e.getErrorCode() + e.getMessage());
                    Logger.e(e.getErrorCode() + e.getMessage());
                }
            }
        });
    }

    @Override
    public void queryResult(User user, final OnOrderFinishCallback onOrderFinishCallback) {
        BmobQuery<ResultData> query = new BmobQuery<>();
        query.addWhereEqualTo("userId", user);
        // 数据关联
        query.include("sendId");
        query.order("-createdAt");
        query.findObjects(new FindListener<ResultData>() {
            @Override
            public void done(List<ResultData> list, BmobException e) {
                if (e == null) {
                    onOrderFinishCallback.onQuerySuccess(list);

                } else {
                    onOrderFinishCallback.onFailure(e.getErrorCode() + e.getMessage());
                    Logger.e(e.getErrorCode() + e.getMessage());
                }
            }
        });
    }
}
