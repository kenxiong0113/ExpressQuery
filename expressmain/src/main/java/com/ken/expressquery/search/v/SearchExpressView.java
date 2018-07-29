package com.ken.expressquery.search.v;

import android.content.Context;

/**
 * 快递查询View层
 *
 * @author by ken on 2018/5/23
 * */
public interface SearchExpressView {
    /**
     * 获取上下文
     * @return 返回上下文
     * */
    Context getContextView();
    /**
     * 查询显示dialog
     * */
    void showDialog();
    /**
     * 查询完成隐藏dialog
     * */
    void dismissDialog();

    /**
     * 查询快递成功
     * @param str 定位结果
     * */
    void onSuccess(String str);
    /**
     * 查询快递失败
     * @param str 定位失败信息
     * */
    void onFailure(String str);
    /**
     * 查询异常
     *
     * @param str 异常信息
     * */
    void showToast(String str);
}
