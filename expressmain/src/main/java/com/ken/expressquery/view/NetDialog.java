package com.ken.expressquery.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.ken.expressquery.R;
/**
 * @author by ken on 2018/8/1
 *
 * 网络监听提示的顶部自定义dialog
 * */
public class NetDialog extends Dialog {
    Context mContext;

    public NetDialog(@NonNull Context context) {
        super(context,R.style.MyDialog);
        this.mContext = context;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_network);
    }

    @Override
    public void show() {
        super.show();
/*** 设置宽度全屏，要设置在show的后面*/
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
//        显示在顶部
        layoutParams.gravity = Gravity.TOP;
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        getWindow().getDecorView().setPadding(0, 0, 0, 0);
        getWindow().setAttributes(layoutParams);
    }
}
