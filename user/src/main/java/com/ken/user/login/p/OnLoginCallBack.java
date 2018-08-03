package com.ken.user.login.p;
/**
 * 登录接口回调
 * @author by ken on 2018/8/3
 * */
public interface OnLoginCallBack {

    /**
     * 登录成功
     * */
    void onSuccess();
    /**
     *  登录失败
     * @param error 失败回调
     * */
    void onFailure(String error);

}
