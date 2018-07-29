package com.ken.expressquery.management.address.v;

import com.ken.expressquery.management.bean.AddressBook;

import java.util.List;

/**
 * 地址管理view层
 * @author by ken on 2018/5/31
 * */
public interface AddressView {
    /** 显示dialog*/
    void showDialog();
    /** 隐藏dialog*/
    void dismissDialog();

    /**
     * 插入数据成功
     * @param obj 返回插入数据的objectId
     * */
    void onInsertSuccess(Object obj);

    /**
     * 插入数据失败
     * @param error 失败信息
     * */
    void onInsertFailure(String error);


    /**
     * 查询数据成功
     * @param list 返回查询数据列表
     * */
    void onQuerySuccess(List<AddressBook> list);

    /**
     * 查询数据失败
     * @param error 失败信息
     * */
    void onQueryFailure(String error);

    /**
     * 删除数据成功
     * @param str 删除成功返回信息
     * */
    void onDeleteSuccess(String str);

    /**
     * 删除数据失败
     * @param error 失败信息
     * */
    void onDeleteFailure(String error);

    /**
     * 更新数据成功
     * @param str 更新成功返回信息
     * */
    void onUpdateSuccess(String str);

    /**
     * 更新数据失败
     * @param error 失败信息
     * */
    void onUpdateFailure(String error);

}
