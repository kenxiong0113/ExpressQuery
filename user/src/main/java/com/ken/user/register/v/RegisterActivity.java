package com.ken.user.register.v;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.ken.base.BaseActivity;
import com.ken.base.bean.User;
import com.ken.base.network.NetworkUtils;
import com.ken.user.R;
import com.ken.user.login.p.LoginPre;
import com.ken.user.login.v.ILoginView;
import com.ken.user.login.v.LoginActivity;
import com.ken.user.register.p.RegisterPre;

import es.dmoral.toasty.Toasty;

/**
 * @author by ken  on 2017/9/11.
 * 注册活动界面
 */

@Route(path = "/register/register")
public class RegisterActivity extends BaseActivity implements IRegisterView,ILoginView,View.OnClickListener {
    private final static String EMPTY = "";
    private final static int PASSWORD_MIX_LENGTH = 6;
    private static final String TAG = "RegisterActivity";
    EditText etUsername;
    EditText etPassword;
    EditText etRePassword;
    Button btnRegister;
    TextView tvBackLogin;
    User user;
    private String username, password, rePassword;
    private Context mContext;
    LoginPre loginPre = new LoginPre(this);
    RegisterPre registerPre = new RegisterPre(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected int getContentView() {
        return R.layout.activity_register;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        toolbar.setVisibility(View.GONE);
        user = new User();
        mContext = getApplicationContext();
        initView();
    }

    /**
     * 初始化View
     */
    private void initView() {
        etUsername = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);
        etRePassword = findViewById(R.id.et_re_password);
        btnRegister = findViewById(R.id.btn_register);
        tvBackLogin = findViewById(R.id.tv_back_login);

        btnRegister.setOnClickListener(this);
        tvBackLogin.setOnClickListener(this);


    }

    @Override
    protected void onNetworkConnected(NetworkUtils.NetType type) {
        showNetErrorView();
    }

    @Override
    protected void onNetworkDisConnected() {
        dismissNetErrorView();
    }

    @Override
    public void onClick(View view) {
        int msg = view.getId();
        if (msg == R.id.btn_register) {
            username = etUsername.getText().toString();
            password = etPassword.getText().toString();
            rePassword = etRePassword.getText().toString();
            if (username.equals(EMPTY) || password.equals(EMPTY) || rePassword.equals(EMPTY)) {
                Toast.makeText(mContext, "以上内容不能为空", Toast.LENGTH_SHORT).show();
            } else if (password.length() < PASSWORD_MIX_LENGTH) {
                Toast.makeText(mContext, "密码不能少于6位", Toast.LENGTH_SHORT).show();
            } else if (password.equals(rePassword)) {
                registerPre.register(username,password);
            } else {
                Toast.makeText(mContext, "两次密码输入不一致",
                        Toast.LENGTH_SHORT).show();
            }

        }
        if (msg == R.id.tv_back_login) {
            startActivity(new Intent(mContext, LoginActivity.class));
            finish();

        }
    }

    @Override
    public void onLoginSuccessView() {
        ARouter.getInstance().build("/main/main").navigation();
        finish();
    }

    @Override
    public void onLoginFailureView(String error) {
        Toasty.error(mContext,error).show();
    }

    @Override
    public void onRegisterSuccessView() {
        loginPre.login(username,password);
    }

    @Override
    public void onRegisterFailureView(String error) {
        Toasty.error(mContext,"注册失败").show();
    }
}
