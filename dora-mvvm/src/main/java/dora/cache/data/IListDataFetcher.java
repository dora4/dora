package dora.cache.data;

import androidx.lifecycle.LiveData;

import java.util.List;

import dora.db.OrmTable;
import dora.http.DoraListCallback;

public interface IListDataFetcher<T extends OrmTable> {

    LiveData<List<T>> getListData();

    DoraListCallback<T> listCallback();
}
