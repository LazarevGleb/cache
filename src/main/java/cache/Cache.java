package cache;

import java.util.Set;

/**
 * In-memory cache. Basic implementation is BaseCache.
 * Includes two eviction strategies: LRU and LFU.
 *
 * @see BaseCache
 * @see LfuCache
 * @see LruCache
 */
public interface Cache {
    /**
     * Writes given value into the cache. If capacity is over then one item will be removed
     * from the cache according to chosen eviction strategy.
     *
     * @param value given object to be added to the cache
     */
    void add(Object value);

    /**
     * Checks whether given object already exists in cache.
     *
     * @param value given object to be checked
     * @return true, if object already exists in cache
     */
    boolean contains(Object value);

    /**
     * Removes all items from the cache
     */
    void clear();

    /**
     * Gets the number of items in the cache
     *
     * @return cache size
     */
    long size();

    /**
     * Gets all items from the cache
     *
     * @return set of all cache items
     */
    Set<Object> getCacheItems();
}
