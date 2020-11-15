package dora.util;

import android.app.ActivityManager;
import android.content.Context;

import java.util.List;

public final class AppProcessUtils {

    private AppProcessUtils() {
    }

    public static boolean isRunInBackground(Context context) {
        ActivityManager activityManager = ServiceUtils.getActivityManager();
        List<ActivityManager.RunningAppProcessInfo> processes = activityManager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo processInfo : processes) {
            if (processInfo.processName.equals(context.getPackageName())) {
                return processInfo.importance != ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND;
            }
        }
        return false;
    }

    public static void killAllProcesses() {
        //杀死相关进程
        ActivityManager activityManager = ServiceUtils.getActivityManager();
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
