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

@Repository
public abstract class BaseDatabaseOnlyRepository<T extends OrmTable> extends BaseRepository<T> {

    OrmDao<T> dao;

    public BaseDatabaseOnlyRepository(Class<T> clazz) {
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
                        dao.delete(where());
                        dao.insert(data);
                        mLiveData.setValue(data);
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
                        dao.delete(where());
                        dao.insert(data);
                        mLiveData.setValue(data);
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
