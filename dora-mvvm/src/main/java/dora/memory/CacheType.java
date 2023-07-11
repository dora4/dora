package dora.memory;

import android.app.ActivityManager;
import android.content.Context;

/**
 * When constructing a {@link Cache}, use the types declared in {@link CacheType} to differentiate
 * between different modules and build different caching strategies for each module.
 * 简体中文：构建{@link Cache}时，使用{@link CacheType}中声明的类型，来区分不同的模块
 * 从而为不同的模块构建不同的缓存策略
 *
 * @see Cache.Factory#build(CacheType, Context)
 */
public interface CacheType {

    int ACTIVITY_CACHE_TYPE_ID = 0;
    int FRAGMENT_CACHE_TYPE_ID = 1;
    int REPOSITORY_CACHE_TYPE_ID = 2;

    CacheType ACTIVITY_CACHE = new CacheType() {
        private static final int MAX_SIZE = 80;
        private static final float MAX_SIZE_MULTIPLIER = 0.0008f;

        @Override
        public int getCacheTypeId() {
            return ACTIVITY_CACHE_TYPE_ID;
        }

        @Override
        public int calculateCacheSize(Context context) {
            ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            int targetMemoryCacheSize = (int) (activityManager.getMemoryClass() * MAX_SIZE_MULTIPLIER * 1024);
            if (targetMemoryCacheSize >= MAX_SIZE) {
                return MAX_SIZE;
            }
            return targetMemoryCacheSize;
        }
    };

    CacheType FRAGMENT_CACHE = new CacheType() {
        private static final int MAX_SIZE = 80;
        private static final float MAX_SIZE_MULTIPLIER = 0.0008f;

        @Override
        public int getCacheTypeId() {
            return FRAGMENT_CACHE_TYPE_ID;
        }

        @Override
        public int calculateCacheSize(Context context) {
            ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            int targetMemoryCacheSize = (int) (activityManager.getMemoryClass() * MAX_SIZE_MULTIPLIER * 1024);
            if (targetMemoryCacheSize >= MAX_SIZE) {
                return MAX_SIZE;
            }
            return targetMemoryCacheSize;
        }
    };

    CacheType REPOSITORY_CACHE = new CacheType() {
        private static final int MAX_SIZE = 80;
        private static final float MAX_SIZE_MULTIPLIER = 0.0008f;

        @Override
        public int getCacheTypeId() {
            return REPOSITORY_CACHE_TYPE_ID;
        }

        @Override
        public int calculateCacheSize(Context context) {
            ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            int targetMemoryCacheSize = (int) (activityManager.getMemoryClass() * MAX_SIZE_MULTIPLIER * 1024);
            if (targetMemoryCacheSize >= MAX_SIZE) {
                return MAX_SIZE;
            }
            return targetMemoryCacheSize;
        }
    };

    int getCacheTypeId();

    /**
     * Calculate the required cache size for the corresponding module.
     * 简体中文：计算对应模块需要的缓存大小。
     */
    int calculateCacheSize(Context context);
}
