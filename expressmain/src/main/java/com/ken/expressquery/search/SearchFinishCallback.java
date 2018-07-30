package com.ken.expressquery.search;

/**
 * 快递查询回调接口类
 *
 * @author by ken on 2018/5/18
 */
public interface SearchFinishCallback {
    /**
     * 快递查询成功回调
     *
     * @param result 查询结果
     */
    void onSuccess(String result);

    /**
     * 快递查询失败回调
     *
     * @param error 查询失败信息
     */
    void onFailure(String error);
}
