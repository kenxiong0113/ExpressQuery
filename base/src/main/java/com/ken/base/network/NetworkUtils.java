package com.ken.base.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/**
 * 检查当前网络状态
 *
 * @author by ken on 2018/3/10.
 */

public class NetworkUtils {
    private static final String TAG = "NetworkUtils";

   public static enum NetType {
       WIFI,MOBILE_NETWORK,OTHER
   }
    /**
     * 判断是否有网络连接
     *
     * @param context 上下文
     * @return true 有网络 false 无网络
     */
    public static NetType isNetworkConnected(Context context) {
        ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
        if (mNetworkInfo != null) {
            Log.e(TAG, "isNetworkConnected: "+mNetworkInfo.getType() );
            Log.e(TAG, "isNetworkConnected: ConnectivityManager:"+ConnectivityManager.TYPE_WIFI );
            if (mNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                Log.e(TAG, "WIFI可用");
                return NetType.WIFI;
            } else if (mNetworkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                Log.e(TAG, "手机移动网可用");
                return NetType.MOBILE_NETWORK;
            } else {
                return NetType.OTHER;
            }
        }else {
            return null;
        }
    }
}
