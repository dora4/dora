package dora.util;

import android.content.Context;

public final class CacheUtils {

    private CacheUtils() {
    }

    public static String getCacheSize() {
        long cacheSize = IoUtils.getFolderTotalSize(GlobalContext.get().getCacheDir());
        if (IoUtils.checkMediaMounted()) {
            cacheSize += IoUtils.getFolderTotalSize(GlobalContext.get().getExternalCacheDir());
        }
        return IoUtils.formatFileSize(cacheSize);
    }

    public static void clearAllCaches() {
        IoUtils.delete(GlobalContext.get().getCacheDir());
        if (IoUtils.checkMediaMounted()) {
            IoUtils.delete(GlobalContext.get().getExternalCacheDir());
        }
    }
}