package cache;

/**
 * Item for LfuCache
 */
class CacheObject {
    private int frequency;
    private final Object item;

    public CacheObject(Object item) {
        this.item = item;
        this.frequency = 1;
    }

    public Object getItem() {
        return item;
    }

    public int getFrequency() {
        return frequency;
    }

    public void incrementFrequency() {
        frequency++;
    }
}
