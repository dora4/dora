package dora.security;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Process;
import android.util.Log;

public class AntiDebugMonitor {

    private static final long DEFAULT_CHECK_INTERVAL = 3000;
    private static long checkInterval = DEFAULT_CHECK_INTERVAL;
    private static Handler handler;

    /**
     * Start anti-debugging thread.
     * 简体中文：启动反调试线程。
     *
     * @see #startMonitoring(long) 
     */
    public static void startMonitoring() {
        startMonitoring(DEFAULT_CHECK_INTERVAL);
    }

    /**
     * Start anti-debugging thread.
     * 简体中文：启动反调试线程。
     *
     * @param interval 1000~10000 ms
     */
    public static void startMonitoring(long interval) {
        if (interval > 10000) {
            checkInterval = 10000;
        } else if (interval < 1000) {
            checkInterval = 1000;
        }
        HandlerThread thread = new HandlerThread("AntiDebugMonitor");
        thread.start();
        handler = new Handler(thread.getLooper());
        handler.post(checkDebuggerRunnable);
    }

    private static final Runnable checkDebuggerRunnable = new Runnable() {
        @Override
        public void run() {
            if (AntiDebug.isDebuggerAttached()) {
                Log.e("AntiDebugMonitor", "kill process");
                killProcess();
            } else {
                handler.postDelayed(this, checkInterval);
            }
        }
    };

    private static void killProcess() {
        Process.killProcess(Process.myPid());
        System.exit(1);
    }
}
