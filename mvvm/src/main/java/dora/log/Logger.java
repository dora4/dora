package dora.log;

import android.util.Log;

/**
 * A system that controls log output globally. When flag is closed, you can't output logs anywhere.
 * Instead, you can output logs anywhere.<note>The log system is opened by default.</note>
 */
public class Logger {

    /**
     * The default log output tag.
     */
    private static final String TAG = "dora";

    // <editor-folder desc="日志控制">

    /**
     * The flag that represents the log system is opened or closed, default is opened.
     */
    private static boolean DEBUG = true;

    public static void close() {
        DEBUG = false;
    }

    public static void open() {
        DEBUG = true;
    }

    public static boolean isOpened() {
        return DEBUG;
    }

    public static boolean isClosed() {
        return !DEBUG;
    }

    // </editor-folder>

    // <editor-folder desc="日志输出">

    public static void info(String msg) {
        infoWithTag(TAG, msg);
    }

    public static void i(String msg) {
        infoWithTag(TAG, msg);
    }

    public static void infoWithTag(String tag, String msg) {
        if (DEBUG) {
            Log.i(tag, msg);
        }
    }

    public static void iwt(String tag, String msg) {
        if (DEBUG) {
            Log.i(tag, msg);
        }
    }

    public static void info(String format, String... values) {
        infoWithTag(TAG, format, values);
    }

    public static void i(String format, String... values) {
        infoWithTag(TAG, format, values);
    }

    public static void infoWithTag(String tag, String format, String... values) {
        infoWithTag(tag, String.format(format, new Object[]{values}));
    }

    public static void iwt(String tag, String format, String... values) {
        infoWithTag(tag, String.format(format, new Object[]{values}));
    }

    public static void error(String msg) {
        errorWithTag(TAG, msg);
    }

    public static void e(String msg) {
        errorWithTag(TAG, msg);
    }

    public static void errorWithTag(String tag, String msg) {
        if (DEBUG) {
            Log.e(tag, msg);
        }
    }

    public static void ewt(String tag, String msg) {
        if (DEBUG) {
            Log.e(tag, msg);
        }
    }

    public static void error(String format, String... values) {
        errorWithTag(TAG, format, values);
    }

    public static void e(String format, String... values) {
        errorWithTag(TAG, format, values);
    }

    public static void errorWithTag(String tag, String format, String... values) {
        errorWithTag(tag, String.format(format, new Object[]{values}));
    }

    public static void ewt(String tag, String format, String... values) {
        errorWithTag(tag, String.format(format, new Object[]{values}));
    }

    public static void debug(String msg) {
        debugWithTag(TAG, msg);
    }

    public static void d(String msg) {
        debugWithTag(TAG, msg);
    }

    public static void debugWithTag(String tag, String msg) {
        if (DEBUG) {
            Log.d(tag, msg);
        }
    }

    public static void dwt(String tag, String msg) {
        if (DEBUG) {
            Log.d(tag, msg);
        }
    }

    public static void debug(String format, String... values) {
        debugWithTag(TAG, format, values);
    }

    public static void d(String format, String... values) {
        debugWithTag(TAG, format, values);
    }

    public static void debugWithTag(String tag, String format, String... values) {
        debugWithTag(tag, String.format(format, new Object[]{values}));
    }

    public static void dwt(String tag, String format, String... values) {
        debugWithTag(tag, String.format(format, new Object[]{values}));
    }

    public static void warn(String msg) {
        warnWithTag(TAG, msg);
    }

    public static void w(String msg) {
        warnWithTag(TAG, msg);
    }

    public static void warnWithTag(String tag, String msg) {
        if (DEBUG) {
            Log.w(tag, msg);
        }
    }

    public static void wwt(String tag, String msg) {
        if (DEBUG) {
            Log.w(tag, msg);
        }
    }

    public static void warn(String format, String... values) {
        warnWithTag(TAG, format, values);
    }

    public static void w(String format, String... values) {
        warnWithTag(TAG, format, values);
    }

    public static void warnWithTag(String tag, String format, String... values) {
        warnWithTag(tag, String.format(format, new Object[]{values}));
    }

    public static void wwt(String tag, String format, String... values) {
        warnWithTag(tag, String.format(format, new Object[]{values}));
    }

    public static void verbose(String msg) {
        verboseWithTag(TAG, msg);
    }

    public static void v(String msg) {
        verboseWithTag(TAG, msg);
    }

    public static void verboseWithTag(String tag, String msg) {
        if (DEBUG) {
            Log.v(tag, msg);
        }
    }

    public static void vwt(String tag, String msg) {
        if (DEBUG) {
            Log.v(tag, msg);
        }
    }

    public static void verbose(String format, String... values) {
        verboseWithTag(TAG, format, values);
    }

    public static void v(String format, String... values) {
        verboseWithTag(TAG, format, values);
    }

    public static void verboseWithTag(String tag, String format, String... values) {
        verboseWithTag(tag, String.format(format, new Object[]{values}));
    }

    public static void vwt(String tag, String format, String... values) {
        verboseWithTag(tag, String.format(format, new Object[]{values}));
    }

    // </editor-folder>
}