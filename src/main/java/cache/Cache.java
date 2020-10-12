package cache;

import java.util.Map;
import java.util.Set;

/**
 * In-memory cache. Basic implementation is BaseCache.
 * Includes two eviction strategies: LRU and LFU.
 *
 * @param <K> Type of ids in cache
 * @see BaseCache
 * @see LfuCache
 * @see LruCache
 */
public interface Cache<K> {
    /**
     * Writes given value into the cache. If capacity is over then one item will be removed
     * from the cache according to chosen eviction strategy.
     *
     * @param value given object to be added to the cache
     */
    void put(K key, Object value);

    /**
     * Retrieves object from cache according to its key
     *
     * @param key id of object in cache
     * @return object stored in cache with given key
     */
    Object get(K key);

    /**
     * Removes all items from the cache
     */
    void clear();

    /**
     * Gets the number of items in the cache
     *
     * @return cache size
     */
    int size();

    /**
     * Gets all items from the cache with their ids
     *
     * @return set of all cache items
     */
    Set<Map.Entry<K, Object>> getCacheItemsWithKeys();

    /**
     * Gets all items from the cache
     *
     * @return set of all cache items
     */
    Set<Object> getCacheItems();
}
