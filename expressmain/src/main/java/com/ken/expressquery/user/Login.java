package com.ken.expressquery.user;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * @author by ken on 2018/4/26.
 * 登录功能
 */

public class Login {
    private static Login login = null;
        private Login(){}
    public static Login getLogin(){
        if (login == null){
            synchronized (Login.class){
                if (login == null){
                    login = new Login();
                }
            }
        }
        return login;
    }

    public void login(String username, String password, final CallBack callBack){
        this.mCallBack = callBack;
        BmobUser user = new BmobUser();
        user.setUsername(username);
        user.setPassword(password);
        user.login(new SaveListener<BmobUser>() {
            @Override
            public void done(BmobUser user, BmobException e) {
                if (e == null){
                    callBack.success();
                }else {
                    callBack.failure(e.getErrorCode()+e.getMessage());
                }
            }
        });

    }
        public Login.CallBack mCallBack;
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
