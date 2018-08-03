package com.ken.expressquery.send.result.m;

import com.ken.base.bean.User;
import com.ken.expressquery.send.result.OnResultFinishCallBack;

/**
 * 寄件返回数据 model层
 *
 * @author by ken on 2018/5/29
 */
public interface ResultM {
    /**
     * 保存寄件成功的返回数据到bmob云后端
     */
    void insertOrder(String sendName,
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
                     OnResultFinishCallBack onResultFinishCallBack);

    /**
     * @param user     用户
     * @param callBack 回调
     */
    void queryOrder(User user, OnResultFinishCallBack callBack);
}
