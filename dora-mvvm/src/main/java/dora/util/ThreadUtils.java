package dora.util;

import android.os.Looper;

/**
 * 线程相关工具。
 */
public final class ThreadUtils {

    private ThreadUtils() {
    }

    /**
     * 检测当前代码环境是否是主线程。
     */
    public static boolean isMainThread() {
        return Looper.getMainLooper() == Looper.myLooper();
    }
}
