package dora;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import dora.cache.annotation.Repository;
import dora.db.OrmTable;
import dora.db.builder.QueryBuilder;
import dora.db.builder.WhereBuilder;
import dora.db.dao.DaoFactory;
import dora.db.dao.OrmDao;
import dora.http.DoraCallback;
import dora.util.KeyValueUtils;

@Repository(cacheName = "BaseMemoryFirstRepository",
        cacheStrategy = BaseRepository.DataSource.CacheStrategy.STRATEGY_MEMORY_FIRST,
        isCacheLoadedInLaunchTime = true,
        isPreLoadBeforeRequestNetwork = true,
        loadDataMethodName = "loadData")
public abstract class BaseMemoryFirstRepository<T extends OrmTable> extends BaseRepository {

    OrmDao<T> dao;

    public BaseMemoryFirstRepository(Class<T> clazz) {
        dao = DaoFactory.getDao(clazz);
    }

    public class DefaultDoraCallback extends DoraCallback<T> {

        private MutableLiveData<T> liveData;

        public DefaultDoraCallback(MutableLiveData<T> liveData) {
            this.liveData = liveData;
        }

        @Override
        public void onSuccess(T data) {
            onInterceptNetworkData(data);
            liveData.setValue(data);
            KeyValueUtils.getInstance().setCacheToMemory(getCacheName(), data);
            dao.delete(where());
            dao.insert(data);
        }

        @Override
        public void onFailure(int code, String msg) {
            liveData.setValue(null);
            KeyValueUtils.getInstance().removeCacheAtMemory(getCacheName());
            dao.delete(where());
        }
    }

    protected abstract T loadData();

    public LiveData<T> getData() {
        final MutableLiveData<T> liveData = new MutableLiveData<>();
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
                    liveData.setValue(entity);
                    return true;
                } else {
                    liveData.setValue(null);
                    return false;
                }
            }

            @Override
            public void loadFromNetwork() throws Exception {
                onLoadFromNetwork(new DefaultDoraCallback(liveData));
            }
        });
        return liveData;
    }

    protected WhereBuilder where() {
        return WhereBuilder.create();
    }

    protected void onInterceptNetworkData(T data) {
    }

    protected abstract void onLoadFromNetwork(DoraCallback<T> callback);
}
