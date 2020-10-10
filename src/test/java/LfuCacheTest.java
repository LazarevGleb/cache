import cache.LfuCache;
import cache.LruCache;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class LfuCacheTest {
    private LfuCache cache;

    @BeforeEach
    public void setUp() {
        cache = new LfuCache(3);
    }

    @AfterEach
    public void destroy() {
        cache.clear();
    }

    @Test
    public void testAdd_1() {
        cache.add("abc");
        cache.add(1f);
        cache.add(1f);
        cache.add(3);
        cache.add("abc");
        Object o = new Object();
        cache.add(o);
        LocalDate now = LocalDate.now();
        cache.add(now);
        assertTrue(cache.contains(1f));
        assertTrue(cache.contains("abc"));
        assertTrue(cache.contains(now));
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

    @Test
    public void testGetCacheItems() {
        cache.add("abc");
        cache.add(1f);
        cache.add(LocalDate.now());
        assertEquals(3, cache.getCacheItems().size());
    }

    @Test
    public void testClear() {
        cache.add("abc");
        cache.add(1f);
        cache.clear();
        assertEquals(0, cache.getCacheItems().size());
    }

    @Test
    public void testConstructorException() {
        assertThrows(IllegalArgumentException.class, () -> new LruCache(-1));
    }

}
