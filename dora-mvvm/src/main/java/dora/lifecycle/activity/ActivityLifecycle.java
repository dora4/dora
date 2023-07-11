package dora.lifecycle.activity;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import java.util.ArrayList;
import java.util.List;

import dora.memory.DataLoader;
import dora.memory.Cache;
import dora.lifecycle.fragment.FragmentLifecycle;
import dora.lifecycle.config.GlobalConfig;
import dora.lifecycle.application.ManifestParser;

public class ActivityLifecycle implements Application.ActivityLifecycleCallbacks {

    private List<FragmentManager.FragmentLifecycleCallbacks> mFragmentLifecycles = new ArrayList<>();

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        if (activity instanceof DataLoader) {
            ActivityDelegate activityDelegate = fetchActivityDelegate(activity);
            if (activityDelegate == null) {
                Cache<String, Object> cache = ((DataLoader) activity).loadCache();
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
            if (activity instanceof DataLoader) {
                ((DataLoader) activity).loadCache().clear();
            }
        }
    }

    private void registerFragmentCallbacks(Activity activity) {
        if (activity instanceof FragmentActivity) {
            FragmentManager fragmentManager = ((FragmentActivity) activity).getSupportFragmentManager();
            List<Fragment> fragments = fragmentManager.getFragments();
            if (fragments.size() > 0) {
                List<GlobalConfig> mConfigs = ManifestParser.parse(activity);
                for (GlobalConfig config : mConfigs) {
                    config.injectFragmentLifecycle(activity, mFragmentLifecycles);
                }
                fragmentManager.registerFragmentLifecycleCallbacks(new FragmentLifecycle(), true);
                // 注册框架外部, 开发者扩展的 Fragment 生命周期逻辑
                for (FragmentManager.FragmentLifecycleCallbacks fragmentLifecycle : mFragmentLifecycles) {
                    ((FragmentActivity) activity).getSupportFragmentManager()
                            .registerFragmentLifecycleCallbacks(fragmentLifecycle, true);
                }
            }
        }
    }

    private ActivityDelegate fetchActivityDelegate(Activity activity) {
        ActivityDelegate activityDelegate = null;
        if (activity instanceof DataLoader) {
            Cache<String, Object> cache = ((DataLoader) activity).loadCache();
            activityDelegate = (ActivityDelegate) cache.get(ActivityDelegate.CACHE_KEY);
        }
        return activityDelegate;
    }
}