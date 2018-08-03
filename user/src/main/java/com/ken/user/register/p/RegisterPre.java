package com.ken.user.register.p;

import com.ken.user.register.m.IRegisterModel;
import com.ken.user.register.m.Register;
import com.ken.user.register.v.IRegisterView;

public class RegisterPre {
    IRegisterModel model;
    IRegisterView registerView;

    public RegisterPre(IRegisterView registerView) {
        this.registerView = registerView;
        model = Register.getInstance();
    }

   public void register(String username,String password){
        model.register(username, password, new OnRegisterCallBack() {
            @Override
            public void onSuccess() {
                registerView.onRegisterSuccessView();
            }

            @Override
            public void onFailure(String error) {
                registerView.onRegisterFailureView(error);
            }
        });

    }

}
