package dora.cache.data;

import androidx.lifecycle.LiveData;

import dora.http.DoraCallback;

public interface DataFetcher<T> {

    LiveData<T> getData();

    DoraCallback<?> callback();
}
