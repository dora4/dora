package dora;

import androidx.annotation.NonNull;

import dora.cache.annotation.Repository;
import dora.util.NetworkUtils;

/**
 * 数据仓库，扩展它来支持数据的三级缓存，即从云端服务器的数据库、手机本地数据库和手机内存中读取需要的数据，以支持用户
 * 手机在断网情况下也能显示以前的数据。
 *
 * 有以下缓存策略：
 * 仅数据库，{@link DataSource.CacheStrategy#STRATEGY_DATABASE_ONLY}，断网的情况下仅从数据库加载数据一次
 * 仅内存，{@link DataSource.CacheStrategy#STRATEGY_MEMORY_ONLY}，断网的情况下仅从内存加载数据一次
 * 数据库优先，{@link DataSource.CacheStrategy#STRATEGY_DATABASE_FIRST}，断网的情况下先从数据库加载一次数据，如果没有获取到，再从内存获取一次数据
 * 内存优先，{@link DataSource.CacheStrategy#STRATEGY_MEMORY_FIRST}，断网的情况下先从内存加载一次数据，如果没有获取到，再从数据库获取一次数据
 */
public abstract class BaseRepository {

    protected int mCacheStrategy = DataSource.CacheStrategy.STRATEGY_DATABASE_ONLY;

    /**
     * app冷启动的时候是否将数据库的数据加载到内存。
     */
    protected boolean mCacheLoadedInLaunchTime;

    /**
     * 请求网络数据的是否预加载缓存。
     */
    protected boolean mPreLoadBeforeRequestNetwork;

    protected String mLoadDataMethodName;

    protected String mCacheName;

    {
        //有配置注解以注解为准
        Repository repository = getClass().getAnnotation(Repository.class);
        if (repository != null) {
            mCacheStrategy = repository.cacheStrategy();
            mCacheLoadedInLaunchTime = repository.isCacheLoadedInLaunchTime();
            mPreLoadBeforeRequestNetwork = repository.isPreLoadBeforeRequestNetwork();
        }
    }

    protected boolean selectData(@NonNull DataSource ds) {
        if (isNetworkAvailable()) {
            if (mPreLoadBeforeRequestNetwork) {
                if (mCacheStrategy == DataSource.CacheStrategy.STRATEGY_MEMORY_FIRST
                        || mCacheStrategy == DataSource.CacheStrategy.STRATEGY_DATABASE_FIRST) {
                    if (mCacheLoadedInLaunchTime) {
                        ds.loadFromCache(DataSource.CacheType.MEMORY);
                    } else {
                        ds.loadFromCache(DataSource.CacheType.DATABASE);
                    }
                }
                if (mCacheStrategy == DataSource.CacheStrategy.STRATEGY_MEMORY_ONLY) {
                    ds.loadFromCache(DataSource.CacheType.MEMORY);
                }
                if (mCacheStrategy == DataSource.CacheStrategy.STRATEGY_DATABASE_ONLY) {
                    ds.loadFromCache(DataSource.CacheType.DATABASE);
                }
            }
            try {
                ds.loadFromNetwork();
                return true;
            } catch (Exception e) {
                return false;
            }
        } else {
            if (mCacheStrategy == DataSource.CacheStrategy.STRATEGY_DATABASE_ONLY ||
                mCacheStrategy == DataSource.CacheStrategy.STRATEGY_DATABASE_FIRST) {
                boolean isLoaded = ds.loadFromCache(DataSource.CacheType.DATABASE);
                if (!isLoaded && mCacheStrategy == DataSource.CacheStrategy.STRATEGY_DATABASE_FIRST) {
                    isLoaded = ds.loadFromCache(DataSource.CacheType.MEMORY);
                }
                return isLoaded;
            } else if (mCacheStrategy == DataSource.CacheStrategy.STRATEGY_MEMORY_ONLY ||
                mCacheStrategy == DataSource.CacheStrategy.STRATEGY_MEMORY_FIRST) {
                boolean isLoaded = ds.loadFromCache(DataSource.CacheType.MEMORY);
                if (!isLoaded && mCacheStrategy == DataSource.CacheStrategy.STRATEGY_MEMORY_FIRST) {
                    isLoaded = ds.loadFromCache(DataSource.CacheType.DATABASE);
                }
                return isLoaded;
            } else {
                return false;
            }
        }
    }

    public boolean isCacheLoadedInLaunchTime() {
        return mCacheLoadedInLaunchTime;
    }

    public boolean isPreLoadBeforeRequestNetwork() {
        return mPreLoadBeforeRequestNetwork;
    }

    public boolean hasMemoryCacheStrategy() {
        return mCacheStrategy == DataSource.CacheStrategy.STRATEGY_MEMORY_ONLY
                || mCacheStrategy == DataSource.CacheStrategy.STRATEGY_MEMORY_FIRST;
    }

    public int getCacheStrategy() {
        return mCacheStrategy;
    }

    protected boolean isNetworkAvailable() {
        return NetworkUtils.checkNetwork();
    }

    public String getLoadDataMethodName() {
        return mLoadDataMethodName;
    }

    public interface DataSource {

        enum CacheType {
            DATABASE,
            MEMORY
        }

        /**
         * 这里的缓存是手机系统数据库数据存储和内存缓存的统称。
         */
        interface CacheStrategy {
            int STRATEGY_DATABASE_ONLY = 0;
            int STRATEGY_MEMORY_ONLY = 1;
            int STRATEGY_DATABASE_FIRST = 2;
            int STRATEGY_MEMORY_FIRST = 3;
        }

        boolean loadFromCache(CacheType type);
        void loadFromNetwork() throws Exception;
    }

    public String getCacheName() {
        return mCacheName;
    }
}
