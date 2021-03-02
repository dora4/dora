package dora.cache.repository;

import androidx.lifecycle.LiveData;

import java.util.List;

import dora.cache.data.DataFetcher;
import dora.cache.data.ListDataFetcher;
import dora.db.OrmTable;
import dora.db.builder.WhereBuilder;
import dora.db.dao.DaoFactory;
import dora.db.dao.OrmDao;
import dora.http.DoraCallback;
import dora.http.DoraListCallback;
import dora.util.KeyValueUtils;

public abstract class BaseMemoryCacheRepository<T extends OrmTable> extends BaseRepository<T> {

    private OrmDao<T> mDao;

    public BaseMemoryCacheRepository(Class<T> clazz) {
        mDao = DaoFactory.getDao(clazz);
        mCacheStrategy = DataSource.CacheStrategy.MEMORY_CACHE;
    }

    public OrmDao<T> getDao() {
        return mDao;
    }

    protected WhereBuilder where() {
        return WhereBuilder.create();
    }

    /**
     * 在冷启动时调用，从数据库将数据加载到内存。
     */
    protected abstract Object loadData();

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
                                mLiveData.setValue(model);
                            } else if (type == CacheType.DATABASE) {
                                T model = mDao.selectOne(where());
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
                        mLiveData.setValue(null);
                        KeyValueUtils.getInstance().removeCacheAtMemory(getCacheName());
                        if (isClearDatabaseOnNetworkError()) {
                            mDao.delete(where());
                        }
                    }

                    @Override
                    protected void onInterceptNetworkData(T data) {
                        BaseMemoryCacheRepository.this.onInterceptNetworkData(data);
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
                                mLiveData.setValue(models);
                            } else if (type == CacheType.DATABASE) {
                                List<T> models = mDao.select(where());
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
                        mLiveData.setValue(null);
                        KeyValueUtils.getInstance().removeCacheAtMemory(getCacheName());
                        if (isClearDatabaseOnNetworkError()) {
                            mDao.delete(where());
                        }
                    }

                    @Override
                    protected void onInterceptNetworkData(List<T> data) {
                        BaseMemoryCacheRepository.this.onInterceptNetworkData(data);
                    }
                };
            }
        };
    }

    protected void onInterceptNetworkData(T data) {
    }

    protected void onInterceptNetworkData(List<T> data) {
    }
}
