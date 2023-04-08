package dora.lifecycle.config;

import android.app.Application;
import android.content.Context;

import androidx.fragment.app.FragmentManager;

import java.util.List;

import dora.lifecycle.application.ApplicationLifecycleCallbacks;

/**
 * 用来配置自动保存Activity堆栈情况。
 */
public class TaskStackGlobalConfig implements GlobalConfig {

    @Override
    public void injectApplicationLifecycle(Context context, List<ApplicationLifecycleCallbacks> lifecycles) {
    }

    @Override
    public void injectActivityLifecycle(Context context, List<Application.ActivityLifecycleCallbacks> lifecycles) {
        lifecycles.add(new TaskStackActivityLifecycle());
    }

    @Override
    public void injectFragmentLifecycle(Context context, List<FragmentManager.FragmentLifecycleCallbacks> lifecycles) {
    }
}
