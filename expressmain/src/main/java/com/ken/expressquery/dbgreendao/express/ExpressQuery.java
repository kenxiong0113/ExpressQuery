package com.ken.expressquery.dbgreendao.express;

import com.ken.base.greendao.ExpressInfoDao;
import com.ken.base.model.ExpressInfo;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

/**
 * 查询本地数据库中是否存在快递单号的插入记录
 *
 * @author by ken on  2018/5/18
 */
public class ExpressQuery {
    private static ExpressQuery instances = null;

    private ExpressQuery() {
    }

    public static ExpressQuery getInstances() {
        if (instances == null) {
            synchronized (ExpressQuery.class) {
                if (instances == null) {
                    instances = new ExpressQuery();
                }
            }
        }
        return instances;
    }

    /**
     * 本地数据库快递信息查询
     *
     * @param mDao     dao实例
     * @param no       快递单号
     * @param callBack 回调
     * @return 本地是否存在单号为no 的数据
     */
    public boolean query(ExpressInfoDao mDao, String no, ExpressCallBack callBack) {
        mDao.loadAll();
        QueryBuilder<ExpressInfo> qb = mDao.queryBuilder();
        if (no != null) {
//            查询指定的快递单号信息，用来判断本地数据库中是否存在该条快递单号的快递信息
            qb.where(ExpressInfoDao.Properties.No.eq(no));
        } else {
//            查询全部的快递单号信息，用来加载快递查询的历史记录

        }
//        按ID降序序排列
        List<ExpressInfo> mList = qb.orderDesc(ExpressInfoDao.Properties.Id).list();
        if (mList.size() != 0) {
//            有记录
            callBack.trajectoryInformation(mList);
            return true;
        } else {
//            空数据
            callBack.trajectoryInformation(mList);
            return false;
        }
    }
}
