package cache;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Least frequently used (LFU) cache strategy
 *
 * @param <K> Type of ids in cache
 */
public class LfuCache<K> extends BaseCache<K, CacheObject> {
    public LfuCache(final int capacity) {
        super(capacity);
        cache = new LinkedHashMap<>(capacity);
    }

    /**
     * Writes given object into the cache. If cache already includes given value
     * then nothing happens. If cache is full then the least frequently used
     * object is deleted.
     *
     * @param value given object to be added to the cache
     */
    @Override
    public void put(K key, Object value) {
        if (contains(value)) return;
        checkValueAndRemove();
        cache.putIfAbsent(key, new CacheObject(value));
        logger.info("Object {} is added or simply accessed", value);
    }

    //Checks whether given object already exists in cache. If exists then the frequency
    //of the object is incremented
    private boolean contains(Object value) {
        Optional<Map.Entry<K, CacheObject>> cacheObject = cache.entrySet()
                .stream()
                .filter(c -> {
                    Object item = c.getValue().getItem();
                    return Objects.equals(item, value);
                })
                .findFirst();
        if (cacheObject.isPresent()) {
            cacheObject.get().getValue().incrementFrequency();
            return true;
        }
        return false;
    }

    // Checks whether cache capacity is over. If it is then
    //least frequently used object should be removed from the cache
    private void checkValueAndRemove() {
        if (cache.size() == capacity) {
            // In turn compare frequencies to find the lfu and its id in the cache

            int minFrequency = Integer.MAX_VALUE;
            K id = null;
            CacheObject lfu = null;

            for (Map.Entry<K, CacheObject> entry : cache.entrySet()) {
                CacheObject current = entry.getValue();

                if (minFrequency >= current.getFrequency()) {
                    minFrequency = current.getFrequency();
                    id = entry.getKey();
                    lfu = current;
                }
            }
            if (id != null) {
                cache.remove(id, lfu);
            }
        }
    }

    @Override
    public Object get(K key) {
        Optional<CacheObject> result = Optional.ofNullable(cache.get(key));
        return result.map(CacheObject::getItem).orElse(null);
    }

    @Override
    public Set<Map.Entry<K, Object>> getCacheItemsWithKeys() {
        return cache.entrySet()
                .stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> e.getValue().getItem()
                ))
                .entrySet();
    }

    @Override
    public Set<Object> getCacheItems() {
        return cache.values()
                .stream()
                .map(CacheObject::getItem)
                .collect(Collectors.toSet());
    }
}
