package com.ken.expressquery.management.address;

import com.ken.expressquery.management.bean.AddressBook;

import java.util.List;

/**
 * 地址管理回调接口
 *
 * @author by ken on 2018/5/27
 * */
public interface OnAddressFinishCallBack {
    /**
     * 回调失败
     * @param error 错误信息*/
    void onFinishFailure(String error);
    /**
     * 回调成功
     * @param obj 成功返回信息
     * */
    void onFinishSuccess(String obj);

    /**
     * 查询地址成功回调
     * @param list 返回地址信息列表
     * */
    void onQuerySuccess(List<AddressBook> list);
}
