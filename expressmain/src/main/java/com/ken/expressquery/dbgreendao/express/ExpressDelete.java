package com.ken.expressquery.dbgreendao.express;

import com.ken.expressquery.greendao.ExpressInfoDao;

/**
 * 删除本地数据库中的快递历史查询信息
 *
 * @author by ken on 2018/5/18
 */
public class ExpressDelete {

    private static ExpressDelete instances = null;
            private ExpressDelete(){}
    public static ExpressDelete getInstances(){
        if (instances == null){
            synchronized (ExpressDelete.class){
                if (instances == null){
                    instances = new ExpressDelete();
                }
            }
        }
        return instances;
    }

    public void delete(ExpressInfoDao mDao,Long i){
        mDao.deleteByKey(i);
    }
}
