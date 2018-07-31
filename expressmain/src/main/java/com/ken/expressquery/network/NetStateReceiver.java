package com.ken.expressquery.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.ken.expressquery.base.BaseConstant;

import java.util.ArrayList;


/**
 * 网络监听广播
 *
 * @author by ken on 2018/7/31
 * */
public class NetStateReceiver extends BroadcastReceiver {
    private static ArrayList<NetChangeObserver> mNetChangeObservers;
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
        if (intent.getAction().equalsIgnoreCase(BaseConstant.NET_CHANGE_ACTION)){
            if (NetworkUtils.isNetworkConnected(context) == null){
                isNetAvailable = false;
                if (NetworkUtils.isNetworkConnected(context) == NetworkUtils.NetType.WIFI){
                    mNetType = NetworkUtils.NetType.WIFI;
                }else if (NetworkUtils.isNetworkConnected(context) == NetworkUtils.NetType.MOBILE_NETWORK){
                    mNetType = NetworkUtils.NetType.MOBILE_NETWORK;
                }else if (NetworkUtils.isNetworkConnected(context) == NetworkUtils.NetType.OTHER){
                    mNetType = NetworkUtils.NetType.OTHER;
                }
            }else {
                isNetAvailable = true;
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
     * 添加网络监听
     *
     * @param observer
     */
    public static void registerObserver(NetChangeObserver observer) {
        if (mNetChangeObservers == null) {
            mNetChangeObservers = new ArrayList<NetChangeObserver>();
        }
        mNetChangeObservers.add(observer);
    }
}
