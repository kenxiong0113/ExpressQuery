package com.ken.expressquery.dbgreendao.express;

import com.ken.expressquery.greendao.ExpressInfoDao;
import com.ken.expressquery.model.ExpressInfo;

/**
 * 更新本地数据库中的快递信心
 *
 * @author by ken on 2018/5/18
 */
public class ExpressUpdate {
    private static ExpressUpdate instances = null;

    private ExpressUpdate() {
    }

    public static ExpressUpdate getInstances() {
        if (instances == null) {
            synchronized (ExpressUpdate.class) {
                if (instances == null) {
                    instances = new ExpressUpdate();
                }
            }
        }
        return instances;
    }

    public void update(ExpressInfoDao mDao, Long id, ExpressInfo info) {
        info = new ExpressInfo(id, info.getNo(), info.getName(), info.getLogo(), info.getList());
        mDao.update(info);
    }
}
