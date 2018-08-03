package com.ken.user.login.v;
/**
 * 登录ui接口回调
 *
 * @author by ken on 2018/8/3
 * */
public interface ILoginView {
    /**
     * 登录成功
     * */
    void onLoginSuccessView();
/**
 * 登录失败
 *
 * @param error 失败信息
 * */
    void onLoginFailureView(String error);
}
