package dora.cache.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import dora.cache.BaseRepository;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Repository {

    /**
     * 缓存策略决定数据读取的来源和优先级。
     *
     * @see BaseRepository.DataSource.CacheStrategy#STRATEGY_DATABASE_ONLY
     * @see BaseRepository.DataSource.CacheStrategy#STRATEGY_MEMORY_ONLY
     * @see BaseRepository.DataSource.CacheStrategy#STRATEGY_DATABASE_FIRST
     * @see BaseRepository.DataSource.CacheStrategy#STRATEGY_MEMORY_FIRST
     */
    int cacheStrategy() default BaseRepository.DataSource.CacheStrategy.STRATEGY_DATABASE_ONLY;

    /**
     * 缓存名称，只有使用到{@link BaseRepository.DataSource.CacheType#MEMORY}的时候，才起作用，否则
     * 可以忽略。在{@link dora.cache.CacheLoader#scan(Class[])}被调用后，会把扫描到的数据通过{@link
     * dora.util.KeyValueUtils#setCacheToMemory(String, Object)}保存起来，然后这个key的值就等于cacheName，
     * 你可以在项目代码的任意位置通过{@link dora.util.KeyValueUtils#getCacheFromMemory(String)}拿到它。
     *
     * @return 如SampleRepository
     */
    String cacheName() default "";

    /**
     * 是否开启app冷启动时加载缓存，将会影响一部分性能，降低启动速度，默认关闭。
     *
     * @return true代表启用，false反之，仅作为标识，表示有这个能力
     */
    boolean isCacheLoadedInLaunchTime() default false;

    /**
     * 加载数据方法名称，只有使用到{@link BaseRepository.DataSource.CacheType#MEMORY}的时候，才起作用，
     * 否则可以忽略。{@link dora.cache.CacheLoader#scan(Class[])}被调用后，将会调用所有{@link BaseRepository}
     * 的子类的该名称的方法，同时需要{@link #isCacheLoadedInLaunchTime()}保持开启状态。
     * @return
     */
    String loadDataMethodName() default  "loadData";

    /**
     * 在调用网络接口之前是否先加载缓存的数据，这样可以大大提升数据加载的速度，提升用户体验。
     *
     * @return 默认开启
     */
    boolean isPreLoadBeforeRequestNetwork() default true;
}
