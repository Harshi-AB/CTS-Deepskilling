import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

/**
 * BatchInsertProcessor.java
 *
 * Exercise 10 - Hibernate-Specific Features
 * -----------------------------------------------------
 * Custom stand-in for Hibernate's hibernate.jdbc.batch_size feature.
 * Instead of opening a new PreparedStatement and round-tripping to MySQL
 * for every single INSERT (what SimpleJpaRepository.save() does one row
 * at a time), this batches many rows onto ONE PreparedStatement via
 * addBatch()/executeBatch(), issuing a single network round trip for the
 * whole batch - the same optimization Hibernate applies automatically
 * when batching is enabled.
 *
 * Design pattern: Template Method (the batching mechanics are generic;
 * only the per-entity field extraction is entity-specific, done via
 * reflection using the same @Column metadata as everywhere else).
 */
public class BatchInsertProcessor<T> {

    private final Class<T> entityClass;
    private final String tableName;
    private final Field idField;
    private final List<Field> insertableFields = new ArrayList<>();

    public BatchInsertProcessor(Class<T> entityClass) {
        this.entityClass = entityClass;
        this.tableName = EntityMetadataUtil.getTableName(entityClass);
        this.idField = EntityMetadataUtil.getIdField(entityClass);
        for (Field f : entityClass.getDeclaredFields()) {
            f.setAccessible(true);
            if (!f.equals(idField) && EntityMetadataUtil.isPersistable(f) && !f.isAnnotationPresent(ManyToOne.class)) {
                insertableFields.add(f);
            }
        }
    }

    /** Inserts every entity in the list using a single batched PreparedStatement. */
    public int[] insertAll(List<T> entities) {
        StringBuilder cols = new StringBuilder();
        StringBuilder placeholders = new StringBuilder();
        for (Field f : insertableFields) {
            cols.append(EntityMetadataUtil.getColumnName(f)).append(",");
            placeholders.append("?,");
        }
        cols.setLength(cols.length() - 1);
        placeholders.setLength(placeholders.length() - 1);

        String sql = "INSERT INTO " + tableName + " (" + cols + ") VALUES (" + placeholders + ")";

        try (Connection conn = DBConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            conn.setAutoCommit(false);
            for (T entity : entities) {
                int idx = 1;
                for (Field f : insertableFields) {
                    ps.setObject(idx++, f.get(entity));
                }
                ps.addBatch();
            }
            int[] results = ps.executeBatch();
            conn.commit();
            conn.setAutoCommit(true);
            return results;

        } catch (Exception e) {
            throw new RuntimeException("Batch insert failed for " + entityClass.getSimpleName(), e);
        }
    }
}
