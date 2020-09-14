package dora;

import android.os.Bundle;

import dora.cache.Cache;

public interface FragmentCache {

    Cache<String, Object> loadCache();

    Cache.Factory cacheFactory();

    void initData(Bundle savedInstanceState);
}
