package dora.security;

import android.os.Process;

import java.io.File;

/**
 * Monitor changes in /proc/self/maps to detect and prevent memory dumping.
 * 简体中文：监控 /proc/self/maps 的变动，用于检测并阻止内存 Dump 行为。
 */
public class AntiDumpMonitor {

    private static final long DEFAULT_CHECK_INTERVAL = 3000;
    private static long checkInterval = DEFAULT_CHECK_INTERVAL;

    /**
     * Start monitoring /proc/self/maps for suspicious modifications.
     * 简体中文：开始监控 /proc/self/maps 是否出现可疑修改。
     */
    public static void startMonitoring() {
        startMonitoring(DEFAULT_CHECK_INTERVAL);
    }

    /**
     * Start monitoring /proc/self/maps for suspicious modifications.
     * 简体中文：开始监控 /proc/self/maps 是否出现可疑修改。
     */
    public static void startMonitoring(long interval) {
        if (interval > 10000) {
            checkInterval = 10000;
        } else if (interval < 1000) {
            checkInterval = 1000;
        }
        Thread monitorThread = new Thread(AntiDumpMonitor::monitorMapsLoop, "AntiDumpMonitor");
        monitorThread.start();
    }

    /**
     * Continuous monitoring loop for /proc/self/maps.
     * 简体中文：循环监控 /proc/self/maps 的具体逻辑。
     */
    private static void monitorMapsLoop() {
        File maps = new File("/proc/self/maps");
        long lastLength = maps.length();
        long lastModified = maps.lastModified();

        while (true) {
            try {
                long currentLength = maps.length();
                long currentModified = maps.lastModified();

                // Detect unusual changes
                if (currentLength != lastLength || currentModified != lastModified) {
                    killProcess();
                }

                lastLength = currentLength;
                lastModified = currentModified;
                Thread.sleep(checkInterval);
            } catch (Exception ignored) {
            }
        }
    }

    /**
     * Kill the current process immediately.
     * 简体中文：立即终止当前进程。
     */
    private static void killProcess() {
        Process.killProcess(Process.myPid());
        System.exit(1);
    }
}
