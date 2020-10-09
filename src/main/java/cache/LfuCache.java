package cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class LfuCache implements Cache {
    private final Logger logger = LoggerFactory.getLogger(LfuCache.class);
    private final Set<CacheObject> cache;
    private final long capacity;

    public LfuCache(final int capacity) {
        if (capacity < 1) {
            throw new IllegalArgumentException("Capacity must not be less than 1");
        }
        cache = new LinkedHashSet<>(capacity);
        this.capacity = capacity;
    }

    @Override
    public void add(Object value) {
        cache.stream()
                .filter(a -> a.getValue().equals(value))
                .findFirst()
                .ifPresent(cache::remove); //TODO increment frequency

        if (cache.size() == capacity) {
            int minFrequency = Integer.MAX_VALUE;
            CacheObject lfu = null;
            for (CacheObject current : cache) {
                if (Objects.equals(value, current.getValue())) {
                    return;
                } else if (minFrequency >= current.getFrequency()) {
                    minFrequency = current.getFrequency();
                    lfu = current;
                }
            }
            cache.remove(lfu);
        }
        cache.add(new CacheObject(value));
        logger.info("Object {} is added or simply accessed", value);
    }

    @Override
    public boolean contains(Object value) {
        Optional<CacheObject> first = cache.stream()
                .filter(a -> a.getValue().equals(value))
                .findFirst();
        if (!first.isPresent()) {
            return false;
        }
        CacheObject current = first.get();
        cache.remove(current);
        cache.add(current);
        return true;
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

    @Override
    public Set<Object> getCacheItems() {
        return cache.stream().map(CacheObject::getValue).collect(Collectors.toSet());
    }
}
