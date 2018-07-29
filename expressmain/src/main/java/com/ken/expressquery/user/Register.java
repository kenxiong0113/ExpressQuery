package com.ken.expressquery.user;

import com.ken.expressquery.model.User;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * @author by ken on 2018/4/26.
 * 注册功能
 */

public class Register {
    private static Register register = null;
        private Register(){}
    public static Register getRegister(){
        if (register == null){
            synchronized (Register.class){
                if (register == null){
                    register = new Register();
                }
            }
        }
        return register;
    }

    public void register(String username, String password, final CallBack callBack){
        this.mCallBack = callBack;
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        //注意：不能用save方法进行注册
        user.signUp(new SaveListener<User>() {
            @Override
            public void done(User user, BmobException e) {
                if (e == null){
                    callBack.success();
                }else {
                    callBack.failure(e.getErrorCode()+e.getMessage());
                }
            }
        });
    }
        public Register.CallBack mCallBack;
        public interface CallBack{
            /**
             * 成功
             */
            void success();

            /**
             * 失败
             * @param error 后台访问失败错误码和错误信息
             */
            void failure(String error);
        }
}
