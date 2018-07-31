package com.ken.expressquery.base;

import android.app.Dialog;
import android.content.Context;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.ken.expressquery.R;
import com.ken.expressquery.network.NetChangeObserver;
import com.ken.expressquery.network.NetStateReceiver;
import com.ken.expressquery.network.NetworkUtils;


/**
 * @author by ken on 2017/4/14.
 * 封装基础Activity,包含toolbar的初始化
 */

public abstract class BaseActivity extends AppCompatActivity {
    Toolbar toolbar;
    FrameLayout viewContent;
    TextView tvTitle;
    OnClickListener onClickListenerTopLeft;
    OnClickListener onClickListenerTopRight;
    int menuResId;
    String menuStr;
    Dialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_toolbar);
        toolbar = findViewById(R.id.toolbar);
        viewContent = findViewById(R.id.viewContent);
        tvTitle = findViewById(R.id.tvTitle);
        //将继承 TopBarBaseActivity 的布局解析到 FrameLayout 里面
        LayoutInflater.from(this).inflate(getContentView(), viewContent);
        //初始化设置 Toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        init(savedInstanceState);

        broadcastReceiver();
    }

    protected void setTitle(String title) {
        if (!TextUtils.isEmpty(title)) {
            tvTitle.setText(title);
        }
    }

    protected void setTopLeftButton() {
        setTopLeftButton(R.drawable.ic_return, null);
    }

    protected void setTopLeftButton(int iconResId, OnClickListener onClickListener) {
        toolbar.setNavigationIcon(iconResId);
        this.onClickListenerTopLeft = onClickListener;
    }

    protected void setTopRightButton(int menuResId, OnClickListener onClickListener) {
        this.onClickListenerTopRight = onClickListener;
        this.menuResId = menuResId;
    }

    protected void setTopRightButton(String menuStr, OnClickListener onClickListener) {
        this.onClickListenerTopRight = onClickListener;
        this.menuStr = menuStr;

    }

    protected void setTopRightButton(String menuStr, int menuResId, OnClickListener onClickListener) {
        this.menuResId = menuResId;
        this.menuStr = menuStr;
        this.onClickListenerTopRight = onClickListener;
    }

    /**
     * 获取布局文件接口
     *
     * @return 布局R.layout.XXX
     */
    protected abstract int getContentView();

    /**
     * activity bundle 初始化接口
     *
     * @param savedInstanceState
     */
    protected abstract void init(Bundle savedInstanceState);

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (menuResId != 0 || !TextUtils.isEmpty(menuStr)) {
            getMenuInflater().inflate(R.menu.menu_activity_base_top_bar, menu);
        }
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (menuResId != 0) {
            menu.findItem(R.id.menu_1).setIcon(menuResId);
        }
        if (!TextUtils.isEmpty(menuStr)) {
            menu.findItem(R.id.menu_1).setTitle(menuStr);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onClickListenerTopLeft.onClick();
        } else if (item.getItemId() == R.id.menu_1) {
            onClickListenerTopRight.onClick();
        }
        // true 告诉系统我们自己处理了点击事件
        return true;
    }

    public interface OnClickListener {
        /**
         * toolBar 监听接口重写
         */
        void onClick();
    }

    void broadcastReceiver(){
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BaseConstant.NET_CHANGE_ACTION);
        registerReceiver(NetStateReceiver.getInstance(),intentFilter);

        NetChangeObserver observer = new NetChangeObserver() {
            @Override
            public void onNetConnected(NetworkUtils.NetType type) {
                onNetworkConnected(type);
            }

            @Override
            public void onNetDisConnect() {
                onNetworkDisConnected();
            }
        };

        NetStateReceiver.registerObserver(observer);

    }

    /**
     * 网络连接状态
     *
     * @param type 网络状态
     */
    protected abstract void onNetworkConnected(NetworkUtils.NetType type);

    /**
     * 网络断开的时候调用
     */
    protected abstract void onNetworkDisConnected();


    public void showNetErrorDialog(Context context){
        if (dialog == null){
            dialog = new Dialog(context);
            Window window = dialog.getWindow();
            WindowManager.LayoutParams layoutParams = window.getAttributes();
            layoutParams.gravity = Gravity.TOP;
            layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
            window.setAttributes(layoutParams);
            dialog.setCancelable(false);
            View view = LayoutInflater.from(context).inflate(R.layout.layout_network,null);
            dialog.setContentView(view);
            dialog.show();
        }else {
            dialog.dismiss();
        }

    }


}