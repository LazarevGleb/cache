import cache.LruCache;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class LruCacheTest extends BaseCacheTest {
    @BeforeEach
    public void setUp() {
        cache = new LruCache(3);
    }

    @Test
    public void testAdd_1() {
        cache.add("abc");
        cache.add(1f);
        cache.add(1f);
        cache.add(3);
        cache.add("abc");
        cache.add(new Object());
        cache.add(LocalDate.now());
        assertEquals(3, cache.size());
    }

    @Test
    public void testAdd_2() {
        cache.add(2f);
        cache.add(2f);
        assertEquals(1, cache.size());
    }

    @Test
    public void testContain() {
        cache.add("abc");
        cache.add(1f);
        assertTrue(cache.contains(1f));
    }

    @Test
    public void testNotContain() {
        cache.add("abc");
        cache.add(1f);
        assertFalse(cache.contains(2f));
    }
}
