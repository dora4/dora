package dora.util;

import android.content.Context;

import dora.cache.Cache;
import dora.cache.CacheType;
import dora.cache.LruCache;

/**
 * 内存缓存工具。
 */
public final class KeyValueUtils {

    private static KeyValueUtils mInstance = new KeyValueUtils();
    private Cache<String, Object> mCache;

    private KeyValueUtils() {
        mCache = new Cache.Factory() {

            @Override
            public Cache build(CacheType type, Context context) {
                return new LruCache<>(type.calculateCacheSize(context));
            }
        }.build(CacheType.REPOSITORY_CACHE, GlobalContext.get());
    }

    public void setCacheToMemory(String name, Object cache) {
        mCache.put(name, cache);
    }

    public Object getCacheFromMemory(String name) {
        return mCache.get(name);
    }

    public static KeyValueUtils getInstance() {
        return mInstance;
    }
}
