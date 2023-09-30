package dora.util;

import android.util.Log;

import androidx.annotation.StringRes;

/**
 * The logging tool defaults to filtering tags as [dora-log].
 * 简体中文：日志工具，默认过滤标签为[dora-log]。
 */
public final class LogUtils {

    /**
     * The default log output tag.
     * 简体中文：默认的日志输出标签。
     */
    public static final String TAG = "dora-log";

    public static void i(String msg) {
        itag(TAG, msg);
    }

    public static void i(@StringRes int resId) {
        itag(TAG, GlobalContext.get().getString(resId));
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

    public static void e(@StringRes int resId) {
        etag(TAG, GlobalContext.get().getString(resId));
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

    public static void d(@StringRes int resId) {
        dtag(TAG, GlobalContext.get().getString(resId));
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

    public static void w(@StringRes int resId) {
        wtag(TAG, GlobalContext.get().getString(resId));
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

    public static void v(@StringRes int resId) {
        vtag(TAG, GlobalContext.get().getString(resId));
    }
    public static void vtag(String tag, String msg) {
        Log.v(tag, msg);
    }

    public static void vformat(String tag, String format, String... values) {
        vtag(tag, String.format(format, values));
    }
}