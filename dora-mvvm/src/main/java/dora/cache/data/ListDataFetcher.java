package dora.cache.data;

import androidx.lifecycle.MutableLiveData;

import java.util.List;

import dora.db.OrmTable;

public abstract class ListDataFetcher<T extends OrmTable> implements IListDataFetcher<T> {

    protected MutableLiveData<List<T>> mLiveData;

    {
        mLiveData = new MutableLiveData<>();
    }
}
