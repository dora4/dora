package dora.cache.data;

import androidx.lifecycle.MutableLiveData;
import dora.db.OrmTable;

public abstract class DataFetcher<T extends OrmTable> implements IDataFetcher<T> {

    protected MutableLiveData<T> mLiveData;

    {
        mLiveData = new MutableLiveData<>();
    }
}
