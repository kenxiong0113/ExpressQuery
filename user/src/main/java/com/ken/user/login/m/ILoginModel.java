package com.ken.user.login.m;

import com.ken.user.login.p.OnLoginCallBack;

/**
 * 登录接口
 *
 * @author by ken on 2018/8/3
 * */
public interface ILoginModel {
    /**
     * 登录
     * @param username 用户名
     * @param password 密码
     * @param onLoginCallBack 回调接口
     * */
    void login(String username, String password, OnLoginCallBack onLoginCallBack);
}
