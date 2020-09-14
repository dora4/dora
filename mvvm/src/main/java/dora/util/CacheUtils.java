package dora.util;

import android.content.Context;

public final class CacheUtils {

    private CacheUtils() {
    }

    public static String getCacheSize(Context context) {
        long cacheSize = IoUtils.getFolderTotalSize(context.getCacheDir());
        if (IoUtils.checkMediaMounted()) {
            cacheSize += IoUtils.getFolderTotalSize(context.getExternalCacheDir());
        }
        return IoUtils.formatFileSize(cacheSize);
    }

    public static void clearAllCaches(Context context) {
        IoUtils.delete(context.getCacheDir());
        if (IoUtils.checkMediaMounted()) {
            IoUtils.delete(context.getExternalCacheDir());
        }
    }
}