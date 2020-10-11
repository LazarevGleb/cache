import cache.Cache;
import cache.LruCache;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BaseCacheTest {
    protected Cache cache;

    @AfterEach
    public void destroy() {
        cache.clear();
    }

    @Test
    void testGetCacheItems() {
        cache.add("abc");
        cache.add(1f);
        cache.add(LocalDate.now());
        assertEquals(3, cache.getCacheItems().size());
    }

    @Test
    void testClear() {
        cache.add("abc");
        cache.add(1f);
        cache.clear();
        assertEquals(0, cache.getCacheItems().size());
    }

    @Test
    void testConstructorException() {
        assertThrows(IllegalArgumentException.class, () -> new LruCache(-1));
    }
}
