package dora.memory;

import android.content.Context;

import java.util.Set;

public interface Cache<K, V> {

    /**
     * Return the total size currently occupied by the cache.
     * 简体中文：返回当前缓存已占用的总size。
     */
    int size();

    /**
     * Return the maximum size allowed by the current cache.
     * 简体中文：返回当前缓存所能允许的最大size。
     */
    int getMaxSize();

    /**
     * Return the value corresponding to the key in the cache. If null is returned, it means that
     * the key does not have a corresponding value in the cache.
     * 简体中文：返回这个key在缓存中对应的value，如果返回null，说明这个key没有对应的value。
     */
    V get(K key);

    /**
     * Add the key and value as an entry to the cache. If the key already has a corresponding value
     * in the cache, the existing value will be replaced with the new value and returned. If the
     * return value is null, it indicates a new entry.
     * 简体中文：将key和value以条目的形式加入缓存,如果这个key在缓存中已经有对应的value，则此value被新的value替换
     * 并返回，如果为null，说明是一个新条目。
     *
     * @return If the key is already stored with a value in the container, return the previous
     * value; otherwise, return null.
     */
    V put(K key, V value);

    /**
     * Remove the entry corresponding to the key in the cache and return the value of the removed
     * entry. If the return value is null, it is possible that either the value corresponding to
     * the key is null or the entry does not exist.
     * 简体中文：移除缓存中这个key所对应的条目，并返回所移除条目的value。如果返回为null，则有可能时因为这个key对应
     * 的value为null或条目不存在。
     *
     * @return If the key is already stored with a value in the container and the deletion is
     * successful, return the deleted value; otherwise, return null.
     */
    V remove(K key);

    /**
     * If there is a corresponding non-null value for this key in the cache, return true.
     * 简体中文：如果这个key在缓存中有对应的value并且不为null，则返回true。
     *
     * @return true if the container contains the key, false otherwise.
     */
    boolean containsKey(K key);

    /**
     * Return all the keys contained in the current cache.
     * 简体中文：返回当前缓存中含有的所有key。
     */
    Set<K> keySet();

    /**
     * Clear all content in the cache.
     * 简体中文：清除缓存中所有的内容。
     */
    void clear();

    interface Factory {

        /**
         * Returns a new cache.
         * 简体中文：返回一个新的Cache对象。
         */
        Cache build(CacheType type, Context context);
    }
}