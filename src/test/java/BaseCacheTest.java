import cache.Cache;
import cache.LruCache;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BaseCacheTest {
    protected Cache<String> cache;

    @AfterEach
    public void destroy() {
        cache.clear();
    }

    @Test
    void testSize() {
        cache.put("1", "abc");
        cache.put("1", "abc");
        cache.put("2", LocalDate.now());
        assertEquals(2, cache.getCacheItems().size());
    }

    @Test
    void testClear() {
        cache.put("1", "abc");
        cache.put("2", 2L);
        cache.clear();
        assertEquals(0, cache.getCacheItems().size());
    }

    @Test
    void testConstructorException() {
        assertThrows(IllegalArgumentException.class, () -> new LruCache(-1));
    }
}
