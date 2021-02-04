package dora.cache.data;

import androidx.lifecycle.MutableLiveData;

public abstract class DefaultDataFetcher<T> implements DataFetcher<T> {

    protected MutableLiveData<T> mLiveData;

    {
        mLiveData = new MutableLiveData<>();
    }
}
