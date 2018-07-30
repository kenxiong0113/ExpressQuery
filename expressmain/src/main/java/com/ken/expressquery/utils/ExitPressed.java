package com.ken.expressquery.utils;

import android.content.Context;
import android.widget.Toast;

import com.ken.expressquery.base.BaseConstant;

import es.dmoral.toasty.Toasty;

/**
 * 按两次退出程序
 *
 * @author by ken on 2017/12/16.
 */

public class ExitPressed {
    public static boolean exitPressed(Context context) {
        boolean isExit = false;
        if (System.currentTimeMillis() - BaseConstant.RunTime.EXIT_TIME > BaseConstant.RunTime.TIME) {
            Toasty.error(context, "再按一次退出程序", Toast.LENGTH_SHORT, false).show();
            BaseConstant.RunTime.EXIT_TIME = System.currentTimeMillis();
            isExit = false;
        } else {
            isExit = true;
        }
        return isExit;
    }

}
