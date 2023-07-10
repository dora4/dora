package dora.memory;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.ViewDataBinding;

public interface DataLoader<T extends ViewDataBinding> {

    Cache<String, Object> loadCache();

    Cache.Factory cacheFactory();

    /**
     * Use {@link #initData(Bundle, ViewDataBinding)} instead, it will be removed in the future.
     */
    @Deprecated
    void initData(@Nullable Bundle savedInstanceState);

    void initData(@Nullable Bundle savedInstanceState, @NonNull T binding);
}
