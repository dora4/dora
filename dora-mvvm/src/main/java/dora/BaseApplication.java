package dora;

import android.app.Application;
import android.content.Context;

import dora.lifecycle.application.AppDelegate;
import dora.lifecycle.application.ApplicationLifecycleCallbacks;
import dora.util.LanguageUtils;

/**
 * Inheriting this class is equivalent to automatically adding it to the application node in
 * AndroidManifest.xml.<metadata name="dora.lifecycle.config.DefaultGlobalConfig" value="GlobalConfig"/>
 * 简体中文：继承这个类就相当于在AndroidManifest.xml中的application节点自动添加了
 * <metadata name="dora.lifecycle.config.DefaultGlobalConfig" value="GlobalConfig"/>
 */
public class BaseApplication extends Application {

    /**
     * Default proxies for the application lifecycle.
     * 简体中文：application生命周期的默认代理。
     */
    private ApplicationLifecycleCallbacks mAppDelegate;

    /**
     * @see Application#attachBaseContext(Context)
     */
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LanguageUtils.attachBaseContext(base));
        if (mAppDelegate == null) {
            mAppDelegate = new AppDelegate(base);
        }
        mAppDelegate.attachBaseContext(base);
    }

    /**
     * @see Application#onCreate()
     */
    @Override
    public void onCreate() {
        super.onCreate();
        if (mAppDelegate != null) {
            mAppDelegate.onCreate(this);
        }
    }

    /**
     * @see Application#onTerminate()
     */
    @Override
    public void onTerminate() {
        super.onTerminate();
        if (mAppDelegate != null) {
            mAppDelegate.onTerminate(this);
        }
    }
}
