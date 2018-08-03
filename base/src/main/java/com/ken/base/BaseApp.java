package com.ken.base;

import android.app.Application;

import com.tencent.tinker.loader.app.TinkerApplication;
import com.tencent.tinker.loader.shareutil.ShareConstants;

public abstract class BaseApp extends TinkerApplication {

    protected BaseApp(int tinkerFlags, String delegateClassName, String loaderClassName, boolean tinkerLoadVerifyFlag) {
        super(tinkerFlags, delegateClassName, loaderClassName, tinkerLoadVerifyFlag);
    }

    /**
     * Application 初始化
     * @param application aaa
     */
    public abstract void initModuleApp(Application application);

    /**
     * 所有 Application 初始化后的自定义操作
     * @param application  aaa
     */
    public abstract void initModuleData(Application application);
}
