package dora.cache.data;

import androidx.lifecycle.LiveData;
import dora.cache.data.page.IDataPager;
import dora.db.OrmTable;
import dora.http.DoraListCallback;

import java.util.List;

public interface IListDataFetcher<T extends OrmTable> {

    LiveData<List<T>> getListData();

    DoraListCallback<T> listCallback();

    IDataPager<T> getPager();
}
