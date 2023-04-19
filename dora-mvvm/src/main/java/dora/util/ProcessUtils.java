package dora.util;

import android.app.ActivityManager;
import android.content.Context;

import java.util.List;

/**
 * 进程管理相关工具。
 */
public final class ProcessUtils {

    private ProcessUtils() {
    }

    public static boolean isRunInBackground() {
        return isRunInBackground(GlobalContext.get());
    }

    public static boolean isRunInBackground(Context context) {
        ActivityManager activityManager = ServiceUtils.getActivityManager(context);
        List<ActivityManager.RunningAppProcessInfo> processes = activityManager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo processInfo : processes) {
            if (processInfo.processName.equals(context.getPackageName())) {
                return processInfo.importance != ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND;
            }
        }
        return false;
    }

    public static void killAllProcesses() {
        killAllProcesses(GlobalContext.get());
    }

    public static void killAllProcesses(Context context) {
        //杀死相关进程
        ActivityManager activityManager = ServiceUtils.getActivityManager(context);
        List<ActivityManager.RunningAppProcessInfo> processes = activityManager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo processInfo : processes) {
            if (processInfo.uid == android.os.Process.myUid() && processInfo.pid != android.os.Process.myPid()) {
                android.os.Process.killProcess(processInfo.pid);
            }
        }
        //杀死本进程
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }
}
