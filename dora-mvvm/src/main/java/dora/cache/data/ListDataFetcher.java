package dora.cache.data;

import androidx.lifecycle.MutableLiveData;

import java.util.List;

import dora.cache.data.page.DataPager;
import dora.cache.data.page.IDataPager;
import dora.db.OrmTable;

public abstract class ListDataFetcher<T extends OrmTable> implements IListDataFetcher<T> {

    protected IDataPager<T> mPage;

    protected MutableLiveData<List<T>> mLiveData;

    {
        mLiveData = new MutableLiveData<>();
    }

    public IDataPager<T> getPager() {
        return mPage = new DataPager<>(mLiveData.getValue());
    }
}
