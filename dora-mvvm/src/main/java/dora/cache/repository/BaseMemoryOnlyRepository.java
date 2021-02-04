package dora.cache.repository;

import androidx.lifecycle.LiveData;

import java.util.List;

import dora.cache.annotation.Repository;
import dora.cache.data.DefaultDataFetcher;
import dora.cache.data.ListDataFetcher;
import dora.db.OrmTable;
import dora.db.builder.WhereBuilder;
import dora.db.dao.DaoFactory;
import dora.db.dao.OrmDao;
import dora.http.DoraCallback;
import dora.util.KeyValueUtils;

@Repository(cacheStrategy = BaseRepository.DataSource.CacheStrategy.STRATEGY_MEMORY_ONLY)
public abstract class BaseMemoryOnlyRepository<T extends OrmTable> extends BaseRepository<T> {

    OrmDao<T> dao;

    public BaseMemoryOnlyRepository(Class<T> clazz) {
        dao = DaoFactory.getDao(clazz);
    }

    protected WhereBuilder where() {
        return WhereBuilder.create();
    }

    @Override
    protected DefaultDataFetcher<T> installDataFetcher() {
        return new DefaultDataFetcher<T>() {
            @Override
            public LiveData<T> getData() {
                selectData(new DataSource() {
                    @Override
                    public boolean loadFromCache(CacheType type) {
                        if (type == CacheType.MEMORY) {
                            T model = (T) KeyValueUtils.getInstance().getCacheFromMemory(getCacheName());
                            mLiveData.setValue(model);
                            return true;
                        }
                        return false;
                    }

                    @Override
                    public void loadFromNetwork() throws Exception {
                        onLoadFromNetwork(callback());
                    }
                });
                return mLiveData;
            }

            @Override
            public DoraCallback<?> callback() {
                return new DoraCallback<T>() {

                    @Override
                    public void onSuccess(T data) {
                        onInterceptNetworkData(data);
                        KeyValueUtils.getInstance().updateCacheAtMemory(getCacheName(), data);
                        mLiveData.setValue(data);
                    }

                    @Override
                    public void onFailure(int code, String msg) {
                        mLiveData.setValue(null);
                        KeyValueUtils.getInstance().removeCacheAtMemory(getCacheName());
                    }
                };
            }
        };
    }

    @Override
    protected ListDataFetcher installListDataFetcher() {
        return new ListDataFetcher() {

            @Override
            public LiveData<List<T>> getData() {
                selectData(new DataSource() {
                    @Override
                    public boolean loadFromCache(CacheType type) {
                        if (type == CacheType.MEMORY) {
                            List<T> models = (List<T>) KeyValueUtils.getInstance().getCacheFromMemory(getCacheName());
                            if (models.size() > 0) {
                                mLiveData.setValue(models);
                                return true;
                            } else {
                                return false;
                            }
                        }
                        return false;
                    }

                    @Override
                    public void loadFromNetwork() throws Exception {
                        onLoadFromNetwork(callback());
                    }

                });
                return mLiveData;
            }

            @Override
            public DoraCallback<?> callback() {
                return new DoraCallback<List<T>>() {
                    @Override
                    public void onSuccess(List<T> data) {
                        onInterceptNetworkData(data);
                        KeyValueUtils.getInstance().updateCacheAtMemory(getCacheName(), data);
                        mLiveData.setValue(data);
                    }

                    @Override
                    public void onFailure(int code, String msg) {
                        mLiveData.setValue(null);
                        KeyValueUtils.getInstance().removeCacheAtMemory(getCacheName());
                    }
                };
            }
        };
    }
}
