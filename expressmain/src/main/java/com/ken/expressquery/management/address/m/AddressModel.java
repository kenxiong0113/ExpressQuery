package com.ken.expressquery.management.address.m;

import com.ken.expressquery.management.address.OnAddressFinishCallBack;
import com.ken.expressquery.management.bean.AddressBook;
import com.ken.expressquery.model.User;

import java.util.List;

import cn.bmob.v3.BmobObject;

/**
 * 地址管理model层
 *
 * @author by ken on 2018/5/27
 * */
public interface AddressModel {
    /**
     * 添加到bmob后端
     * @param user 用户id
     * @param name 名字
     * @param phone 电话
     * @param address 地址
     * @param type 地址类型，true---寄件地址 ,false ---- 收件地址
     * @param onAddressFinishCallBack 回调
     * */
    void insert(User user, String name, String phone, String address, boolean type, OnAddressFinishCallBack onAddressFinishCallBack);

    /**
     * 从bmob后端删除
     * @param objectId 数据的id
     * @param onAddressFinishCallBack 回调
     * */
    void delete(String objectId, OnAddressFinishCallBack onAddressFinishCallBack);
    /**
     * 从bmob后端更新
     * @param objectId 数据的id
     * @param name 名字
     * @param phone 电话
     * @param address 地址
     * @param onAddressFinishCallBack 回调
     * */
    void update(String objectId, String name, String phone, String address, OnAddressFinishCallBack onAddressFinishCallBack);
    /**
     * 查询bmob 后端地址簿数据
     * @param user 用户id
     * @param type 地址类型，true---寄件地址 ,false ---- 收件地址
     * @param onAddressFinishCallBack 回调
     * */
    void query(User user, boolean type, OnAddressFinishCallBack onAddressFinishCallBack);

    /**
     * 批量更新数据，这里主要批量更新默认地址
     *
     * @param b1
     * @param b2
     * @param onAddressFinishCallBack 回调
     * */
    void updateBatch(AddressBook b1,AddressBook b2, OnAddressFinishCallBack onAddressFinishCallBack);

}
