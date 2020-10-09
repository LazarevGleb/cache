package cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Least recently used cache strategy
 */
public class LruCache implements Cache {
    private final Logger logger = LoggerFactory.getLogger(LruCache.class);
    private final Set<Object> cache;
    private final long capacity;

    public LruCache(final int capacity) {
        if (capacity < 1) {
            throw new IllegalArgumentException("Capacity must not be less than 1");
        }
        cache = new LinkedHashSet<>(capacity);
        this.capacity = capacity;
    }

    /**
     * Checks whether given object already exists in cache. Does remove and add operations
     * to move object to the top of cache
     *
     * @param value given object to be checked
     * @return true, if object already exists in cache
     */
    public boolean contains(Object value) {
        if (!cache.contains(value)) {
            return false;
        }
        cache.remove(value);
        cache.add(value);
        logger.info("Object {} is accessed", value);
        return true;
    }

    /**
     * Writes given object to the cache. If cache is full then removes
     * the least recently used object. Even if given object already exists in the cache,
     * it should be moved onto the top. That's why there is a removing of the object
     * from the cache
     *
     * @param value given object to be added to the cache
     */
    public void add(Object value) {
        if (cache.contains(value)) {
            cache.remove(value);
        } else if (cache.size() == capacity) {
            Object lru = cache.iterator().next();
            cache.remove(lru);
        }
        cache.add(value);
        logger.info("Object {} is added or simply accessed", value);
    }

    public void clear() {
        cache.clear();
        logger.info("Cache is empty");
    }

    public long size() {
        return cache.size();
    }

    public Set<Object> getCacheItems() {
        return cache;
    }
}
