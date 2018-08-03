package com.ken.base.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import java.util.ArrayList;

import static com.ken.base.utils.BaseConstant.NET_CHANGE_ACTION;


/**
 * 网络监听广播
 *
 * @author by ken on 2018/7/31
 * */
public class NetStateReceiver extends BroadcastReceiver {
    private static final String TAG = "NetStateReceiver";
    private static ArrayList<NetChangeObserver> mNetChangeObservers = new ArrayList<NetChangeObserver>();
    private static NetStateReceiver instance;

    private static boolean isNetAvailable = false;
    private static NetworkUtils.NetType mNetType;
    private NetStateReceiver(){

    }

    public static NetStateReceiver getInstance(){
        if (instance == null){
            synchronized (NetStateReceiver.class){
                if (instance == null){
                    instance = new NetStateReceiver();
                }
            }
        }
        return instance;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equalsIgnoreCase(NET_CHANGE_ACTION)){
            if (NetworkUtils.isNetworkConnected(context) == null){
                isNetAvailable = true;
                if (NetworkUtils.isNetworkConnected(context) == NetworkUtils.NetType.WIFI){
                    mNetType = NetworkUtils.NetType.WIFI;
                }else if (NetworkUtils.isNetworkConnected(context) == NetworkUtils.NetType.MOBILE_NETWORK){
                    mNetType = NetworkUtils.NetType.MOBILE_NETWORK;
                }else if (NetworkUtils.isNetworkConnected(context) == NetworkUtils.NetType.OTHER){
                    mNetType = NetworkUtils.NetType.OTHER;
                }
            }else {
                isNetAvailable = false;
            }
            notifyObserver();
        }
    }

    public static boolean isNetworkAvailable() {
        return isNetAvailable;
    }


    private void notifyObserver() {
        if (!mNetChangeObservers.isEmpty()) {
            int size = mNetChangeObservers.size();
            for (int i = 0; i < size; i++) {
                NetChangeObserver observer = mNetChangeObservers.get(i);
                if (observer != null) {
                    if (isNetworkAvailable()) {
                        observer.onNetConnected(mNetType);
                    } else {
                        observer.onNetDisConnect();
                    }
                }
            }
        }
    }

    /**
     * 注册
     *
     * @param mContext
     */
    public static void registerNetworkStateReceiver(Context mContext) {
        Log.e(TAG, "registerNetworkStateReceiver: 注册广播" );
        IntentFilter filter = new IntentFilter();
        filter.addAction(NET_CHANGE_ACTION);
        mContext.getApplicationContext().registerReceiver(getInstance(), filter);

    }
    /**
     * 添加网络监听
     *
     * @param observer
     */
    public static void registerObserver(NetChangeObserver observer) {
        if (mNetChangeObservers == null) {
            mNetChangeObservers = new ArrayList<NetChangeObserver>();
        }
        mNetChangeObservers.add(observer);
        Log.e(TAG, "registerObserver: 添加观察者" );

    }
}
