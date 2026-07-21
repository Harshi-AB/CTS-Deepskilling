import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * RateLimiter implements the classic Token Bucket algorithm, the same
 * strategy Spring Cloud Gateway's RequestRateLimiter filter uses.
 *
 * Each client (identified here by a simple client key, e.g. an IP
 * address or API key) gets its own bucket holding up to
 * {@code capacity} tokens. Every request consumes one token; the
 * bucket refills at {@code refillRatePerSecond} tokens per second.
 * When a bucket is empty, further requests are rejected (HTTP 429)
 * until it refills.
 */
public class RateLimiter {

    /** Per-client bucket state: remaining tokens and the last refill timestamp. */
    private static class Bucket {
        double tokens;
        long lastRefillTimestampMillis;

        Bucket(double tokens, long lastRefillTimestampMillis) {
            this.tokens = tokens;
            this.lastRefillTimestampMillis = lastRefillTimestampMillis;
        }
    }

    private final int capacity;
    private final double refillRatePerSecond;
    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();

    public RateLimiter(int capacity, double refillRatePerSecond) {
        this.capacity = capacity;
        this.refillRatePerSecond = refillRatePerSecond;
    }

    /**
     * Attempts to consume one token for the given client key.
     * Returns true if the request is allowed, false if it should be
     * rejected because the client's bucket is currently empty.
     */
    public synchronized boolean tryConsume(String clientKey) {
        long now = System.currentTimeMillis();
        Bucket bucket = buckets.computeIfAbsent(clientKey, k -> new Bucket(capacity, now));

        // Refill tokens based on elapsed time since the last request
        double elapsedSeconds = (now - bucket.lastRefillTimestampMillis) / 1000.0;
        double refill = elapsedSeconds * refillRatePerSecond;
        bucket.tokens = Math.min(capacity, bucket.tokens + refill);
        bucket.lastRefillTimestampMillis = now;

        if (bucket.tokens >= 1.0) {
            bucket.tokens -= 1.0;
            return true;
        }
        return false;
    }

    /** Returns the number of tokens currently available for a client (for diagnostics). */
    public synchronized double getAvailableTokens(String clientKey) {
        Bucket bucket = buckets.get(clientKey);
        return bucket == null ? capacity : bucket.tokens;
    }
}
