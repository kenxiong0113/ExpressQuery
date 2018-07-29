package com.ken.expressquery.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.util.Log;

/**
 * 检查当前网络状态
 * @author  by ken on 2018/3/10.
 */

public class CheckCurrentNetwork {
    /**
     * 判断是否有网络连接
     *
     * @param context 上下文
     * @return true 有网络 false 无网络
     */
    public static int isNetworkConnected(Context context) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                if (mNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                    Log.e("CheckCurrentNetwork", "WIFI可用");
                    return 1;
                } else if (mNetworkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                    Log.e("CheckCurrentNetwork", "手机移动网可用");
                    return 2;
                }else {
                    return 0;
                }
            }
            return -1;
        }
}
