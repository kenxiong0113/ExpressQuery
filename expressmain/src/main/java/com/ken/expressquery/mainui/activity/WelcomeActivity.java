package com.ken.expressquery.mainui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.alibaba.android.arouter.launcher.ARouter;
import com.hjq.permissions.OnPermission;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;
import com.ken.base.BaseActivity;
import com.ken.base.network.NetworkUtils;
import com.ken.expressquery.R;
import com.ken.expressquery.mainui.MainActivity;

import java.util.List;

import cn.bmob.v3.BmobUser;

/**
 * @author by ken on 2017/9/11.
 * 启动欢迎活动界面
 */

public class WelcomeActivity extends BaseActivity {
    BmobUser bmobUser;
    private Context mContext;
    private static final String TAG = "WelcomeActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_welcome;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        toolbar.setVisibility(View.GONE);
        mContext = getApplicationContext();
        getPermissions();
    }

    @Override
    protected void onNetworkConnected(NetworkUtils.NetType type) {

    }

    @Override
    protected void onNetworkDisConnected() {

    }

    private void runApp() {
        startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
        finish();
    }

    /**
     * 获取用户缓存
     */
    private void getUserCache() {
        bmobUser = BmobUser.getCurrentUser();
        if (bmobUser != null) {
            // 允许用户使用应用
            startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
            finish();
        } else {
            //缓存用户对象为空时， 可打开用户登录界面…
            ARouter.getInstance().build("/login/login").navigation();
            finish();
        }
    }

    private void getPermissions() {

        XXPermissions.with(this)
                .constantRequest() //可设置被拒绝后继续申请，直到用户授权或者永久拒绝
                //.permission(Permission.REQUEST_INSTALL_PACKAGES, Permission.SYSTEM_ALERT_WINDOW) //支持请求安装权限和悬浮窗权限
                //支持多个权限组进行请求，不指定则默以清单文件中的危险权限进行请求
                .permission(Permission.Group.STORAGE, Permission.Group.LOCATION, Permission.Group.CAMERA)
                .request(new OnPermission() {

                    @Override
                    public void hasPermission(List<String> granted, boolean isAll) {
                        runApp();
                    }

                    @Override
                    public void noPermission(List<String> denied, boolean quick) {
                        runApp();
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}