/*
 * Copyright (C) 2023 The Dora Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dora.util;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;

import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.Stack;

/**
 * Activity Stack Manager.
 * 简体中文：Activity堆栈管理器。
 */
public final class TaskStackManager {

    private static TaskStackManager sTaskStackManager;
    private Application mApplication;

    /**
     * Only a mirror used to record the activity created.
     * 简体中文：仅使用镜像来记录所创建的活动。
     */
    private final Stack<WeakReference<? extends Activity>>
            mActivityStacks = new Stack<>();

    private TaskStackManager() {
    }

    public static TaskStackManager getInstance() {
        if (sTaskStackManager == null) {
            synchronized (TaskStackManager.class) {
                if (sTaskStackManager == null) {
                    sTaskStackManager = new TaskStackManager();
                }
            }
        }
        return sTaskStackManager;
    }

    public TaskStackManager init(Application application) {
        this.mApplication = application;
        return sTaskStackManager;
    }

    /**
     * Save activity information to the stack.
     * 简体中文：保存activity信息到栈。
     */
    public void pushTask(Activity activity) {
        synchronized (TaskStackManager.class) {
            mActivityStacks.add(new WeakReference<>(activity));
        }
    }

    /**
     * Detect the destruction of the activity at the top of the stack.
     * 简体中文：检测销毁栈顶的activity。
     */
    public void popTaskWithChecking(Class<? extends Activity> activityClazz) {
        synchronized (TaskStackManager.class) {
            // Peek without removal
            // 只查看不移除
            WeakReference<? extends Activity> ref = mActivityStacks.peek();
            if (ref != null) {
                Activity activity = ref.get();
                if (activityClazz.isAssignableFrom(activity.getClass())) {
                    mActivityStacks.pop();
                }
            }
        }
    }

    /**
     * Destroy the activity at the top of the stack.
     * 简体中文：销毁栈顶的activity。
     */
    public void popTask() {
        synchronized (TaskStackManager.class) {
            WeakReference<? extends Activity> ref = mActivityStacks.peek(); //只查看不移除
            if (ref != null) {
                mActivityStacks.pop();
            }
        }
    }

    /**
     * Make the activity at the top of the stack open the specified activity.
     * 简体中文：让在栈顶的activity打开指定的activity。
     */
    public void startActivity(Class activityClass) {
        startActivity(new Intent(mApplication, activityClass));
    }

    /**
     * Start a new activity.
     * 简体中文：开启一个新的activity。
     */
    private void startActivity(Intent intent) {
        if (getTopActivity() == null) {
            //如果没有前台的activity就使用new_task模式启动activity
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mApplication.startActivity(intent);
            return;
        }
        getTopActivity().startActivity(intent);
    }

    /**
     * Get the top activity of the stack.
     * 简体中文：获取栈顶activity。
     */
    public Activity getTopActivity() {
        if (mActivityStacks.peek() != null) {
            return mActivityStacks.peek().get();
        }
        return null;
    }

    /**
     * Destroy the specified activity.
     * 简体中文：销毁指定activity。
     */
    public void finishActivity(Class<?> activityClazz) {
        synchronized (TaskStackManager.class) {
            Iterator<WeakReference<? extends Activity>> iterator = mActivityStacks.iterator();
            while (iterator.hasNext()) {
                Activity next = iterator.next().get();
                if (next.getClass().equals(activityClazz)) {
                    iterator.remove();
                    next.finish();
                }
            }
        }
    }

    /**
     * Destroy all activities of this app, leaving only the bottom activity in the stack,
     * usually the main interface.
     * 简体中文：销毁本app所有activity，只保留栈底的activity，通常主界面在栈底。
     */
    public void finishActivityUntilBottom() {
        synchronized (TaskStackManager.class) {
            int size = mActivityStacks.size();
            for (int i = size - 1; i > 0; i--) {
                WeakReference<? extends Activity> ref = mActivityStacks.get(i);
                if (ref != null) {
                    Activity activity = ref.get();
                    if (activity != null) {
                        activity.finish();
                    }
                }
            }
        }
    }

    /**
     * Destroy all activities of this app.
     * 简体中文：销毁本app所有activity。
     */
    public void finishAllActivities() {
        synchronized (TaskStackManager.class) {
            Iterator<WeakReference<? extends Activity>> iterator = mActivityStacks.iterator();
            while (iterator.hasNext()) {
                if (iterator.next() != null && iterator.next().get() != null) {
                    Activity next = iterator.next().get();
                    iterator.remove();
                    next.finish();
                }
            }
        }
    }

    /**
     * Completely kill this app, including all activities and its processes.
     * 简体中文：完全杀死本app，包括所有activity和其进程。
     */
    public void killAll(Context context) {
        synchronized (TaskStackManager.class) {
            finishAllActivities();
            ProcessUtils.killAllProcesses(context);
        }
    }
}
