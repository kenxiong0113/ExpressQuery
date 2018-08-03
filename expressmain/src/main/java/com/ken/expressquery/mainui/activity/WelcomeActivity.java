package com.ken.expressquery.mainui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.alibaba.android.arouter.launcher.ARouter;
import com.ken.expressquery.R;
import com.ken.base.BaseActivity;
import com.ken.expressquery.mainui.MainActivity;
import com.ken.base.network.NetworkUtils;
import com.ken.base.utils.PermissionHelper;

import cn.bmob.v3.BmobUser;

/**
 * @author by ken on 2017/9/11.
 * 启动欢迎活动界面
 */

public class WelcomeActivity extends BaseActivity {
    BmobUser bmobUser;
    private PermissionHelper mPermissionHelper;
    private Context mContext;
    private String TAG = WelcomeActivity.class.getName();

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
        // 当系统为6.0以上时，需要申请权限
        mPermissionHelper = new PermissionHelper(this);
        mPermissionHelper.setOnApplyPermissionListener(new PermissionHelper.OnApplyPermissionListener() {
            @Override
            public void onAfterApplyAllPermission() {
                Log.i(TAG, "All of requested permissions has been granted, so run app logic.");
                runApp();
            }
        });
        if (Build.VERSION.SDK_INT < 23) {
            // 如果系统版本低于23，直接跑应用的逻辑
            Log.d(TAG, "The api level of system is lower than 23, so run app logic directly.");
            runApp();
        } else {
            // 如果权限全部申请了，那就直接跑应用逻辑
            if (mPermissionHelper.isAllRequestedPermissionGranted()) {
                Log.d(TAG, "All of requested permissions has been granted, so run app logic directly.");
                runApp();
            } else {
                // 如果还有权限为申请，而且系统版本大于23，执行申请权限逻辑
                Log.i(TAG, "Some of requested permissions hasn't been granted, so apply permissions first.");
                mPermissionHelper.applyPermissions();
            }
        }
    }

    @Override
    protected void onNetworkConnected(NetworkUtils.NetType type) {

    }

    @Override
    protected void onNetworkDisConnected() {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mPermissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mPermissionHelper.onActivityResult(requestCode, resultCode, data);
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


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}