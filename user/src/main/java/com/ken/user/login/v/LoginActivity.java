package com.ken.user.login.v;

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
import com.ken.base.network.NetworkUtils;
import com.ken.base.utils.ExitPressed;
import com.ken.user.R;
import com.ken.user.login.p.LoginPre;
import com.ken.user.register.v.RegisterActivity;

import es.dmoral.toasty.Toasty;

/**
 * @author by ken on 2017/9/11.
 * 登录活动界面
 */
@Route(path = "/login/login")
public class LoginActivity extends BaseActivity implements ILoginView, View.OnClickListener {
    private final static int PHONE_LENGTH = 11;
    EditText etPhone;
    EditText etPassword;
    Button btnLogin;
    TextView tvForget;
    TextView tvRegister;
    private String phone, password;
    private Context mContext;
    private LoginPre loginPre = new LoginPre(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_login;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        mContext = getApplicationContext();
        toolbar.setVisibility(View.GONE);
        initView();
    }

    /**
     * 初始化View
     */
    private void initView() {
        etPhone = findViewById(R.id.et_phone);
        etPassword = findViewById(R.id.et_password);
        btnLogin = findViewById(R.id.btn_login);
        tvForget = findViewById(R.id.tv_forget);
        tvRegister = findViewById(R.id.tv_register);

        tvRegister.setOnClickListener(this);
        tvForget.setOnClickListener(this);
        btnLogin.setOnClickListener(this);

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
        if (msg == R.id.tv_register) {
            startActivity(new Intent(mContext, RegisterActivity.class));
            finish();
        }

        if (msg == R.id.btn_login) {
            phone = etPhone.getText().toString();
            password = etPassword.getText().toString();
            if (phone.length() != PHONE_LENGTH) {
                Toast.makeText(mContext, "请输入正确的手机号",
                        Toast.LENGTH_SHORT).show();
            } else {
                loginPre.login(phone, password);
            }

        }

        if (msg == R.id.tv_forget) {

        }
    }

    /**
     * 按两次back退出应用程序
     */
    @Override
    public void onBackPressed() {
        if (ExitPressed.exitPressed(mContext)) {
            finish();
        }
    }

    @Override
    public void onLoginSuccessView() {
        Toasty.success(mContext, "登录成功").show();
        ARouter.getInstance().build("/main/main").navigation();
        finish();
    }

    @Override
    public void onLoginFailureView(String error) {
        Toasty.error(mContext, error).show();
    }
}
