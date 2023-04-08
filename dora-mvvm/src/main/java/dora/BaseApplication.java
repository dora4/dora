package dora;

import android.app.Application;
import android.content.Context;

import dora.lifecycle.application.AppDelegate;
import dora.lifecycle.application.ApplicationLifecycleCallbacks;
import dora.util.MultiLanguageUtils;

/**
 * 继承这个类就相当于在AndroidManifest.xml中的application节点自动添加了
 * <metadata name="dora.lifecycle.config.DefaultGlobalConfig" value="GlobalConfig"/>
 */
public class BaseApplication extends Application {

    private ApplicationLifecycleCallbacks mAppDelegate;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(MultiLanguageUtils.init().attachBaseContext(base));
        MultiLanguageUtils.getInstance().setConfiguration(this);
        if (mAppDelegate == null) {
            mAppDelegate = new AppDelegate(base);
        }
        mAppDelegate.attachBaseContext(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (mAppDelegate != null) {
            mAppDelegate.onCreate(this);
        }
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        if (mAppDelegate != null) {
            mAppDelegate.onTerminate(this);
        }
    }
}
