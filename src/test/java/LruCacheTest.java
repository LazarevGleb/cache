import cache.LruCache;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class LruCacheTest extends BaseCacheTest {
    @BeforeEach
    public void setUp() {
        cache = new LruCache<>(3);
    }

    @Test
    void testPut_1() {
        cache.put("1", "abc");
        cache.put("2", 2L);
        cache.put("1", "abc");
        cache.put("2", 2L);
        cache.put("abc", true);
        Object ob = new Object();
        cache.put("4", ob);
        LocalDate now = LocalDate.now();
        cache.put("now", now);
        assertEquals(3, cache.size());
        assertTrue(cache.getCacheItemsWithKeys().contains(Map.entry("now", now)));
        assertTrue(cache.getCacheItemsWithKeys().contains(Map.entry("4", ob)));
        assertTrue(cache.getCacheItemsWithKeys().contains(Map.entry("abc", true)));
        assertFalse(cache.getCacheItemsWithKeys().contains(Map.entry("2", 2L)));

    }

    @Test
    void testPut_2() {
        cache.put("2", 2L);
        cache.put("2", 2L);
        assertEquals(1, cache.size());
    }

    @Test
    void testGet() {
        cache.put("1", "abc");
        cache.put("2", 2L);
        assertEquals(2L, cache.get("2"));
        assertNull(cache.get("4"));
    }
}
