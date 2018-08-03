package com.ken.user.register.m;

import com.ken.user.register.p.OnRegisterCallBack;

/**
 *
 * 注册model层
 * @author by ken on 2018/8/3
 * */
public interface IRegisterModel {

    /**
     * 注册
     * @param username 用户名
     * @param password 密码
     * @param onRegisterCallBack  回调
     * */
    void register(String username, String password, OnRegisterCallBack onRegisterCallBack);

}
