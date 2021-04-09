package dora;

import android.os.Bundle;

import androidx.annotation.Nullable;

public interface ActivityCache {

    Cache<String, Object> loadCache();

    Cache.Factory cacheFactory();

    void initData(@Nullable Bundle savedInstanceState);
}
