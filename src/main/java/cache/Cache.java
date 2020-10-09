package cache;

import java.util.Set;

public interface Cache {
    void add(Object value);

    boolean contains(Object value);

    void clear();

    long size();

    Set<Object> getCacheItems();
}
