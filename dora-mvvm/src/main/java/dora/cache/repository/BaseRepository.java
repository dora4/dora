package dora.cache.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import java.util.List;

import dora.cache.data.IDataFetcher;
import dora.cache.data.IListDataFetcher;
import dora.db.OrmTable;
import dora.http.DoraCallback;
import dora.http.DoraListCallback;
import dora.log.Logger;
import dora.util.NetworkUtils;

/**
 * 数据仓库，扩展它来支持数据的三级缓存，即从云端服务器的数据库、手机本地数据库和手机内存中读取需要的数据，以支持用户
 * 手机在断网情况下也能显示以前的数据。
 */
abstract class BaseRepository<T extends OrmTable> implements IDataFetcher<T>, IListDataFetcher<T> {

    protected int mCacheStrategy = DataSource.CacheStrategy.NO_CACHE;

    protected IDataFetcher<T> mDataFetcher;

    protected IListDataFetcher<T> mListDataFetcher;

    protected boolean mListData;

    {
        Repository repository = getClass().getAnnotation(Repository.class);
        if (repository != null) {
            mCacheStrategy = repository.cacheStrategy();
            mListData = repository.isListData();
        }
        if (mListData) {
            mListDataFetcher = installListDataFetcher();
        } else {
            mDataFetcher = installDataFetcher();
        }
    }

    /**
     * 是否在网络加载数据失败的时候清空数据库的缓存。
     *
     * @return
     */
    protected boolean isClearDatabaseOnNetworkError() {
        return false;
    }

    protected IDataFetcher<T> installDataFetcher() {
        return mDataFetcher;
    }

    protected IListDataFetcher<T> installListDataFetcher() {
        return mListDataFetcher;
    }

    protected void onLoadFromNetwork(DoraCallback<T> callback) {
    }

    protected void onLoadFromNetwork(DoraListCallback<T> callback) {
    }

    protected boolean selectData(@NonNull DataSource ds) {
        if (mCacheStrategy == DataSource.CacheStrategy.NO_CACHE) {
            if (isNetworkAvailable()) {
                try {
                    ds.loadFromNetwork();
                    return true;
                } catch (Exception e) {
                    Logger.e(e.getMessage());
                    return false;
                }
            }
        } else if (mCacheStrategy == DataSource.CacheStrategy.DATABASE_CACHE) {
            boolean isLoaded = ds.loadFromCache(DataSource.CacheType.DATABASE);
            if (isNetworkAvailable()) {
                try {
                    ds.loadFromNetwork();
                    return true;
                } catch (Exception e) {
                    Logger.e(e.getMessage());
                    return false;
                }
            } else return isLoaded;
        } else if (mCacheStrategy == DataSource.CacheStrategy.MEMORY_CACHE) {
            boolean isLoaded = ds.loadFromCache(DataSource.CacheType.MEMORY);
            if (!isLoaded) {
                ds.loadFromCache(DataSource.CacheType.DATABASE);
            }
            if (isNetworkAvailable()) {
                try {
                    ds.loadFromNetwork();
                    return true;
                } catch (Exception e) {
                    Logger.e(e.getMessage());
                    return false;
                }
            } else return isLoaded;
        }
        return false;
    }

    public interface DataSource {

        enum CacheType {
            DATABASE,
            MEMORY
        }

        interface CacheStrategy {

            /**
             * 默认策略，不启用缓存。
             */
            int NO_CACHE = 0;

            /**
             * 数据库缓存，通常用于断网的情况，在打开界面前从数据库读取离线数据。
             */
            int DATABASE_CACHE = 1;

            /**
             * 内存缓存，通常用于需要在app冷启动的时候将数据库的数据先加载到内存，以后打开界面直接从内存中去拿数据。
             */
            int MEMORY_CACHE = 2;
        }

        boolean loadFromCache(CacheType type);
        void loadFromNetwork();
    }

    @Override
    public LiveData<T> getData() {
        if (mDataFetcher == null && !mListData) {
            throw new RuntimeException("请先重写installDataFetcher");
        }
        return mDataFetcher.getData();
    }

    @Override
    public LiveData<List<T>> getListData() {
        if (mListDataFetcher == null && mListData) {
            throw new RuntimeException("请先重写installListDataFetcher");
        }
        return mListDataFetcher.getListData();
    }

    protected boolean isNetworkAvailable() {
        return NetworkUtils.checkNetwork();
    }
}
