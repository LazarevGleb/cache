package cache;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Least frequently used (LFU) cache strategy
 *
 * @param <K> Type of ids in cache
 */
public class LfuCache<K> extends BaseCache<K, CacheObject> {
    private final Map<K, CacheObject> candidates;
    private int maxCandidateFrequency;

    public LfuCache(final int capacity) {
        super(capacity);
        cache = new HashMap<>(capacity);
        candidates = new HashMap<>(capacity);
        maxCandidateFrequency = 0;
    }

    //Checks whether given object already exists in given storage. If exists then the frequency
    //of the object is incremented
    private boolean incrementFrequencyIfContains(Map<K, CacheObject> storage, Object value) {
        Optional<CacheObject> cacheObject = storage.values()
                .stream()
                .filter(c -> {
                    Object item = c.getItem();
                    return Objects.equals(item, value);
                })
                .findFirst();
        if (cacheObject.isPresent()) {
            cacheObject.get().incrementFrequency();
            updateMaxCandidateFrequency(storage);
            return true;
        }
        return false;
    }

    // if given storage is for candidates to main cache then update max candidates' frequency
    private void updateMaxCandidateFrequency(Map<K, CacheObject> storage) {
        if (storage.equals(candidates)) {
            candidates.values().forEach(item -> {
                if (item.getFrequency() > maxCandidateFrequency) {
                    maxCandidateFrequency = item.getFrequency();
                }
            });
        }
    }

    /**
     * Writes given object into the cache. If cache already includes given value
     * then nothing happens. If cache is full then the least frequently used
     * object is deleted.
     *
     * @param key given key of object to be added to the cache
     * @param value given object to be added to the cache
     */
    @Override
    public void put(K key, Object value) {
        if (incrementFrequencyIfContains(cache, value)) return;
        putInCache(key, value);
        logger.info("Object {} is added or simply accessed", value);
    }

    // if main cache is full then add value to the candidates
    private void putInCache(K key, Object value) {
        if (cache.size() == capacity) {
            putInCandidates(key, value);
            findBestCandidateAndChangeCaches();
        } else {
            cache.put(key, new CacheObject(value));
        }
    }

    // if candidates already has this value then just increment its frequency
    private void putInCandidates(K key, Object value) {
        if (incrementFrequencyIfContains(candidates, value)) return;
        checkCandidateAndRemove();
        candidates.put(key, new CacheObject(value));
        logger.info("Object {} is added or simply accessed in candidates for cache", value);
    }

    // remove least frequently used object from candidates
    private void checkCandidateAndRemove() {
        if (candidates.size() == capacity) {
            // In turn compare frequencies to find the lfu and its id in the cache

            int minFrequency = Integer.MAX_VALUE;
            K id = null;
            CacheObject lfu = null;

            for (Map.Entry<K, CacheObject> entry : candidates.entrySet()) {
                CacheObject current = entry.getValue();

                if (minFrequency >= current.getFrequency()) {
                    minFrequency = current.getFrequency();
                    id = entry.getKey();
                    lfu = current;
                } else {
                    maxCandidateFrequency = current.getFrequency();
                }
            }
            if (id != null) {
                candidates.remove(id, lfu);
            }
        }
    }

    // check whether most frequently used candidate has a bigger frequency than lfu in the cache
    private void findBestCandidateAndChangeCaches() {
        K id = null;
        CacheObject lfu = null;

        for (Map.Entry<K, CacheObject> entry : cache.entrySet()) {
            CacheObject current = entry.getValue();

            if (maxCandidateFrequency > current.getFrequency()) {
                id = entry.getKey();
                lfu = current;
            }
        }
        if (id != null) {
            checkAndChangeCaches(id, lfu);
        }
    }

    // find candidate with maxCandidateFrequency
    private void checkAndChangeCaches(K lfuId, CacheObject lfuValue) {
        Optional<Map.Entry<K, CacheObject>> candidate = candidates.entrySet().stream()
                .filter(item -> item.getValue().getFrequency() == maxCandidateFrequency)
                .findFirst();
        candidate.ifPresent(can -> changeCaches(lfuId, lfuValue, can));
    }

    // move lfu to the candidates; move mfu candidate to the cache
    private void changeCaches(K lfuId, CacheObject lfuValue, Map.Entry<K, CacheObject> candidate) {
        cache.remove(lfuId, lfuValue);
        cache.put(candidate.getKey(), candidate.getValue());

        candidates.remove(candidate.getKey(), candidate.getValue());
        candidates.put(lfuId, lfuValue);
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
