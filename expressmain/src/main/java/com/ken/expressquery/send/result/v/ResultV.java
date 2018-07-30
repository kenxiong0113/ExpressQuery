package com.ken.expressquery.send.result.v;

import com.ken.expressquery.send.bean.SendExpressOrder;

import java.util.List;

/**
 * 预约寄件返回的结果保存到Bmob后的view层
 *
 * @author by ken on 2018/5/31
 */
public interface ResultV {
    /**
     * 成功返回
     *
     * @param str 返回信息
     */
    void bSuccess(String str);

    /**
     * 返回失败
     *
     * @param error 失败信息
     */
    void bFailure(String error);

    void bQuerySuccess(List<SendExpressOrder> list, int vrg);
}
