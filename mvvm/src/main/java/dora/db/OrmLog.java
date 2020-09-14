package dora.db;

import android.util.Log;

public class OrmLog {

    private static boolean ORM_DEBUG = true;
    private static String TAG = "dora-db";

    public static void setDebugMode(boolean debugMode) {
        ORM_DEBUG = debugMode;
    }

    public static void i(String msg) {
        if (ORM_DEBUG) {
            Log.i(TAG, msg);
        }
    }

    public static void w(String msg) {
        if (ORM_DEBUG) {
            Log.w(TAG, msg);
        }
    }

    public static void d(String msg) {
        if (ORM_DEBUG) {
            Log.d(TAG, msg);
        }
    }

    public static void e(String msg) {
        if (ORM_DEBUG) {
            Log.e(TAG, msg);
        }
    }

    public static void v(String msg) {
        if (ORM_DEBUG) {
            Log.v(TAG, msg);
        }
    }
}
