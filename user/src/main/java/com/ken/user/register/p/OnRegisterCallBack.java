package com.ken.user.register.p;

/**
 * 注册接口回调p层
 *
 * @author by ken on 2018/8/3
 * */
public interface OnRegisterCallBack {

    /**
     * 注册成功
     * */
    void onSuccess();
    /**
     *  注册失败
     * @param error 失败回调
     * */
    void onFailure(String error);
}
