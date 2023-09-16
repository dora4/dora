package dora.memory;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.ViewDataBinding;

public interface DataLoader<T extends ViewDataBinding> {

    Cache<String, Object> loadCache();

    Cache.Factory cacheFactory();

    void initData(@Nullable Bundle savedInstanceState, @NonNull T binding);
}
