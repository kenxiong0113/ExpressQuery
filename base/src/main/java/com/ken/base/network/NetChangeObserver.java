package com.ken.base.network;

/**
 * 网络改变观察者，观察网络改变后回调的方法
 *
 * @author by ken on 2018/7/31
 */
public interface NetChangeObserver {

    /**
     * 网络连接回调
     * @param type 为网络类型
     */
     void onNetConnected(NetworkUtils.NetType type);

    /**
     * 没有网络
     */
     void onNetDisConnect();
}
