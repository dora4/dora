package dora.util;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;

import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.Stack;

/**
 * Activity堆栈管理器。
 */
public final class TaskStackManager {

    private static TaskStackManager sTaskStackManager;
    private Application mApplication;

    /**
     * Only a mirror used to record the activity created.
     */
    protected Stack<WeakReference<? extends Activity>>
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

    /**
     * 依附到application。
     *
     * @param application
     * @return
     */
    public TaskStackManager init(Application application) {
        this.mApplication = application;
        return sTaskStackManager;
    }

    /**
     * 保存activity信息到栈。
     *
     * @param activity
     */
    public void pushTask(Activity activity) {
        synchronized (TaskStackManager.class) {
            mActivityStacks.add(new WeakReference<>(activity));
        }
    }

    /**
     * 检测销毁栈顶的activity。
     *
     * @param activityClazz
     */
    public void popTaskWithChecking(Class<? extends Activity> activityClazz) {
        synchronized (TaskStackManager.class) {
            WeakReference<? extends Activity> ref = mActivityStacks.peek(); //只查看不移除
            if (ref != null) {
                Activity activity = ref.get();
                if (activityClazz.isAssignableFrom(activity.getClass())) {
                    mActivityStacks.pop();
                }
            }
        }
    }

    /**
     * 销毁栈顶的activity。
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
     * 让在栈顶的activity ,打开指定的activity。
     *
     * @param activityClass
     */
    public void startActivity(Class activityClass) {
        startActivity(new Intent(mApplication, activityClass));
    }

    /**
     * 开启一个新的activity。
     *
     * @param intent
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
     * 获取栈顶activity。
     *
     * @return
     */
    public Activity getTopActivity() {
        if (mActivityStacks.peek() != null) {
            return mActivityStacks.peek().get();
        }
        return null;
    }

    /**
     * 销毁指定activity。
     *
     * @param activityClazz
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
     * 销毁本app所有activity，只保留栈底的activity，通常主界面一个在栈底。
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
     * 销毁本app所有activity。
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
     * 完全杀死本app，包括所有activity和其进程。
     */
    public void killAll(Context context) {
        synchronized (TaskStackManager.class) {
            finishAllActivities();
            ProcessUtils.killAllProcesses(context);
        }
    }
}
