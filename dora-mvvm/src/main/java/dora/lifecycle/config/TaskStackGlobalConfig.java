package dora.lifecycle.config;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;

import java.util.List;

import dora.lifecycle.application.ApplicationLifecycleCallbacks;

/**
 * Used to configure automatic saving of the activity stack.
 * 简体中文：用来配置自动保存Activity堆栈情况。
 */
public class TaskStackGlobalConfig implements GlobalConfig {

    @Override
    public void injectApplicationLifecycle(@NonNull Context context, @NonNull List<ApplicationLifecycleCallbacks> lifecycles) {
    }

    @Override
    public void injectActivityLifecycle(@NonNull Context context, @NonNull List<Application.ActivityLifecycleCallbacks> lifecycles) {
        lifecycles.add(new TaskStackActivityLifecycle());
    }

    @Override
    public void injectFragmentLifecycle(@NonNull Context context, @NonNull List<FragmentManager.FragmentLifecycleCallbacks> lifecycles) {
    }
}
