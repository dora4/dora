package dora.cache.data;

import androidx.lifecycle.LiveData;
import dora.db.OrmTable;
import dora.http.DoraCallback;

public interface IDataFetcher<T extends OrmTable> {

    LiveData<T> getData();

    DoraCallback<T> callback();
}
