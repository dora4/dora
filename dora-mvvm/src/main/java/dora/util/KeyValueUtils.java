package dora.util;

import android.content.Context;

import java.util.Set;

import dora.cache.Cache;
import dora.cache.CacheType;
import dora.cache.LruCache;

/**
 * 内存缓存工具。
 */
public final class KeyValueUtils {

    private static KeyValueUtils mInstance;
    private Cache<String, Object> mCache;

    private KeyValueUtils(Context context) {
        mCache = new Cache.Factory() {

            @Override
            public Cache build(CacheType type, Context context) {
                return new LruCache<>(type.calculateCacheSize(context));
            }
        }.build(CacheType.REPOSITORY_CACHE, context);
    }

    public void setCacheToMemory(String name, Object cache) {
        mCache.put(name, cache);
    }

    public Object getCacheFromMemory(String name) {
        return mCache.get(name);
    }

    public void removeCacheAtMemory(String name) {
        if (mCache.containsKey(name)) {
            mCache.remove(name);
        }
    }

    public void updateCacheAtMemory(String name, Object cache) {
        if (mCache.containsKey(name)) {
            mCache.remove(name);
            mCache.put(name, cache);
        }
    }

    public String printCacheKeys() {
        Set<String> keys = mCache.keySet();
        if (keys.size() > 0) {
            return TextUtils.combineString(keys.iterator(), "\n");
        }
        return "no key found";
    }

    public static KeyValueUtils getInstance() {
        return getInstance(GlobalContext.get());
    }

    private static KeyValueUtils getInstance(Context context) {
        if (mInstance == null) {
            synchronized (KeyValueUtils.class) {
                if (mInstance == null)
                     mInstance = new KeyValueUtils(context);
            }
        }
        return mInstance;
    }
}
