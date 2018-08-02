package com.ken.expressquery.base;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import com.alibaba.android.arouter.launcher.ARouter;
import com.ken.base.BaseApp;
import com.ken.expressquery.R;
import com.ken.expressquery.*;
import com.ken.expressquery.greendao.DaoMaster;
import com.ken.expressquery.greendao.DaoSession;
import com.ken.expressquery.mainui.MainActivity;
import com.ken.expressquery.network.NetStateReceiver;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.tencent.bugly.beta.Beta;
import com.tencent.tinker.loader.shareutil.ShareConstants;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;

import cn.bmob.v3.Bmob;
import es.dmoral.toasty.Toasty;

import static com.ken.expressquery.base.BaseConstant.BMOB_APP_KEY;

/**
 * enableProxyApplication = false 的情况
 * 这是Tinker推荐的接入方式，一定程度上会增加接入成本，但具有更好的兼容性。
 * */
/**
 * * @author by ken on 2018/4/28.
 *  全局基类
 * /**
 * ************************************************************************
 * **                              _oo0oo_                               **
 * **                             o8888888o                              **
 * **                             88" . "88                              **
 * **                             (| -_- |)                              **
 * **                             0\  =  /0                              **
 * **                           ___/'---'\___                            **
 * **                        .' \\\|     |// '.                          **
 * **       张冠之          / \\\|||  :  |||// \\         顾博君         **
 * **                      / _ ||||| -:- |||||- \\                       **
 * **                      | |  \\\\  -  /// |   |                       **
 * **                      | \_|  ''\---/''  |_/ |                       **
 * **                      \  .-\__  '-'  __/-.  /                       **
 * **                    ___'. .'  /--.--\  '. .'___                     **
 * **                 ."" '<  '.___\_<|>_/___.' >'  "".                  **
 * **                | | : '-  \'.;'\ _ /';.'/ - ' : | |                 **
 * **                \  \ '_.   \_ __\ /__ _/   .-' /  /                 **
 * **            ====='-.____'.___ \_____/___.-'____.-'=====             **
 * **                              '=---='                               **
 * ************************************************************************
 * **                        佛祖保佑      镇类之宝                      **
 * ************************************************************************
 */
public class MyApplication extends BaseApp {
    private static final String TAG = "MyApplication";
    /*** 静态单例*/
    public static MyApplication instances;
    private DaoMaster.DevOpenHelper mHelper;
    private SQLiteDatabase db;
    private DaoMaster mDaoMaster;
    private DaoSession mDaoSession;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);

    }
    public static MyApplication getInstances() {
        return instances;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // 初始化 ARouter
        if (isDebug()) {           // 这两行必须写在init之前，否则这些配置在init过程中将无效
            ARouter.openLog();     // 打印日志
            ARouter.openDebug();   // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
        }
        // 初始化组件 Application
        initModuleApp(this);

        // 其他操作

        // 所有 Application 初始化后的操作
        initModuleData(this);

        instances = this;

    }

    private boolean isDebug() {
        return BuildConfig.DEBUG;
    }
    /**
     * 设置greenDao
     */
    private void setDatabase() {
        /**
         * 通过 DaoMaster 的内部类 DevOpenHelper，你可以得到一个便利的 SQLiteOpenHelper 对象。
         * 可能你已经注意到了，你并不需要去编写「CREATE TABLE」这样的 SQL 语句，因为 greenDAO 已经帮你做了。
         * 注意：默认的 DaoMaster.DevOpenHelper 会在数据库升级时，删除所有的表，意味着这将导致数据的丢失。
         * 所以，在正式的项目中，你还应该做一层封装，来实现数据库的安全升级。*/
        mHelper = new DaoMaster.DevOpenHelper(this, "sport-db", null);
        db = mHelper.getWritableDatabase();
        /** 注意：该数据库连接属于 DaoMaster，所以多个 Session 指的是相同的数据库连接。*/
        mDaoMaster = new DaoMaster(db);
        mDaoSession = mDaoMaster.newSession();
    }

    public DaoSession getDaoSession() {
        return mDaoSession;
    }

    public SQLiteDatabase getDb() {
        return db;
    }

    /**
     *
     * Beta高级设置
     * **/
    private void initBugly(){

        /**
         * true表示app启动自动初始化升级模块;
         * false不会自动初始化;
         * 开发者如果担心sdk初始化影响app启动速度，可以设置为false，
         * 在后面某个时刻手动调用Beta.init(getApplicationContext(),false);
         */
        Beta.autoInit = true;

        /**
         * true表示初始化时自动检查升级;
         * false表示不会自动检查升级,需要手动调用Beta.checkUpgrade()方法;
         */
        Beta.autoCheckUpgrade = true;

        /**
         * 设置升级检查周期为60s(默认检查周期为0s)，60s内SDK不重复向后台请求策略);
         */
        Beta.upgradeCheckPeriod = 60 * 1000;

        /**
         * 设置启动延时为1s（默认延时3s），APP启动1s后初始化SDK，避免影响APP启动速度;
         */
        Beta.initDelay = 1 * 1000;

        /**
         * 设置通知栏大图标，largeIconId为项目中的图片资源;
         */
        Beta.largeIconId = R.mipmap.app_logo;

        /**
         * 设置状态栏小图标，smallIconId为项目中的图片资源Id;
         */

        Beta.smallIconId = R.drawable.version_update;

        /**
         * 设置更新弹窗默认展示的banner，defaultBannerId为项目中的图片资源Id;
         * 当后台配置的banner拉取失败时显示此banner，默认不设置则展示“loading“;
         */

        Beta.defaultBannerId = R.drawable.version_update;


        /**
         * 设置sd卡的Download为更新资源保存目录;
         * 后续更新资源会保存在此目录，需要在manifest中添加WRITE_EXTERNAL_STORAGE权限;
         */
        Beta.storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

        /**
         * 点击过确认的弹窗在APP下次启动自动检查更新时会再次显示;
         */
        Beta.showInterruptedStrategy = true;

        /**
         * 只允许在MainActivity上显示更新弹窗，其他activity上不显示弹窗;
         * 不设置会默认所有activity都可以显示弹窗;
         */
        Beta.canShowUpgradeActs.add(MainActivity.class);

//        统一初始化Bugly产品，包含Beta
//        Bugly.init(getApplicationContext(), BaseConstant.BUGLY_APP_ID, true);

    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();

    }

    @Override
    public void initModuleApp(Application application) {
        NetStateReceiver.registerNetworkStateReceiver(this);
        initBugly();
//        初始化扫码库
        ZXingLibrary.initDisplayOpinion(this);
//初始化BmobSDK
        Bmob.initialize(this, BMOB_APP_KEY);
//        初始化Logger{https://github.com/orhanobut/logger}
        Logger.addLogAdapter(new AndroidLogAdapter());

//        配置toast
//        toast颜色可以自定义
//        具体参考github{https://github.com/GrenderG/Toasty}
        Toasty.Config.getInstance().apply(); // required
        setDatabase();

    }

    @Override
    public void initModuleData(Application application) {


    }
}
