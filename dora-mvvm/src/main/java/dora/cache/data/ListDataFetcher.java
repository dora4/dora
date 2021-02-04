package dora.cache.data;

import androidx.lifecycle.MutableLiveData;

import java.util.List;

public abstract class ListDataFetcher<T> implements DataFetcher<List<T>> {

    protected MutableLiveData<List<T>> mLiveData;

    {
        mLiveData = new MutableLiveData<>();
    }
}
