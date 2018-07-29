package com.ken.login.m;

import com.ken.login.OnLoginCallback;

public interface ILogin {
    void login(String username, String password, OnLoginCallback onLoginCallback);
}
