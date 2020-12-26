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
    public static final String TAG = "dora-log";

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

    public static void i(String msg) {
        itag(TAG, msg);
    }

    public static void itag(String tag, String msg) {
        if (DEBUG) {
            Log.i(tag, msg);
        }
    }

    public static void iformat(String tag, String format, String... values) {
        itag(tag, String.format(format, values));
    }

    public static void e(String msg) {
        etag(TAG, msg);
    }

    public static void etag(String tag, String msg) {
        if (DEBUG) {
            Log.e(tag, msg);
        }
    }

    public static void eformat(String tag, String format, String... values) {
        etag(tag, String.format(format, values));
    }

    public static void d(String msg) {
        dtag(TAG, msg);
    }

    public static void dtag(String tag, String msg) {
        if (DEBUG) {
            Log.d(tag, msg);
        }
    }

    public static void dformat(String tag, String format, String... values) {
        dtag(tag, String.format(format, values));
    }

    public static void w(String msg) {
        wtag(TAG, msg);
    }

    public static void wtag(String tag, String msg) {
        if (DEBUG) {
            Log.w(tag, msg);
        }
    }

    public static void wformat(String tag, String format, String... values) {
        wtag(tag, String.format(format, values));
    }

    public static void v(String msg) {
        vtag(TAG, msg);
    }

    public static void vtag(String tag, String msg) {
        if (DEBUG) {
            Log.v(tag, msg);
        }
    }

    public static void vformat(String tag, String format, String... values) {
        vtag(tag, String.format(format, values));
    }

    // </editor-folder>
}