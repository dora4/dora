package dora;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import dora.cache.annotation.Repository;
import dora.db.OrmTable;
import dora.db.builder.QueryBuilder;
import dora.db.builder.WhereBuilder;
import dora.db.dao.DaoFactory;
import dora.db.dao.OrmDao;
import dora.http.DoraCallback;

@Repository(cacheName = "BaseDatabaseOnlyRepository", loadDataMethodName = "getData",
        isPreLoadBeforeRequestNetwork = true)
public abstract class BaseDatabaseOnlyRepository<T extends OrmTable> extends BaseRepository {

    OrmDao<T> dao;

    public BaseDatabaseOnlyRepository(Class<T> clazz) {
        dao = DaoFactory.getDao(clazz);
    }

    public class DefaultDoraCallback extends DoraCallback<List<T>> {

        private MutableLiveData<List<T>> liveData;

        public DefaultDoraCallback(MutableLiveData<List<T>> liveData) {
            this.liveData = liveData;
        }

        @Override
        public void onSuccess(List<T> data) {
            onInterceptNetworkData(data);
            dao.delete(where());
            dao.insert(data);
            liveData.setValue(data);
        }

        @Override
        public void onFailure(int code, String msg) {
            liveData.setValue(null);
            dao.delete(where());
        }
    }

    public LiveData<List<T>> getData() {
        final MutableLiveData<List<T>> liveData = new MutableLiveData<>();
        selectData(new DataSource() {

            @Override
            public boolean loadFromCache(CacheType type) {
                if (type == CacheType.DATABASE) {
                    List<T> models = dao.select(QueryBuilder.create()
                            .where(where()));
                    if (models.size() > 0) {
                        liveData.setValue(models);
                        return true;
                    } else {
                        return false;
                    }
                }
                return false;
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

    protected void onInterceptNetworkData(List<T> data) {
    }

    protected abstract void onLoadFromNetwork(DoraCallback<List<T>> callback);
}
