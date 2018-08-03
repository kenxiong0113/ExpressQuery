package com.ken.expressquery.send.result.m;

import com.ken.base.bean.User;
import com.ken.expressquery.send.bean.SendExpressOrder;
import com.ken.expressquery.send.result.OnResultFinishCallBack;
import com.orhanobut.logger.Logger;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * 返回预约寄件成功后把数据保存到bmob的实现类
 *
 * @author by ken on 2018/5/29
 */
public class IResult implements ResultM {
    private static IResult instances = null;

    private IResult() {
    }

    public static IResult getInstances() {
        if (instances == null) {
            synchronized (IResult.class) {
                if (instances == null) {
                    instances = new IResult();
                }
            }
        }
        return instances;
    }

    @Override
    public void insertOrder(String sendName, String sendPhone, String sendAddress, String receiveName, String receivePhone, String receiveAddress, String goodsType, String weight, String packageNum, String cost, String leave, String company, final OnResultFinishCallBack onResultFinishCallBack) {

        SendExpressOrder order = new SendExpressOrder();
        order.setsName(sendName);
        order.setsPhone(sendPhone);
        order.setsAddress(sendAddress);
        order.setrName(receiveName);
        order.setrPhone(receivePhone);
        order.setrAddress(receiveAddress);
        order.setGoodsType(goodsType);
        order.setWeight(weight);
        order.setPackageNum(packageNum);
        order.setCost(cost);
        order.setLeave(leave);
//        保存用户id
        order.setUser(BmobUser.getCurrentUser(User.class));
        order.setExpressCompany(company);
        order.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    onResultFinishCallBack.onBmobSuccess(s);
                } else {
                    onResultFinishCallBack.onBmobFailure(e.getErrorCode() + e.getMessage());
                }
            }
        });
    }

    @Override
    public void queryOrder(User user, final OnResultFinishCallBack callBack) {
        BmobQuery<SendExpressOrder> query = new BmobQuery<>();
        query.order("-orderNumber");
        query.findObjects(new FindListener<SendExpressOrder>() {
            @Override
            public void done(List<SendExpressOrder> list, BmobException e) {
                if (e == null) {
                    callBack.onQuerySuccess(list);
                } else {
                    callBack.onBmobFailure(e.getErrorCode() + e.getMessage());
                    Logger.d("IResult", e.getErrorCode() + e.getMessage());
                }

            }
        });
    }
}
