import java.util.concurrent.ConcurrentHashMap;

/**
 * SecondLevelCache.java
 *
 * Exercise 10 - Hibernate-Specific Features
 * -----------------------------------------------------
 * Custom stand-in for Hibernate's SECOND-LEVEL CACHE (the optional cache
 * region that survives across Sessions/EntityManagers, backed in real
 * Hibernate by EhCache/Infinispan/etc). Here it's just one static,
 * process-wide ConcurrentHashMap shared by every Session instance -
 * enough to demonstrate the concept: a fresh Session can still get a
 * cache hit if some earlier Session already loaded that row.
 *
 * Design pattern: Singleton (one shared cache region for the whole JVM).
 */
public class SecondLevelCache {

    private static final ConcurrentHashMap<String, Object> CACHE = new ConcurrentHashMap<>();

    private SecondLevelCache() { }

    public static String key(Class<?> type, Object id) {
        return type.getName() + "#" + id;
    }

    public static Object get(Class<?> type, Object id) {
        return CACHE.get(key(type, id));
    }

    public static void put(Class<?> type, Object id, Object entity) {
        CACHE.put(key(type, id), entity);
    }

    public static void evict(Class<?> type, Object id) {
        CACHE.remove(key(type, id));
    }

    public static int size() {
        return CACHE.size();
    }
}
