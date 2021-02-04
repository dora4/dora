package dora.cache.repository;

import androidx.lifecycle.LiveData;

import java.util.List;

import dora.cache.annotation.Repository;
import dora.cache.data.DataLoader;
import dora.cache.data.DefaultDataFetcher;
import dora.cache.data.ListDataFetcher;
import dora.db.OrmTable;
import dora.db.builder.QueryBuilder;
import dora.db.builder.WhereBuilder;
import dora.db.dao.DaoFactory;
import dora.db.dao.OrmDao;
import dora.http.DoraCallback;
import dora.util.KeyValueUtils;

@Repository(cacheName = "BaseDatabaseFirstRepository",
        cacheStrategy = BaseRepository.DataSource.CacheStrategy.STRATEGY_DATABASE_FIRST,
        isCacheLoadedInLaunchTime = true)
public abstract class BaseDatabaseFirstRepository<T extends OrmTable> extends BaseRepository<T>
            implements DataLoader<T> {

    OrmDao<T> dao;

    public BaseDatabaseFirstRepository(Class<T> clazz) {
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
                        T entity = null;
                        if (type == CacheType.DATABASE) {
                            entity = dao.selectOne(QueryBuilder.create().where(where()));
                        } else if (type == CacheType.MEMORY) {
                            entity = (T) KeyValueUtils.getInstance().getCacheFromMemory(getCacheName());
                        }
                        if (entity != null) {
                            mLiveData.setValue(entity);
                            return true;
                        } else {
                            mLiveData.setValue(null);
                            return false;
                        }
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
                        mLiveData.setValue(data);
                        dao.delete(where());
                        dao.insert(data);
                    }

                    @Override
                    public void onFailure(int code, String msg) {
                        mLiveData.setValue(null);
                        dao.delete(where());
                    }
                };
            }
        };
    }

    @Override
    protected ListDataFetcher<?> installListDataFetcher() {
        return new ListDataFetcher<T>() {
            @Override
            public LiveData<List<T>> getData() {
                selectData(new DataSource() {
                    @Override
                    public boolean loadFromCache(CacheType type) {
                        List<T> entities = null;
                        if (type == CacheType.DATABASE) {
                            entities = dao.select(QueryBuilder.create().where(where()));
                        } else if (type == CacheType.MEMORY) {
                            entities = (List<T>) KeyValueUtils.getInstance().getCacheFromMemory(getCacheName());
                        }
                        if (entities != null && entities.size() > 0) {
                            mLiveData.setValue(entities);
                            return true;
                        } else {
                            mLiveData.setValue(null);
                            return false;
                        }
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
                        mLiveData.setValue(data);
                        dao.delete(where());
                        dao.insert(data);
                    }

                    @Override
                    public void onFailure(int code, String msg) {
                        mLiveData.setValue(null);
                        dao.delete(where());
                    }
                };
            }
        };
    }
}
