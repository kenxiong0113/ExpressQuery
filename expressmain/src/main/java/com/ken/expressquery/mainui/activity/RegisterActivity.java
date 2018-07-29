package com.ken.expressquery.mainui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ken.expressquery.R;
import com.ken.expressquery.mainui.MainActivity;
import com.ken.expressquery.model.User;
import com.ken.expressquery.user.Login;
import com.ken.expressquery.user.Register;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author by ken  on 2017/9/11.
 * 注册活动界面
 */

public class RegisterActivity extends AppCompatActivity {
    @BindView(R.id.et_username)
    EditText etUsername;
    @BindView(R.id.view)
    View view;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.view1)
    View view1;
    @BindView(R.id.et_re_password)
    EditText etRePassword;
    @BindView(R.id.view2)
    View view2;
    @BindView(R.id.btn_register)
    Button btnRegister;
    @BindView(R.id.tv_back_login)
    TextView tvBackLogin;
    private String username, password, rePassword;
    private TextView tvBack;
    private Context mContext;
    User user;
    private final static String EMPTY = "";
    private final static int PASSWORD_MIX_LENGTH = 6;
    private final static String TAG = RegisterActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        user = new User();
        mContext = getApplicationContext();
    }


    @OnClick({R.id.btn_register,R.id.tv_back_login})
    public void onListener(View view) {
        switch (view.getId()) {
            case R.id.btn_register:
                username = etUsername.getText().toString();
                password = etPassword.getText().toString();
                rePassword = etRePassword.getText().toString();
                if (username.equals(EMPTY) || password.equals(EMPTY) || rePassword.equals(EMPTY)) {
                    Toast.makeText(mContext, "以上内容不能为空", Toast.LENGTH_SHORT).show();
                } else if (password.length() < PASSWORD_MIX_LENGTH) {
                    Toast.makeText(mContext, "密码不能少于6位", Toast.LENGTH_SHORT).show();
                } else if (password.equals(rePassword)) {
                    Register.getRegister().register(username, password, new Register.CallBack() {
                        @Override
                        public void success() {
                            Login.getLogin().login(username, password, new Login.CallBack() {
                                @Override
                                public void success() {
                                    startActivity(new Intent(mContext, MainActivity.class));
                                    finish();
                                }

                                @Override
                                public void failure(String error) {
                                    Toast.makeText(mContext, "登录失败", Toast.LENGTH_SHORT).show();
                                    Log.e(TAG, error + "登录失败");
                                }
                            });
                        }

                        @Override
                        public void failure(String error) {
                            Toast.makeText(mContext, "注册失败", Toast.LENGTH_SHORT).show();
                            Log.e(TAG, error + "注册失败");
                        }
                    });

                } else {
                    Toast.makeText(mContext, "两次密码输入不一致",
                            Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.tv_back_login:
                startActivity(new Intent(mContext, LoginActivity.class));
                finish();
                break;
            default:
                break;
        }
    }


}
