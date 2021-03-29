package dora.cache.repository;

import android.content.Context;
import androidx.lifecycle.LiveData;
import dora.cache.data.DataFetcher;
import dora.cache.data.ListDataFetcher;
import dora.db.OrmTable;
import dora.http.DoraCallback;
import dora.http.DoraListCallback;

import java.util.List;

public abstract class BaseNoCacheRepository<T extends OrmTable> extends BaseRepository<T> {

    protected BaseNoCacheRepository(Context context) {
        super(context);
    }

    @Override
    protected DataFetcher<T> installDataFetcher() {
        return new DataFetcher<T>() {
            @Override
            public LiveData<T> getData() {
                selectData(new DataSource() {
                    @Override
                    public boolean loadFromCache(CacheType type) {
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
                        mLiveData.setValue(data);
                    }

                    @Override
                    public void onFailure(int code, String msg) {
                        if (isClearDataOnNetworkError()) {
                            mLiveData.setValue(null);
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
                        mLiveData.setValue(data);
                    }

                    @Override
                    public void onFailure(int code, String msg) {
                        if (isClearDataOnNetworkError()) {
                            mLiveData.setValue(null);
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
