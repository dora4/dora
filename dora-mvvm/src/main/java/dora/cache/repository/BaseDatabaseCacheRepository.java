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

public abstract class BaseDatabaseCacheRepository<T extends OrmTable> extends BaseRepository<T> {

    OrmDao<T> dao;

    public BaseDatabaseCacheRepository(Class<T> clazz) {
        dao = DaoFactory.getDao(clazz);
        mCacheStrategy = DataSource.CacheStrategy.DATABASE_CACHE;
    }

    protected WhereBuilder where() {
        return WhereBuilder.create();
    }

    @Override
    protected DataFetcher<T> installDataFetcher() {
        return new DataFetcher<T>() {
            @Override
            public LiveData<T> getData() {
                selectData(new DataSource() {
                    @Override
                    public boolean loadFromCache(CacheType type) {
                        if (type == CacheType.DATABASE) {
                            T entity = dao.selectOne(where());
                            if (entity != null) {
                                mLiveData.setValue(entity);
                            }
                            return true;
                        }
                        mLiveData.setValue(null);
                        return false;
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
                        dao.delete(where());
                        dao.insert(data);
                        mLiveData.setValue(data);
                    }

                    @Override
                    public void onFailure(int code, String msg) {
                        mLiveData.setValue(null);
                        if (isClearDatabaseOnNetworkError()) {
                            dao.delete(where());
                        }
                    }
                };
            }
        };
    }

    @Override
    protected ListDataFetcher<T> installListDataFetcher() {
        return new ListDataFetcher<T>() {
            @Override
            public LiveData<List<T>> getListData() {
                selectData(new DataSource() {
                    @Override
                    public boolean loadFromCache(CacheType type) {
                        if (type == CacheType.DATABASE) {
                            List<T> entities = dao.select(where());
                            if (entities != null && entities.size() > 0) {
                                mLiveData.setValue(entities);
                            }
                            return true;
                        }
                        mLiveData.setValue(null);
                        return false;
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
                        dao.delete(where());
                        dao.insert(data);
                        mLiveData.setValue(data);
                    }

                    @Override
                    public void onFailure(int code, String msg) {
                        mLiveData.setValue(null);
                        if (isClearDatabaseOnNetworkError()) {
                            dao.delete(where());
                        }
                    }
                };
            }
        };
    }
}
