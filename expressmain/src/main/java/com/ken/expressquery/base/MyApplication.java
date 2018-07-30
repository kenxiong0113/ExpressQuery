package com.ken.expressquery.base;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.ken.expressquery.greendao.DaoMaster;
import com.ken.expressquery.greendao.DaoSession;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.tencent.bugly.Bugly;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;

import es.dmoral.toasty.Toasty;

/**
 * @author by ken on 2018/4/28.
 * 全局基类
 */

public class MyApplication extends Application {
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);

    }

    /*** 静态单例*/
    public static MyApplication instances;
    private DaoMaster.DevOpenHelper mHelper;
    private SQLiteDatabase db;
    private DaoMaster mDaoMaster;
    private DaoSession mDaoSession;

    public static MyApplication getInstances() {
        return instances;
    }

    @Override
    public void onCreate() {
        super.onCreate();
//        初始化bugly
        Bugly.init(getApplicationContext(), "09af291644", true);
        instances = this;
//        初始化扫码库
        ZXingLibrary.initDisplayOpinion(this);
//        初始化Logger{https://github.com/orhanobut/logger}
        Logger.addLogAdapter(new AndroidLogAdapter());

//        配置toast
//        toast颜色可以自定义
//        具体参考github{https://github.com/GrenderG/Toasty}
        Toasty.Config.getInstance().apply(); // required
        setDatabase();
    }

    /**
     * 设置greenDao
     */
    private void setDatabase() {
        // 通过 DaoMaster 的内部类 DevOpenHelper，你可以得到一个便利的 SQLiteOpenHelper 对象。
        // 可能你已经注意到了，你并不需要去编写「CREATE TABLE」这样的 SQL 语句，因为 greenDAO 已经帮你做了。
        // 注意：默认的 DaoMaster.DevOpenHelper 会在数据库升级时，删除所有的表，意味着这将导致数据的丢失。
        // 所以，在正式的项目中，你还应该做一层封装，来实现数据库的安全升级。
        mHelper = new DaoMaster.DevOpenHelper(this, "sport-db", null);
        db = mHelper.getWritableDatabase();
        // 注意：该数据库连接属于 DaoMaster，所以多个 Session 指的是相同的数据库连接。
        mDaoMaster = new DaoMaster(db);
        mDaoSession = mDaoMaster.newSession();
    }

    public DaoSession getDaoSession() {
        return mDaoSession;
    }

    public SQLiteDatabase getDb() {
        return db;
    }



}
