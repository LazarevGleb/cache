package cache;

import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Least frequently used (LFU) cache strategy
 */
public class LfuCache extends BaseCache<CacheObject> {
    public LfuCache(final int capacity) {
        super(capacity);
        cache = new LinkedHashSet<>(capacity);
    }

    /**
     * Writes given object into the cache. If cache already includes given value
     * then nothing happens. If cache is full then the least frequently used
     * object is deleted.
     *
     * @param value given object to be added to the cache
     */
    @Override
    public void add(Object value) {
        if (contains(value)) return;

        checkValueAndRemove(value);
        cache.add(new CacheObject(value));
        logger.info("Object {} is added or simply accessed", value);
    }

    private void checkValueAndRemove(Object value) {
        if (cache.size() == capacity) {
            // In turn compare frequencies to find the least
            int minFrequency = Integer.MAX_VALUE;
            CacheObject lfu = null;
            // flag is needed because null is available to be in cache
            boolean isLfuDetected = false;
            for (CacheObject current : cache) {
                if (current.getValue().equals(value)) {
                    return;
                } else if (minFrequency >= current.getFrequency()) {
                    minFrequency = current.getFrequency();
                    lfu = current;
                    isLfuDetected = true;
                }
            }
            if (isLfuDetected) {
                cache.remove(lfu);
            }
        }
    }

    /**
     * Checks whether given object already exists in cache. If true then the frequency
     * of the object is incremented
     *
     * @param value given object to be checked
     * @return true, if object already exists in cache
     */
    @Override
    public boolean contains(Object value) {
        Optional<CacheObject> cacheObject = cache.stream()
                .filter(c -> c.getValue().equals(value))
                .findFirst();
        if (cacheObject.isPresent()) {
            cacheObject.get().incrementFrequency();
            return true;
        }
        return false;
    }

    @Override
    public Set<Object> getCacheItems() {
        return cache.stream()
                .map(CacheObject::getValue)
                .collect(Collectors.toSet());
    }
}
