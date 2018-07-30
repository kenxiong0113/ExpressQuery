package com.ken.expressquery.dbgreendao.express;

import com.ken.expressquery.model.ExpressInfo;

import java.util.List;

/**
 * 快递查询的回调信息
 *
 * @author by ken on 2018/5/18
 */
public interface ExpressCallBack {
    /**
     * 轨迹信息
     *
     * @param mList 本地数据库中的快递信息
     */
    void trajectoryInformation(List<ExpressInfo> mList);
}
