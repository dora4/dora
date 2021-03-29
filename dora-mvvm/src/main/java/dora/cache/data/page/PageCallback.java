package dora.cache.data.page;

import java.util.List;

public interface PageCallback<T> {
    void onResult(List<T> data);
}