import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Session.java
 *
 * Exercise 10 - Hibernate-Specific Features
 * -----------------------------------------------------
 * A small stand-in for org.hibernate.Session / a JPA EntityManager's
 * persistence context. Bundles together the two caching tiers plus
 * Hibernate's signature DIRTY CHECKING behaviour:
 *
 *   1) FIRST-LEVEL CACHE  - an identity map scoped to this Session only.
 *      Cleared automatically when the Session is closed (try-with-resources).
 *
 *   2) SECOND-LEVEL CACHE - delegates to the shared, cross-Session
 *      SecondLevelCache so a brand-new Session can still avoid hitting
 *      the database if another Session already loaded that row.
 *
 *   3) DIRTY CHECKING / @DynamicUpdate - a snapshot of every loaded
 *      entity's field values is kept. update() diffs the current entity
 *      state against that snapshot and issues an UPDATE that touches
 *      ONLY the columns that actually changed, instead of rewriting
 *      every column every time.
 *
 * Design pattern: Unit of Work (Session tracks everything loaded/changed
 * during one logical interaction and reconciles it with the database).
 */
public class Session implements AutoCloseable {

    private final Map<String, Object> firstLevelCache = new HashMap<>();
    private final Map<String, Map<String, Object>> snapshots = new HashMap<>();

    /**
     * Loads one entity by id, checking L1 cache, then L2 cache, then finally
     * the database - printing which tier served the request for visibility.
     */
    @SuppressWarnings("unchecked")
    public <T> Optional<T> load(Class<T> type, Object id) {
        String key = SecondLevelCache.key(type, id);

        if (firstLevelCache.containsKey(key)) {
            System.out.println("  [L1 cache HIT]  " + key);
            return Optional.of((T) firstLevelCache.get(key));
        }

        Object l2Hit = SecondLevelCache.get(type, id);
        if (l2Hit != null) {
            System.out.println("  [L2 cache HIT]  " + key);
            firstLevelCache.put(key, l2Hit);
            takeSnapshot(key, l2Hit);
            return Optional.of((T) l2Hit);
        }

        System.out.println("  [CACHE MISS -> querying MySQL]  " + key);
        SimpleJpaRepository<T, java.io.Serializable> repo = new SimpleJpaRepository<>(type);
        Optional<T> result = repo.findById((java.io.Serializable) id);
        result.ifPresent(entity -> {
            firstLevelCache.put(key, entity);
            SecondLevelCache.put(type, id, entity);
            takeSnapshot(key, entity);
        });
        return result;
    }

    /**
     * Hibernate-style dirty-checking UPDATE: compares the entity's current
     * field values against the snapshot captured at load() time and writes
     * only the columns that changed (mirrors Hibernate's @DynamicUpdate).
     */
    public void update(Object entity) {
        try {
            Field idField = EntityMetadataUtil.getIdField(entity.getClass());
            Object id = idField.get(entity);
            String key = SecondLevelCache.key(entity.getClass(), id);
            Map<String, Object> before = snapshots.get(key);
            if (before == null) {
                throw new IllegalStateException("Entity was never loaded through this Session - no snapshot to diff against");
            }

            StringBuilder setClause = new StringBuilder();
            Map<String, Object> changedValues = new HashMap<>();

            for (Field f : entity.getClass().getDeclaredFields()) {
                f.setAccessible(true);
                if (f.equals(idField) || !EntityMetadataUtil.isPersistable(f) || f.isAnnotationPresent(ManyToOne.class)) {
                    continue; // keep the demo focused on simple scalar columns
                }
                Object newValue = f.get(entity);
                Object oldValue = before.get(f.getName());
                if (!java.util.Objects.equals(newValue, oldValue)) {
                    String column = EntityMetadataUtil.getColumnName(f);
                    setClause.append(column).append(" = ?,");
                    changedValues.put(column, newValue);
                }
            }

            if (changedValues.isEmpty()) {
                System.out.println("  [dirty check] no changed fields -> UPDATE skipped entirely");
                return;
            }
            setClause.setLength(setClause.length() - 1);

            String tableName = EntityMetadataUtil.getTableName(entity.getClass());
            String idColumn = EntityMetadataUtil.getColumnName(idField);
            String sql = "UPDATE " + tableName + " SET " + setClause + " WHERE " + idColumn + " = ?";

            System.out.println("  [dynamic update SQL] " + sql + "   changed=" + changedValues.keySet());

            try (Connection conn = DBConnectionUtil.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {
                int idx = 1;
                for (Object v : changedValues.values()) {
                    ps.setObject(idx++, v);
                }
                ps.setObject(idx, id);
                ps.executeUpdate();
            }

            // Refresh the snapshot + both cache tiers so subsequent reads see the update.
            takeSnapshot(key, entity);
            firstLevelCache.put(key, entity);
            SecondLevelCache.put(entity.getClass(), id, entity);

        } catch (Exception e) {
            throw new RuntimeException("Dynamic update failed", e);
        }
    }

    private void takeSnapshot(String key, Object entity) {
        Map<String, Object> values = new HashMap<>();
        for (Field f : entity.getClass().getDeclaredFields()) {
            f.setAccessible(true);
            try {
                values.put(f.getName(), f.get(entity));
            } catch (IllegalAccessException ignored) {
                // Field is not readable - skip it from the dirty-checking snapshot.
            }
        }
        snapshots.put(key, values);
    }

    /** Ends the unit of work: L1 cache and snapshots are discarded (L2 cache is untouched - it's cross-Session). */
    @Override
    public void close() {
        firstLevelCache.clear();
        snapshots.clear();
    }
}
