package dora.util;

import android.content.Context;

import java.util.Set;

import dora.Cache;
import dora.CacheType;
import dora.LruCache;

/**
 * 内存缓存工具。
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
     * 添加缓存，如果name重复则会失败。
     *
     * @param name
     * @param cache
     */
    private void setCacheToMemory(String name, Object cache) {
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

    /**
     * 推荐使用这个方法而不是{@link #setCacheToMemory(String, Object)}，{@link #updateCacheAtMemory}
     * 这个方法能保证更新成功。
     *
     * @param name
     * @param cache
     */
    public void updateCacheAtMemory(String name, Object cache) {
        removeCacheAtMemory(name);
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

    public static KVUtils getInstance() {
        if (mInstance == null) {
            throw new IllegalStateException("dora.BaseApplication未被使用");
        }
        return mInstance;
    }

    private static KVUtils getInstance(Context context) {
        if (mInstance == null) {
            synchronized (KVUtils.class) {
                if (mInstance == null)
                    mInstance = new KVUtils(context);
            }
        }
        return mInstance;
    }
}
