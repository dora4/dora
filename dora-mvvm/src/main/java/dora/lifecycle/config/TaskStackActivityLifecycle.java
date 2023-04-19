package dora.lifecycle.config;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import dora.util.TaskStackManager;

/**
 * 一个保存Activity堆栈情况的生命周期实现。
 */
public class TaskStackActivityLifecycle implements Application.ActivityLifecycleCallbacks {

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        TaskStackManager.getInstance().pushTask(activity);
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
        TaskStackManager.getInstance().popTaskWithChecking(activity.getClass());
    }
}
