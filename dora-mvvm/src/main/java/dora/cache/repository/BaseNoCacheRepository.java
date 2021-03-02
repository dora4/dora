package dora.cache.repository;

import androidx.lifecycle.LiveData;

import java.util.List;

import dora.cache.data.DataFetcher;
import dora.cache.data.ListDataFetcher;
import dora.db.OrmTable;
import dora.http.DoraCallback;
import dora.http.DoraListCallback;

public abstract class BaseNoCacheRepository<T extends OrmTable> extends BaseRepository<T> {

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
                        mLiveData.setValue(null);
                    }

                    @Override
                    protected void onInterceptNetworkData(T data) {
                        BaseNoCacheRepository.this.onInterceptNetworkData(data);
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
                        mLiveData.setValue(null);
                    }

                    @Override
                    protected void onInterceptNetworkData(List<T> data) {
                        BaseNoCacheRepository.this.onInterceptNetworkData(data);
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
