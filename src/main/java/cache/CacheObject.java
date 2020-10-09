package cache;

public class CacheObject {
    private int frequency;
    private Object value;

    public CacheObject(Object value) {
        this.value = value;
        this.frequency = 1;
    }

    public Object getValue() {
        return value;
    }

    public int getFrequency() {
        return frequency;
    }
}
