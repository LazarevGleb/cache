package cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

public abstract class BaseCache<T> implements Cache {
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    protected Set<T> cache;
    protected final long capacity;

    public BaseCache(final long capacity) {
        if (capacity < 1) {
            throw new IllegalArgumentException("Capacity must not be less than 1");
        }
        this.capacity = capacity;
    }

    public void clear() {
        cache.clear();
        logger.info("Cache is empty");
    }

    public long size() {
        return cache.size();
    }
}
