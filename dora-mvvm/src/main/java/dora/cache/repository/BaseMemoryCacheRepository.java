package dora.cache.repository;

import android.content.Context;
import androidx.lifecycle.LiveData;
import dora.cache.data.DataFetcher;
import dora.cache.data.ListDataFetcher;
import dora.db.OrmTable;
import dora.db.builder.WhereBuilder;
import dora.db.dao.DaoFactory;
import dora.db.dao.OrmDao;
import dora.http.DoraCallback;
import dora.http.DoraListCallback;
import dora.util.KeyValueUtils;

import java.util.List;

public abstract class BaseMemoryCacheRepository<T extends OrmTable> extends BaseRepository<T> {

    private OrmDao<T> mDao;

    public BaseMemoryCacheRepository(Context context, Class<T> clazz) {
        super(context);
        mDao = DaoFactory.getDao(clazz);
        mCacheStrategy = DataSource.CacheStrategy.MEMORY_CACHE;
    }

    public OrmDao<T> getDao() {
        return mDao;
    }

    /**
     * 根据查询条件进行初步的过滤从数据库加载的数据，过滤不完全则再调用onInterceptData。
     *
     * @return
     */
    protected WhereBuilder where() {
        return WhereBuilder.create();
    }

    /**
     * 在冷启动时调用，从数据库将数据加载到内存。
     */
    public abstract Object loadData();

    @Override
    protected DataFetcher<T> installDataFetcher() {
        return new DataFetcher<T>() {
            @Override
            public LiveData<T> getData() {
                selectData(new DataSource() {
                    @Override
                    public boolean loadFromCache(CacheType type) {
                        try {
                            if (type == CacheType.MEMORY) {
                                T model = (T) KeyValueUtils.getInstance().getCacheFromMemory(getCacheName());
                                onInterceptData(Type.CACHE, model);
                                mLiveData.setValue(model);
                            } else if (type == CacheType.DATABASE) {
                                T model = mDao.selectOne(where());
                                onInterceptData(Type.CACHE, model);
                                mLiveData.setValue(model);
                                KeyValueUtils.getInstance().updateCacheAtMemory(getCacheName(), model);
                            }
                        } catch (Exception e) {
                            return false;
                        }
                        return true;
                    }

                    @Override
                    public void loadFromNetwork() {
                        onLoadFromNetwork(callback());
                    }
                });
                return mLiveData;
            }

            @Override
            public DoraCallback<T> callback() {
                return new DoraCallback<T>() {

                    @Override
                    public void onSuccess(T data) {
                        onInterceptNetworkData(data);
                        KeyValueUtils.getInstance().updateCacheAtMemory(getCacheName(), data);
                        mDao.delete(where());
                        mDao.insert(data);
                        mLiveData.setValue(data);
                    }

                    @Override
                    public void onFailure(int code, String msg) {
                        if (isClearDataOnNetworkError()) {
                            mLiveData.setValue(null);
                            KeyValueUtils.getInstance().removeCacheAtMemory(getCacheName());
                            mDao.delete(where());
                        }
                    }

                    @Override
                    protected void onInterceptNetworkData(T data) {
                        onInterceptData(DataSource.Type.NETWORK, data);
                    }
                };
            }
        };
    }

    public abstract String getCacheName();

    @Override
    protected ListDataFetcher<T> installListDataFetcher() {
        return new ListDataFetcher<T>() {

            @Override
            public LiveData<List<T>> getListData() {
                selectData(new DataSource() {
                    @Override
                    public boolean loadFromCache(CacheType type) {
                        try {
                            if (type == CacheType.MEMORY) {
                                List<T> models = (List<T>) KeyValueUtils.getInstance().getCacheFromMemory(getCacheName());
                                onInterceptData(Type.CACHE, models);
                                mLiveData.setValue(models);
                            } else if (type == CacheType.DATABASE) {
                                List<T> models = mDao.select(where());
                                onInterceptData(Type.CACHE, models);
                                mLiveData.setValue(models);
                                KeyValueUtils.getInstance().updateCacheAtMemory(getCacheName(), models);
                            }
                        } catch (Exception e) {
                            return false;
                        }
                        return true;
                    }

                    @Override
                    public void loadFromNetwork() {
                        onLoadFromNetwork(listCallback());
                    }

                });
                return mLiveData;
            }

            @Override
            public DoraListCallback<T> listCallback() {
                return new DoraListCallback<T>() {
                    @Override
                    public void onSuccess(List<T> data) {
                        onInterceptNetworkData(data);
                        KeyValueUtils.getInstance().updateCacheAtMemory(getCacheName(), data);
                        mDao.delete(where());
                        mDao.insert(data);
                        mLiveData.setValue(data);
                    }

                    @Override
                    public void onFailure(int code, String msg) {
                        if (isClearDataOnNetworkError()) {
                            mDao.delete(where());
                            mLiveData.setValue(null);
                            KeyValueUtils.getInstance().removeCacheAtMemory(getCacheName());
                        }
                    }

                    @Override
                    protected void onInterceptNetworkData(List<T> data) {
                        onInterceptData(DataSource.Type.NETWORK, data);
                    }
                };
            }
        };
    }

    protected void onInterceptData(DataSource.Type type, T data) {
    }

    protected void onInterceptData(DataSource.Type type, List<T> data) {
    }
}
