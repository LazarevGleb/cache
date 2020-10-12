package cache;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * Least recently used (LRU) cache strategy
 *
 * @param <K> Type of ids in cache
 */
public class LruCache<K> extends BaseCache<K, Object> {
    public LruCache(final int capacity) {
        super(capacity);
        cache = new LinkedHashMap<>(capacity) {
            @Override
            protected boolean removeEldestEntry(Map.Entry eldest) {
                return size() > capacity;
            }
        };
    }

    /**
     * Writes given object into the cache. If cache is full then removes
     * the least recently used object. Even if given object already exists in the cache,
     * it should be moved onto the top. That's why there is a removing of the object
     * from the cache
     *
     * @param value given object to be added to the cache
     */
    @Override
    public void put(K key, Object value) {
        cache.putIfAbsent(key, value);
        logger.info("Object {} is added or simply accessed", value);
    }

    @Override
    public Object get(K key) {
        return cache.get(key);
    }

    @Override
    public Set<Map.Entry<K, Object>> getCacheItemsWithKeys() {
        return cache.entrySet();
    }

    @Override
    public Set<Object> getCacheItems() {
        return new HashSet<>(cache.values());
    }
}
