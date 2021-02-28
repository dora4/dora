package dora.cache.repository;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Repository {

    /**
     * 缓存策略标记。
     *
     * @see BaseRepository.DataSource.CacheStrategy#NO_CACHE
     * @see BaseRepository.DataSource.CacheStrategy#DATABASE_CACHE
     * @see BaseRepository.DataSource.CacheStrategy#MEMORY_CACHE
     */
    int cacheStrategy() default BaseRepository.DataSource.CacheStrategy.NO_CACHE;

    /**
     * 是否是List数据，如果不为List数据，请修改为false。
     *
     * @return
     */
    boolean isListData() default true;
}
