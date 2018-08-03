package com.ken.user.register.m;

import android.util.Log;

import com.ken.base.bean.User;
import com.ken.user.register.p.OnRegisterCallBack;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * @author by ken on 2018/4/26.
 * 注册功能
 */

public class Register implements IRegisterModel {

    private static final String TAG = "Register";
    private static Register instance = null;
    private Register() {
    }

    public static Register getInstance() {
        if (instance == null) {
            synchronized (Register.class) {
                if (instance == null) {
                    instance = new Register();
                }
            }
        }
        return instance;
    }

    @Override
    public void register(String username, String password, final OnRegisterCallBack onRegisterCallBack) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        //注意：不能用save方法进行注册
        user.signUp(new SaveListener<User>() {
            @Override
            public void done(User user, BmobException e) {
                if (e == null) {
                    onRegisterCallBack.onSuccess();
                } else {
                    Log.e(TAG, "done: "+e.getErrorCode() + e.getMessage() );
                    onRegisterCallBack.onFailure(e.getErrorCode() + e.getMessage());
                }
            }
        });


    }
}
