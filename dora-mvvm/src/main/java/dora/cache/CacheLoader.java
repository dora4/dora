package dora.cache;

import dora.cache.repository.BaseMemoryCacheRepository;
import dora.util.KeyValueUtils;
import dora.util.ReflectionUtils;

public class CacheLoader {

    /**
     * 一般在Application中调用。
     *
     * @param repositories
     */
    public static void scan(Class<? extends BaseMemoryCacheRepository>... repositories) {
        for (Class<? extends BaseMemoryCacheRepository> repositoryClazz : repositories) {
            loadCache(repositoryClazz);
        }
    }

    /**
     * App启动时把部分仓库的数据加载到内存。
     *
     * @param repositoryClazz
     */
    private static void loadCache(Class<? extends BaseMemoryCacheRepository> repositoryClazz) {
        BaseMemoryCacheRepository repository = ReflectionUtils.newInstance(repositoryClazz);
        Object data = repository.loadData();
        KeyValueUtils.getInstance().updateCacheAtMemory(repository.getCacheName(), data);
    }
}
