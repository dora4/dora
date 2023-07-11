package dora.memory;

import androidx.annotation.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class LruCache<K, V> implements Cache<K, V> {
    private final LinkedHashMap<K, V> cache = new LinkedHashMap<>(100, 0.75f, true);
    private final int initialMaxSize;
    private int maxSize;
    private int currentSize = 0;

    /**
     * @param size The maximum size of this cache, where the unit of size used must be consistent
     *            with the unit used in {@link #getItemSize(Object)}.
     */
    public LruCache(int size) {
        this.initialMaxSize = size;
        this.maxSize = size;
    }

    /**
     * Set a coefficient to be applied to the size passed in the constructor to obtain a new
     * {@link #maxSize}.It will immediately call {@link #evict} to start clearing the entries that
     * meet the criteria.
     * 简体中文：设置一个系数应用于当时构造函数中所传入的size，从而得到一个新的{@link #maxSize}
     * 并会立即调用{@link #evict}开始清除满足条件的条目。
     */
    public synchronized void setSizeMultiplier(float multiplier) {
        if (multiplier < 0) {
            throw new IllegalArgumentException("Multiplier must be >= 0");
        }
        maxSize = Math.round(initialMaxSize * multiplier);
        evict();
    }

    /**
     * Returns the size occupied by each {@code item}, which is initially set to 1. The unit of
     * this size must be consistent with the size passed in the constructor.Subclasses can override
     * this method to adapt to different units, such as bytes.
     * 简体中文：返回每个{@code item}所占用的size，默认为1，这个size的单位必须和构造函数所传入的size一致
     * 子类可以重写这个方法以适应不同的单位，比如说bytes
     *
     * @param item The size occupied by each {@code item}.
     * @return The size of an individual item.
     */
    protected int getItemSize(V item) {
        return 1;
    }

    /**
     * This method is called when an entry is evicted from the cache. It has a default empty
     * implementation, and subclasses can override it.
     * 简体中文：当缓存中有被驱逐的条目时，会回调此方法，默认空实现，子类可以重写这个方法。
     */
    protected void onItemEvicted(K key, V value) {
        // optional override
    }

    /**
     * Return the maximum size allowed by the current cache.
     * 简体中文：返回当前缓存所能允许的最大size。
     */
    @Override
    public synchronized int getMaxSize() {
        return maxSize;
    }

    /**
     * Return the total size currently occupied by the cache.
     * 简体中文：返回当前缓存已占用的总size。
     */
    @Override
    public synchronized int size() {
        return currentSize;
    }

    /**
     * If there is a corresponding non-null value for this key in the cache, return true.
     * 简体中文：如果这个key在缓存中有对应的value并且不为null，则返回true。
     *
     * @return true if the container contains the key, false otherwise.
     */
    @Override
    public synchronized boolean containsKey(K key) {
        return cache.containsKey(key);
    }

    /**
     * Return all the keys contained in the current cache.
     * 简体中文：返回当前缓存中含有的所有key。
     */
    @Override
    public synchronized Set<K> keySet() {
        return cache.keySet();
    }

    /**
     * Return the value corresponding to the key in the cache. If null is returned, it means that
     * the key does not have a corresponding value in the cache.
     * 简体中文：返回这个key在缓存中对应的value，如果返回null，说明这个key没有对应的value。
     */
    @Override
    @Nullable
    public synchronized V get(K key) {
        return cache.get(key);
    }

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
    @Override
    @Nullable
    public synchronized V put(K key, V value) {
        final int itemSize = getItemSize(value);
        if (itemSize >= maxSize) {
            onItemEvicted(key, value);
            return null;
        }

        final V result = cache.put(key, value);
        if (value != null) {
            currentSize += getItemSize(value);
        }
        if (result != null) {
            currentSize -= getItemSize(result);
        }
        evict();

        return result;
    }

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
    @Override
    @Nullable
    public synchronized V remove(K key) {
        final V value = cache.remove(key);
        if (value != null) {
            currentSize -= getItemSize(value);
        }
        return value;
    }

    /**
     * Clear all contents in the cache.
     * 简体中文：清除缓存中所有的内容。
     */
    @Override
    public void clear() {
        trimToSize(0);
    }

    /**
     * When the specified size is smaller than the current total size occupied by the cache, it will
     * start clearing the least recently used entries in the cache.
     * 简体中文：当指定的size小于当前缓存已占用的总size时，会开始清除缓存中最近最少使用的条目。
     */
    protected synchronized void trimToSize(int size) {
        Map.Entry<K, V> last;
        while (currentSize > size) {
            last = cache.entrySet().iterator().next();
            final V toRemove = last.getValue();
            currentSize -= getItemSize(toRemove);
            final K key = last.getKey();
            cache.remove(key);
            onItemEvicted(key, toRemove);
        }
    }

    /**
     * When the total size occupied by the cache exceeds the maximum allowed size,
     * {@link #trimToSize(int)} is used to start clearing the entries that meet the criteria.
     * 简体中文：当缓存中已占用的总size大于所能允许的最大size，会使用{@link #trimToSize(int)}开始清除满足条件的
     * 条目
     */
    private void evict() {
        trimToSize(maxSize);
    }
}

