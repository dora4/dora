package dora;

import android.os.Bundle;

public interface FragmentCache {

    Cache<String, Object> loadCache();

    Cache.Factory cacheFactory();

    void initData(Bundle savedInstanceState);
}
