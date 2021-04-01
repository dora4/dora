package dora.cache.repository;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import dora.cache.data.IDataFetcher;
import dora.cache.data.IListDataFetcher;
import dora.cache.data.page.IDataPager;
import dora.db.OrmTable;
import dora.http.DoraCallback;
import dora.http.DoraListCallback;
import dora.log.Logger;
import dora.util.NetworkUtils;

import java.util.List;

/**
 * 数据仓库，扩展它来支持数据的三级缓存，即从云端服务器的数据库、手机本地数据库和手机内存中读取需要的数据，以支持用户
 * 手机在断网情况下也能显示以前的数据。一个{@link BaseRepository}要么用于非集合数据，要么用于集合数据。如果要用于
 * 非集合数据，请配置{@link Repository}注解将{@link #mListData}的值设置为false。
 */
public abstract class BaseRepository<T extends OrmTable> implements IDataFetcher<T>, IListDataFetcher<T> {

    /**
     * 缓存策略。
     */
    protected int mCacheStrategy = DataSource.CacheStrategy.NO_CACHE;

    protected static final String TAG = "BaseRepository";

    /**
     * 非集合数据获取接口。
     */
    protected IDataFetcher<T> mDataFetcher;

    /**
     * 集合数据获取接口。
     */
    protected IListDataFetcher<T> mListDataFetcher;

    /**
     * true代表用于集合数据，false用于非集合数据。
     */
    protected boolean mListData = true;

    protected Context mContext;

    protected BaseRepository(Context context) {
        this.mContext = context;
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

    public Context getContext() {
        return mContext;
    }

    public boolean isListData() {
        return mListData;
    }

    public void setDataFetcher(IDataFetcher<T> fetcher) {
        this.mDataFetcher = fetcher;
    }

    public void setListDataFetcher(IListDataFetcher<T> fetcher) {
        this.mListDataFetcher = fetcher;
    }

    /**
     * 是否在网络加载数据失败的时候清空数据。
     *
     * @return
     */
    protected boolean isClearDataOnNetworkError() {
        return false;
    }

    /**
     * 安装默认的非集合数据抓取。
     *
     * @return
     */
    protected abstract IDataFetcher<T> installDataFetcher();

    /**
     * 安装默认的集合数据抓取。
     *
     * @return
     */
    protected abstract IListDataFetcher<T> installListDataFetcher();

    @Override
    public DoraCallback<T> callback() {
        return new DoraCallback<T>() {
            @Override
            public void onSuccess(T data) {
                Logger.dtag(TAG, data.toString());
            }

            @Override
            public void onFailure(int code, String msg) {
                Logger.dtag(TAG, code+":"+msg);
            }
        };
    }

    @Override
    public DoraListCallback<T> listCallback() {
        return new DoraListCallback<T>() {
            @Override
            public void onSuccess(List<T> data) {
                for (T t : data) {
                    Logger.dtag(TAG, t.toString());
                }
            }

            @Override
            public void onFailure(int code, String msg) {
                Logger.dtag(TAG, code+":"+msg);
            }
        };
    }

    /**
     * 非集合数据的API接口调用。
     *
     * @param callback
     */
    protected void onLoadFromNetwork(DoraCallback<T> callback) {
    }

    /**
     * 集合数据的API接口调用。
     *
     * @param callback
     */
    protected void onLoadFromNetwork(DoraListCallback<T> callback) {
    }

    /**
     * 从三级缓存仓库选择数据。
     *
     * @param ds 数据的来源
     * @return 数据是否获取成功
     */
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
        } else if (mCacheStrategy == DataSource.CacheStrategy.DATABASE_CACHE_NO_NETWORK) {
            boolean isLoaded = false;
            if (!isNetworkAvailable()) {
                isLoaded = ds.loadFromCache(DataSource.CacheType.DATABASE);
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

    /**
     * 数据的来源。
     */
    public interface DataSource {

        public enum Type {

            /**
             * 数据来源于网络服务器。
             */
            NETWORK,

            /**
             * 数据来源于缓存。
             */
            CACHE;
        }


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

            /**
             * 和{@link #DATABASE_CACHE}的不同之处在于，只有在没网的情况下才会加载数据库的缓存数据。
             */
            int DATABASE_CACHE_NO_NETWORK = 3;
        }

        /**
         * 从缓存中加载数据。
         *
         * @param type
         * @return
         */
        boolean loadFromCache(CacheType type);

        /**
         * 从服务器/网络加载数据。
         */
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

    @Override
    public IDataPager<T> getPager() {
        if (mListDataFetcher == null && mListData) {
            throw new RuntimeException("请先重写installListDataFetcher");
        }
        return mListDataFetcher.getPager();
    }

    /**
     * 检测网络是否可用。
     *
     * @return
     */
    protected boolean isNetworkAvailable() {
        return NetworkUtils.checkNetwork(mContext);
    }
}
