package com.ken.user.login.p;

import com.ken.user.login.m.ILoginModel;
import com.ken.user.login.m.Login;
import com.ken.user.login.v.ILoginView;

/**
 *
 * 登录p层
 * @author by ken on 2018/8/3
 * */
public class LoginPre {
    ILoginModel login;
    ILoginView iLoginView;

    public LoginPre(ILoginView loginView) {
        this.iLoginView = loginView;
        login = Login.getInstance();
    }

    public void login(String username,String password) {
        login.login(username, password, new OnLoginCallBack() {
            @Override
            public void onSuccess() {
                iLoginView.onLoginSuccessView();
            }

            @Override
            public void onFailure(String error) {
                iLoginView.onLoginFailureView(error);
            }
        });

    }
}
