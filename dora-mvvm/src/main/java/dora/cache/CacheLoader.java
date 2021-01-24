package dora.cache;

import java.lang.reflect.Method;

import dora.cache.annotation.Repository;
import dora.util.KeyValueUtils;
import dora.util.ReflectionUtils;
import dora.util.TextUtils;

public class CacheLoader {

    /**
     * 一般在Application中调用。
     *
     * @param repositories
     */
    public static void scan(Class<? extends BaseRepository>... repositories) {
        for (Class<? extends BaseRepository> repositoryClazz : repositories) {
            loadCache(repositoryClazz);
        }
    }

    /**
     * App启动时把部分仓库的数据加载到内存。
     *
     * @param repositoryClazz
     */
    private static void loadCache(Class<? extends BaseRepository> repositoryClazz) {
        Repository repository = repositoryClazz.getAnnotation(Repository.class);
        BaseRepository baseRepository = ReflectionUtils.newInstance(repositoryClazz);
        if (baseRepository != null && baseRepository.isCacheLoadedInLaunchTime()
                && baseRepository.hasMemoryCacheStrategy()) {
            String loadDataMethodName = repository.loadDataMethodName();
            if (TextUtils.isEmpty(loadDataMethodName)) {
                loadDataMethodName = baseRepository.getLoadDataMethodName();
            }
            String cacheName = repository.cacheName();
            if (TextUtils.isEmpty(cacheName)) {
                cacheName = baseRepository.getCacheName();
            }
            Method method = ReflectionUtils.newMethod(repositoryClazz, true, loadDataMethodName);
            Object data = ReflectionUtils.invokeMethod(baseRepository, method);
            KeyValueUtils.getInstance().setCacheToMemory(cacheName, data);
        }
    }
}
