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

import java.util.List;

public abstract class BaseDatabaseCacheNoNetworkRepository<T extends OrmTable> extends BaseRepository<T> {

    private OrmDao<T> mDao;

    public BaseDatabaseCacheNoNetworkRepository(Context context, Class<T> clazz) {
        super(context);
        mDao = DaoFactory.getDao(clazz);
        mCacheStrategy = DataSource.CacheStrategy.DATABASE_CACHE_NO_NETWORK;
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

    @Override
    protected DataFetcher<T> installDataFetcher() {
        return new DataFetcher<T>() {
            @Override
            public LiveData<T> getData() {
                selectData(new DataSource() {
                    @Override
                    public boolean loadFromCache(CacheType type) {
                        if (type == CacheType.DATABASE) {
                            T entity = mDao.selectOne(where());
                            if (entity != null) {
                                onInterceptData(Type.CACHE, entity);
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
                        mDao.delete(where());
                        mDao.insert(data);
                        mLiveData.setValue(data);
                    }

                    @Override
                    public void onFailure(int code, String msg) {
                        if (isClearDataOnNetworkError()) {
                            mLiveData.setValue(null);
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

    @Override
    protected ListDataFetcher<T> installListDataFetcher() {
        return new ListDataFetcher<T>() {
            @Override
            public LiveData<List<T>> getListData() {
                selectData(new DataSource() {
                    @Override
                    public boolean loadFromCache(CacheType type) {
                        if (type == CacheType.DATABASE) {
                            List<T> entities = mDao.select(where());
                            if (entities != null && entities.size() > 0) {
                                onInterceptData(Type.CACHE, entities);
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
                        mDao.delete(where());
                        mDao.insert(data);
                        mLiveData.setValue(data);
                    }

                    @Override
                    public void onFailure(int code, String msg) {
                        if (isClearDataOnNetworkError()) {
                            mLiveData.setValue(null);
                            mDao.delete(where());
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
