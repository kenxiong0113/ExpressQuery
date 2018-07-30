package com.ken.expressquery.mainui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ken.expressquery.R;
import com.ken.expressquery.mainui.MainActivity;
import com.ken.expressquery.user.Login;
import com.ken.expressquery.utils.ExitPressed;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author by ken on 2017/9/11.
 * 登录活动界面
 */
public class LoginActivity extends AppCompatActivity {
    private final static int PHONE_LENGTH = 11;
    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.view1)
    View view1;
    @BindView(R.id.view2)
    View view2;
    @BindView(R.id.tv_forget)
    TextView tvForget;
    @BindView(R.id.tv_register)
    TextView tvRegister;
    private String phone, password;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        mContext = getApplicationContext();

    }


    @OnClick({R.id.tv_register, R.id.btn_login, R.id.tv_forget})
    public void onClick(View view) {
        int msg = view.getId();
        switch (msg) {
            case R.id.tv_register:
                startActivity(new Intent(mContext, RegisterActivity.class));
                finish();
                break;
            case R.id.btn_login:
                phone = etPhone.getText().toString();
                password = etPassword.getText().toString();
                if (phone.length() != PHONE_LENGTH) {
                    Toast.makeText(mContext, "请输入正确的手机号",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Login.getLogin().login(phone, password, new Login.CallBack() {
                        @Override
                        public void success() {
                            startActivity(new Intent(mContext, MainActivity.class));
                            finish();
                        }

                        @Override
                        public void failure(String error) {
                            Toast.makeText(mContext, error, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                break;
            case R.id.tv_forget:

                break;
            default:
                break;
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
}
