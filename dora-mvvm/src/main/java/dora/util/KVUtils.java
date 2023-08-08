package dora.util;

import android.content.Context;

import java.util.Set;

import dora.memory.Cache;
import dora.memory.CacheType;
import dora.memory.LruCache;

/**
 * Key-value pair tool for in-memory caching.
 * 简体中文：键值对工具，用于内存缓存。
 */
public final class KVUtils {

    private static KVUtils mInstance;
    private Cache<String, Object> mCache;

    private KVUtils(Context context) {
        mCache = new Cache.Factory() {

            @Override
            public Cache build(CacheType type, Context context) {
                return new LruCache<>(type.calculateCacheSize(context));
            }
        }.build(CacheType.REPOSITORY_CACHE, context);
    }

    /**
     * Add caching, failure will occur if the name is duplicated.
     * 简体中文：添加缓存，如果name重复则会失败。
     */
    private void setCacheToMemory(String name, Object cache) {
        mCache.put(name, cache);
    }

    public Object getCacheFromMemory(String name) {
        return mCache.get(name);
    }

    public void removeCache(String name) {
        if (mCache.containsKey(name)) {
            mCache.remove(name);
        }
    }

    /**
     * I recommend using this method instead of {@link #setCacheToMemory(String, Object)} and
     * {@link #updateCache}.This method ensures a successful update.
     * 简体中文：推荐使用这个方法而不是{@link #setCacheToMemory(String, Object)}，{@link #updateCache}
     * 这个方法能保证更新成功。
     */
    public void updateCache(String name, Object cache) {
        removeCache(name);
        setCacheToMemory(name, cache);
    }

    public Set<String> cacheKeys() {
        return mCache.keySet();
    }

    public String printCacheKeys() {
        Set<String> keys = mCache.keySet();
        if (keys.size() > 0) {
            return TextUtils.combineString(keys.iterator(), "\n");
        }
        return "no key found";
    }

    public static KVUtils getInstance(Context context) {
        if (mInstance == null) {
            synchronized (KVUtils.class) {
                if (mInstance == null)
                    mInstance = new KVUtils(context);
            }
        }
        return mInstance;
    }
}
