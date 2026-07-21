import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ResponseCache is a small in-memory cache with a time-to-live (TTL),
 * mirroring the caching behaviour that Spring Cloud Gateway can perform
 * (or that would sit behind it, e.g. via a LocalResponseCache filter).
 *
 * Cached entries automatically expire after {@code ttlMillis}
 * milliseconds; a request for an expired (or missing) key is treated
 * as a cache miss.
 */
public class ResponseCache {

    private static class CacheEntry {
        final String value;
        final long expiryTimestampMillis;

        CacheEntry(String value, long expiryTimestampMillis) {
            this.value = value;
            this.expiryTimestampMillis = expiryTimestampMillis;
        }

        boolean isExpired() {
            return System.currentTimeMillis() > expiryTimestampMillis;
        }
    }

    private final long ttlMillis;
    private final Map<String, CacheEntry> store = new ConcurrentHashMap<>();

    public ResponseCache(long ttlMillis) {
        this.ttlMillis = ttlMillis;
    }

    /** Returns the cached value for a key, or null on a miss/expired entry. */
    public String get(String key) {
        CacheEntry entry = store.get(key);
        if (entry == null) {
            return null;
        }
        if (entry.isExpired()) {
            store.remove(key);
            return null;
        }
        return entry.value;
    }

    /** Stores a value under the given key with the configured TTL. */
    public void put(String key, String value) {
        store.put(key, new CacheEntry(value, System.currentTimeMillis() + ttlMillis));
    }

    /** Clears every cached entry (useful for tests/demos). */
    public void clear() {
        store.clear();
    }
}
