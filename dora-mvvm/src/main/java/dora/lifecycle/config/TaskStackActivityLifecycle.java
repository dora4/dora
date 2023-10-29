package dora.lifecycle.config;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import androidx.annotation.NonNull;

import dora.util.TaskStackManager;

/**
 * A lifecycle implementation that saves the state of the activity stack.
 * 简体中文：一个保存Activity堆栈情况的生命周期实现。
 */
public class TaskStackActivityLifecycle implements Application.ActivityLifecycleCallbacks {

    @Override
    public void onActivityCreated(@NonNull Activity activity, Bundle savedInstanceState) {
        TaskStackManager.getInstance().pushTask(activity);
    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {
    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {
    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {
    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {
    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        TaskStackManager.getInstance().popTaskWithChecking(activity.getClass());
    }
}
