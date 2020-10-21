import cache.LfuCache;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class LfuCacheTest extends BaseCacheTest {
    @BeforeEach
    public void setUp() {
        cache = new LfuCache<>(3);
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
        cache.put("now", now);
        assertEquals(3, cache.size());
        assertTrue(cache.getCacheItemsWithKeys().contains(Map.entry("1", "abc")));
        assertTrue(cache.getCacheItemsWithKeys().contains(Map.entry("2", 2L)));
        assertTrue(cache.getCacheItemsWithKeys().contains(Map.entry("now", now)));
        assertFalse(cache.getCacheItemsWithKeys().contains(Map.entry("4", ob)));
    }

    @Test
    void testPut_2() {
        cache.put("null", null);
        cache.put("null", null);
        assertEquals(1, cache.size());
    }

    @Test
    void testPut_3() {
        cache.put("1", "abc");
        cache.put("1", "abc");
        cache.put("2", 2L);
        cache.put("2", 2L);
        cache.put("1", "abc");
        cache.put("2", 2L);
        Object ob = new Object();
        cache.put("4", ob);
        LocalDate now = LocalDate.now();
        cache.put("now", now);
        cache.put("now", now);
        assertEquals(3, cache.size());
        assertTrue(cache.getCacheItemsWithKeys().contains(Map.entry("1", "abc")));
        assertTrue(cache.getCacheItemsWithKeys().contains(Map.entry("2", 2L)));
        assertTrue(cache.getCacheItemsWithKeys().contains(Map.entry("now", now)));
        assertFalse(cache.getCacheItemsWithKeys().contains(Map.entry("4", ob)));

        cache.put("4", ob);
        cache.put("4", ob);
        assertTrue(cache.getCacheItemsWithKeys().contains(Map.entry("4", ob)));
        assertFalse(cache.getCacheItemsWithKeys().contains(Map.entry("now", now)));
    }

    @Test
    void testGet() {
        cache.put("1", "abc");
        cache.put("2", 2L);
        assertEquals(2L, cache.get("2"));
        assertNull(cache.get("4"));
    }
}
