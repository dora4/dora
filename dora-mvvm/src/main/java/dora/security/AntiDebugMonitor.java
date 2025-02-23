package dora.security;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Process;

public class AntiDebugMonitor {

    private static final long CHECK_INTERVAL = 3000;
    private static Handler handler;

    public static void startMonitoring() {
        HandlerThread thread = new HandlerThread("AntiDebugMonitor");
        thread.start();
        handler = new Handler(thread.getLooper());
        handler.post(checkDebuggerRunnable);
    }

    private static final Runnable checkDebuggerRunnable = new Runnable() {
        @Override
        public void run() {
            if (AntiDebug.isDebuggerAttached()) {
                killProcess();
            } else {
                handler.postDelayed(this, CHECK_INTERVAL);
            }
        }
    };

    private static void killProcess() {
        Process.killProcess(Process.myPid());
        System.exit(1);
    }
}
