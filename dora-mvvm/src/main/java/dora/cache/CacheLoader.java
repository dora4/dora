package dora.cache;

import java.lang.reflect.Method;

import dora.BaseRepository;
import dora.cache.annotation.Repository;
import dora.util.KeyValueUtils;
import dora.util.ReflectionUtils;

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
        String loadDataMethodName = repository.loadDataMethodName();
        String cacheName = repository.cacheName();
        BaseRepository baseRepository = ReflectionUtils.newInstance(repositoryClazz);
        if (baseRepository != null && baseRepository.isCacheLoadedInLaunchTime()
                && baseRepository.hasMemoryCacheStrategy()) {
            Method method = ReflectionUtils.newMethod(repositoryClazz, true, loadDataMethodName);
            Object data = ReflectionUtils.invokeMethod(baseRepository, method);
            KeyValueUtils.getInstance().setCacheToMemory(cacheName, data);
        }
    }
}
