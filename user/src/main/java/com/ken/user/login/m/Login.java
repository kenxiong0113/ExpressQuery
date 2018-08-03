package com.ken.user.login.m;

import android.util.Log;

import com.ken.user.login.p.OnLoginCallBack;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * @author by ken on 2018/4/26.
 * 登录功能
 */

public class Login implements ILoginModel {
    private static final String TAG = "Login";
    private static Login instance = null;
    private Login() {
    }

    public static Login getInstance() {
        if (instance == null) {
            synchronized (Login.class) {
                if (instance == null) {
                    instance = new Login();
                }
            }
        }
        return instance;
    }


    @Override
    public void login(String username, String password, final OnLoginCallBack onLoginCallBack) {
        BmobUser user = new BmobUser();
        user.setUsername(username);
        user.setPassword(password);
        user.login(new SaveListener<BmobUser>() {
            @Override
            public void done(BmobUser user, BmobException e) {
                if (e == null) {
                    onLoginCallBack.onSuccess();
                } else {
                    Log.e(TAG, "done: "+e.getErrorCode() + e.getMessage());
                    onLoginCallBack.onFailure(e.getErrorCode() + e.getMessage());
                }
            }
        });
    }

}
