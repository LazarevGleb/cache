package cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

/**
 * Basic implementation of the Cache. Does not include add and contains methods,
 * cause they are implemented by one of the cache strategies
 *
 * @param <T> type of cache items according to chosen strategy
 * @see LruCache
 * @see LfuCache
 */
public abstract class BaseCache<T> implements Cache {
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    protected Set<T> cache;
    protected final long capacity;

    /**
     * Initialises capacity of the cache. It must be positive number
     *
     * @param capacity a capacity of the cache
     * @throws IllegalArgumentException if capacity is less than 1
     */
    public BaseCache(final long capacity) {
        if (capacity < 1) {
            throw new IllegalArgumentException("Capacity must not be less than 1");
        }
        this.capacity = capacity;
    }

    @Override
    public void clear() {
        cache.clear();
        logger.info("Cache is empty");
    }

    @Override
    public long size() {
        return cache.size();
    }
}
