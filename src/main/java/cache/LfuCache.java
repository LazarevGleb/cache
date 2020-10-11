package cache;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class LfuCache extends BaseCache<CacheObject> {
    public LfuCache(final int capacity) {
        super(capacity);
        cache = new LinkedHashSet<>(capacity);
    }

    @Override
    public void add(Object value) {
        Optional<CacheObject> cacheObject = cache.stream()
                .filter(c -> c.getValue().equals(value))
                .findFirst();
        if (cacheObject.isPresent()) {
            cacheObject.get().incrementFrequency();
            return;
        }

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
        return cache.stream().map(CacheObject::getValue).collect(Collectors.toSet());
    }
}
