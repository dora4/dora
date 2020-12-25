package dora;

import androidx.annotation.NonNull;

import dora.util.NetworkUtils;

/**
 * 数据仓库。
 */
public abstract class BaseRepository {

    protected boolean mCacheLoadedInLaunchTime;

    protected void selectData(@NonNull DataSource ds) {
        if (useCache()) {
            ds.loadFromCache();
        } else {
            ds.loadFromNetwork();
        }
    }

    public boolean isCacheLoadedInLaunchTime() {
        return mCacheLoadedInLaunchTime;
    }

    public boolean hasMemoryCacheStrategy() {
        return getCacheStrategy() == DataSource.CacheStrategy.STRATEGY_MEMORY_ONLY
                || getCacheStrategy() == DataSource.CacheStrategy.STRATEGY_MEMORY_FIRST;
    }

    public int getCacheStrategy() {
        return DataSource.CacheStrategy.STRATEGY_DATABASE_ONLY;
    }

    protected boolean useCache() {
        return !NetworkUtils.checkNetwork();
    }

    public interface DataSource {

        /**
         * 这里的缓存是手机系统数据库数据存储和内存缓存的统称。
         */
        interface CacheStrategy {
            int STRATEGY_DATABASE_ONLY = 0;
            int STRATEGY_MEMORY_ONLY = 1;
            int STRATEGY_DATABASE_FIRST = 2;
            int STRATEGY_MEMORY_FIRST = 3;
        }

        void loadFromCache();
        void loadFromNetwork();
    }
}
