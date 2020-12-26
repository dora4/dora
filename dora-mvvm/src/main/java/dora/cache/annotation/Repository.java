package dora.cache.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import dora.BaseRepository;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Repository {

    String cacheName();

    int cacheStrategy() default BaseRepository.DataSource.CacheStrategy.STRATEGY_DATABASE_ONLY;

    boolean isCacheLoadedInLaunchTime() default false;

    String loadDataMethodName();
}
