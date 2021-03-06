package com.ken.expressquery.dbgreendao.express;

import com.ken.base.greendao.ExpressInfoDao;
import com.ken.base.model.ExpressInfo;

/**
 * 插入快递信息的数据
 * <p>
 * no - 快递单号
 * com - 公司名称
 *
 * @author by ken on 2018/5/18
 */
public class ExpressInsert {
    private static ExpressInsert instances = null;

    private ExpressInsert() {
    }

    public static ExpressInsert getInstances() {
        if (instances == null) {
            synchronized (ExpressInsert.class) {
                if (instances == null) {
                    instances = new ExpressInsert();
                }
            }
        }
        return instances;
    }

    /**
     * 插入
     */
    public void insert(ExpressInfoDao mDao, ExpressInfo info) {
        ExpressInfo expressInfo = new ExpressInfo(null,
                info.getNo(),
                info.getName(),
                info.getLogo(),
                info.getList());
        mDao.insert(expressInfo);
    }
}
