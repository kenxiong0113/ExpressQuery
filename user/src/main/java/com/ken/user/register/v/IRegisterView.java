package com.ken.user.register.v;
/**
 *
 * */
public interface IRegisterView {

    /**
     * 登录成功
     * */
    void onRegisterSuccessView();
    /**
     * 登录失败
     *
     * @param error 失败信息
     * */
    void onRegisterFailureView(String error);
}
