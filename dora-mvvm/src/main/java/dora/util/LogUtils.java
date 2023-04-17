package dora.util;

import android.util.Log;

/**
 * 日志工具，默认过滤标签为[dora-log]。
 */
public class LogUtils {

    /**
     * The default log output tag.
     */
    public static final String TAG = "dora-log";

    // <editor-folder desc="日志输出">

    public static void i(String msg) {
        itag(TAG, msg);
    }

    public static void itag(String tag, String msg) {
        Log.i(tag, msg);
    }

    public static void iformat(String tag, String format, String... values) {
        itag(tag, String.format(format, values));
    }

    public static void e(String msg) {
        etag(TAG, msg);
    }

    public static void etag(String tag, String msg) {
        Log.e(tag, msg);
    }

    public static void eformat(String tag, String format, String... values) {
        etag(tag, String.format(format, values));
    }

    public static void d(String msg) {
        dtag(TAG, msg);
    }

    public static void dtag(String tag, String msg) {
        Log.d(tag, msg);
    }

    public static void dformat(String tag, String format, String... values) {
        dtag(tag, String.format(format, values));
    }

    public static void w(String msg) {
        wtag(TAG, msg);
    }

    public static void wtag(String tag, String msg) {
        Log.w(tag, msg);
    }

    public static void wformat(String tag, String format, String... values) {
        wtag(tag, String.format(format, values));
    }

    public static void v(String msg) {
        vtag(TAG, msg);
    }

    public static void vtag(String tag, String msg) {
        Log.v(tag, msg);
    }

    public static void vformat(String tag, String format, String... values) {
        vtag(tag, String.format(format, values));
    }

    // </editor-folder>
}