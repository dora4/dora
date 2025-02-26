package dora.security;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import android.os.Debug;
import java.net.Socket;
import java.lang.reflect.Method;
import java.io.File;

public class AntiDebug {

    public static boolean isDebuggerAttached() {
        return isPrepareForDebugging() ||
                isDebugging() ||
                isTracerPidSet() ||
                isJdwpActive() ||
                detectDebuggerByException() ||
                detectDebuggerByStackTrace();
    }

    private static boolean isPrepareForDebugging() {
        return Debug.waitingForDebugger();
    }

    private static boolean isDebugging() {
        return Debug.isDebuggerConnected();
    }

    private static boolean isJdwpActive() {
        File jdwpDir = new File("/proc/net/tcp");
        return jdwpDir.exists();
    }

    private static boolean isTracerPidSet() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("/proc/self/status"));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("TracerPid:")) {
                    int tracerPid = Integer.parseInt(line.split("\\s+")[1]);
                    reader.close();
                    return tracerPid > 0;
                }
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private static boolean detectDebuggerByException() {
        try {
            throw new Exception("Test Exception");
        } catch (Exception e) {
            for (StackTraceElement element : e.getStackTrace()) {
                String className = element.getClassName();
                String methodName = element.getMethodName();

                if (className.contains("com.android.tools") ||
                        className.contains("android.ddm") ||
                        className.contains("dalvik.system.VMDebug") ||
                        methodName.contains("debug") ||
                        methodName.contains("invoke")) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean detectDebuggerByStackTrace() {
        for (Thread thread : Thread.getAllStackTraces().keySet()) {
            for (StackTraceElement element : Thread.getAllStackTraces().get(thread)) {
                String className = element.getClassName();
                if (className.contains("com.android.tools") ||
                        className.contains("android.ddm") ||
                        className.contains("dalvik.system.VMDebug")) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isDebuggable() {
        try {
            Class<?> c = Class.forName("android.os.SystemProperties");
            Method get = c.getMethod("get", String.class);
            String debugFlag = (String) get.invoke(null, "ro.debuggable");
            return "1".equals(debugFlag);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean isAdbEnabled() {
        try {
            Socket socket = new Socket("127.0.0.1", 5555);
            socket.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
