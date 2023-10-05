package dora.lifecycle.activity;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import java.util.ArrayList;
import java.util.List;

import dora.lifecycle.fragment.FragmentLifecycle;
import dora.lifecycle.config.GlobalConfig;
import dora.lifecycle.application.ManifestParser;

public class ActivityLifecycle implements Application.ActivityLifecycleCallbacks {

    private List<FragmentManager.FragmentLifecycleCallbacks> mFragmentLifecycles = new ArrayList<>();

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        registerFragmentCallbacks(activity);
    }

    @Override
    public void onActivityStarted(Activity activity) {
    }

    @Override
    public void onActivityResumed(Activity activity) {
    }

    @Override
    public void onActivityPaused(Activity activity) {
    }

    @Override
    public void onActivityStopped(Activity activity) {
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
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
                // Registering external framework, developer-extended Fragment lifecycle logic.
                // 简体中文：注册框架外部, 开发者扩展的Fragment生命周期逻辑
                for (FragmentManager.FragmentLifecycleCallbacks fragmentLifecycle : mFragmentLifecycles) {
                    ((FragmentActivity) activity).getSupportFragmentManager()
                            .registerFragmentLifecycleCallbacks(fragmentLifecycle, true);
                }
            }
        }
    }
}