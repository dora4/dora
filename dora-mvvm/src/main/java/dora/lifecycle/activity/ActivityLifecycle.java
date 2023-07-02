package dora.lifecycle.activity;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import java.util.ArrayList;
import java.util.List;

import dora.lifecycle.application.AppLifecycle;
import dora.lifecycle.application.ApplicationLifecycleCallbacks;
import dora.memory.ActivityCache;
import dora.memory.Cache;
import dora.lifecycle.fragment.FragmentLifecycle;
import dora.lifecycle.config.GlobalConfig;
import dora.lifecycle.application.ManifestParser;

public class ActivityLifecycle implements Application.ActivityLifecycleCallbacks {

    private List<FragmentManager.FragmentLifecycleCallbacks> mFragmentLifecycles = new ArrayList<>();

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        if (activity instanceof ActivityCache) {
            ActivityDelegate activityDelegate = fetchActivityDelegate(activity);
            if (activityDelegate == null) {
                Cache<String, Object> cache = ((ActivityCache) activity).loadCache();
                activityDelegate = new ActivityDelegateImpl(activity);
                cache.put(ActivityDelegate.CACHE_KEY, activityDelegate);
            }
            activityDelegate.onCreate(savedInstanceState);
        }
        registerFragmentCallbacks(activity);
    }

    @Override
    public void onActivityStarted(Activity activity) {
        ActivityDelegate activityDelegate = fetchActivityDelegate(activity);
        if (activityDelegate != null) {
            activityDelegate.onStart();
        }
    }

    @Override
    public void onActivityResumed(Activity activity) {
        ActivityDelegate activityDelegate = fetchActivityDelegate(activity);
        if (activityDelegate != null) {
            activityDelegate.onResume();
        }
    }

    @Override
    public void onActivityPaused(Activity activity) {
        ActivityDelegate activityDelegate = fetchActivityDelegate(activity);
        if (activityDelegate != null) {
            activityDelegate.onPause();
        }
    }

    @Override
    public void onActivityStopped(Activity activity) {
        ActivityDelegate activityDelegate = fetchActivityDelegate(activity);
        if (activityDelegate != null) {
            activityDelegate.onStop();
        }
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        ActivityDelegate activityDelegate = fetchActivityDelegate(activity);
        if (activityDelegate != null) {
            activityDelegate.onSaveInstanceState(outState);
        }
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        ActivityDelegate activityDelegate = fetchActivityDelegate(activity);
        if (activityDelegate != null) {
            activityDelegate.onDestroy();
            if (activity instanceof ActivityCache) {
                ((ActivityCache) activity).loadCache().clear();
            }
        }
    }

    /**
     * 默认全局配置实现，让Activity自动监听了网络状况。继承并使用[dora.BaseApplication]自动配置。
     */
    private static class DefaultGlobalConfig implements GlobalConfig {

        @Override
        public void injectApplicationLifecycle(Context context, List<ApplicationLifecycleCallbacks> lifecycles) {
            //AppLifecycle 中的所有方法都会在基类 Application 的对应生命周期中被调用, 所以在对应的方法中可以扩展一些自己需要的逻辑
            //可以根据不同的逻辑添加多个实现类
            lifecycles.add(new AppLifecycle());
        }

        @Override
        public void injectActivityLifecycle(Context context, List<Application.ActivityLifecycleCallbacks> lifecycles) {
            //ActivityLifecycleCallbacks 中的所有方法都会在 Activity (包括三方库) 的对应生命周期中被调用, 所以在对应的方法中可以扩展一些自己需要的逻辑
            //可以根据不同的逻辑添加多个实现类
            lifecycles.add(new ActivityLifecycle());
        }

        @Override
        public void injectFragmentLifecycle(Context context, List<FragmentManager.FragmentLifecycleCallbacks> lifecycles) {
            //FragmentLifecycleCallbacks 中的所有方法都会在 Fragment (包括三方库) 的对应生命周期中被调用, 所以在对应的方法中可以扩展一些自己需要的逻辑
            //可以根据不同的逻辑添加多个实现类
            lifecycles.add(new FragmentLifecycle());
        }
    }


    private void registerFragmentCallbacks(Activity activity) {
        if (activity instanceof FragmentActivity) {
            FragmentManager fragmentManager = ((FragmentActivity) activity).getSupportFragmentManager();
            List<Fragment> fragments = fragmentManager.getFragments();
            if (fragments.size() > 0) {
                List<GlobalConfig> mConfigs = ManifestParser.parse(activity);
                mConfigs.add(0, new DefaultGlobalConfig());
                for (GlobalConfig config : mConfigs) {
                    config.injectFragmentLifecycle(activity, mFragmentLifecycles);
                }
                fragmentManager.registerFragmentLifecycleCallbacks(new FragmentLifecycle(), true);
                //注册框架外部, 开发者扩展的 Fragment 生命周期逻辑
                for (FragmentManager.FragmentLifecycleCallbacks fragmentLifecycle : mFragmentLifecycles) {
                    ((FragmentActivity) activity).getSupportFragmentManager()
                            .registerFragmentLifecycleCallbacks(fragmentLifecycle, true);
                }
            }
        }
    }

    private ActivityDelegate fetchActivityDelegate(Activity activity) {
        ActivityDelegate activityDelegate = null;
        if (activity instanceof ActivityCache) {
            Cache<String, Object> cache = ((ActivityCache) activity).loadCache();
            activityDelegate = (ActivityDelegate) cache.get(ActivityDelegate.CACHE_KEY);
        }
        return activityDelegate;
    }
}