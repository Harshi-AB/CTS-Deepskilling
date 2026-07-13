import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * SimpleJpaRepository.java
 *
 * Exercise 04 - Implementing CRUD Operations
 * ---------------------------------------------
 * This is the hand-rolled equivalent of Spring Data JPA's real
 * org.springframework.data.jpa.repository.support.SimpleJpaRepository:
 * ONE generic class that implements save/find/delete/count for ANY
 * annotated entity, purely through reflection - so individual
 * repositories (EmployeeRepository, DepartmentRepository, ...) never
 * need hand-written implementation classes.
 *
 * Design pattern: Generic DAO + Template Method (the SQL shape is fixed,
 * only the entity metadata varies per call).
 *
 * @param <T>  entity type
 * @param <ID> primary key type
 */
public class SimpleJpaRepository<T, ID extends java.io.Serializable> implements JpaRepository<T, ID> {

    private final Class<T> entityClass;
    private final String tableName;
    private final Field idField;
    private final String idColumn;

    public SimpleJpaRepository(Class<T> entityClass) {
        this.entityClass = entityClass;
        this.tableName = EntityMetadataUtil.getTableName(entityClass);
        this.idField = EntityMetadataUtil.getIdField(entityClass);
        this.idColumn = EntityMetadataUtil.getColumnName(idField);
    }

    // =====================================================================
    // CREATE / UPDATE
    // =====================================================================
    @Override
    @SuppressWarnings("unchecked")
    public <S extends T> S save(S entity) {
        try {
            Object idValue = idField.get(entity);
            boolean isNew = idValue == null || (idValue instanceof Integer && (Integer) idValue == 0);
            return isNew ? (S) insert(entity) : (S) update(entity);
        } catch (Exception e) {
            throw new RuntimeException("save() failed for " + entityClass.getSimpleName(), e);
        }
    }

    private T insert(T entity) throws Exception {
        List<Field> fields = persistableNonIdFields();
        StringBuilder cols = new StringBuilder();
        StringBuilder placeholders = new StringBuilder();
        for (Field f : fields) {
            cols.append(columnFor(f)).append(",");
            placeholders.append("?,");
        }
        cols.setLength(cols.length() - 1);
        placeholders.setLength(placeholders.length() - 1);

        String sql = "INSERT INTO " + tableName + " (" + cols + ") VALUES (" + placeholders + ")";

        try (Connection conn = DBConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            bindParameters(ps, entity, fields);
            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    idField.set(entity, keys.getInt(1));
                }
            }
        }
        return entity;
    }

    private T update(T entity) throws Exception {
        List<Field> fields = persistableNonIdFields();
        StringBuilder setClause = new StringBuilder();
        for (Field f : fields) {
            setClause.append(columnFor(f)).append(" = ?,");
        }
        setClause.setLength(setClause.length() - 1);

        String sql = "UPDATE " + tableName + " SET " + setClause + " WHERE " + idColumn + " = ?";

        try (Connection conn = DBConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            int index = bindParameters(ps, entity, fields);
            ps.setObject(index, idField.get(entity));
            ps.executeUpdate();
        }
        return entity;
    }

    // =====================================================================
    // READ
    // =====================================================================
    @Override
    public Optional<T> findById(ID id) {
        String sql = "SELECT * FROM " + tableName + " WHERE " + idColumn + " = ?";
        try (Connection conn = DBConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setObject(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("findById() failed", e);
        }
        return Optional.empty();
    }

    @Override
    public boolean existsById(ID id) {
        return findById(id).isPresent();
    }

    @Override
    public List<T> findAll() {
        List<T> results = new ArrayList<>();
        String sql = "SELECT * FROM " + tableName;
        try (Connection conn = DBConnectionUtil.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                results.add(mapRow(rs));
            }
        } catch (Exception e) {
            throw new RuntimeException("findAll() failed", e);
        }
        return results;
    }

    @Override
    public List<T> findAllById(Iterable<ID> ids) {
        List<T> results = new ArrayList<>();
        for (ID id : ids) {
            findById(id).ifPresent(results::add);
        }
        return results;
    }

    @Override
    public long count() {
        String sql = "SELECT COUNT(*) FROM " + tableName;
        try (Connection conn = DBConnectionUtil.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            return rs.next() ? rs.getLong(1) : 0L;
        } catch (Exception e) {
            throw new RuntimeException("count() failed", e);
        }
    }

    // =====================================================================
    // DELETE
    // =====================================================================
    @Override
    public void deleteById(ID id) {
        String sql = "DELETE FROM " + tableName + " WHERE " + idColumn + " = ?";
        try (Connection conn = DBConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setObject(1, id);
            ps.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException("deleteById() failed", e);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void delete(T entity) {
        try {
            deleteById((ID) idField.get(entity));
        } catch (IllegalAccessException e) {
            throw new RuntimeException("delete() failed", e);
        }
    }

    @Override
    public void deleteAll() {
        String sql = "DELETE FROM " + tableName;
        try (Connection conn = DBConnectionUtil.getConnection();
             Statement st = conn.createStatement()) {
            st.executeUpdate(sql);
        } catch (Exception e) {
            throw new RuntimeException("deleteAll() failed", e);
        }
    }

    @Override
    public void deleteAllInBatch() {
        deleteAll();
    }

    // =====================================================================
    // JPA extras
    // =====================================================================
    @Override
    public void flush() {
        // No persistence-context / dirty-checking layer exists in this simplified
        // engine (every save() already commits immediately via try-with-resources
        // auto-commit connections), so flush() is intentionally a no-op - kept
        // only to satisfy the JpaRepository contract.
    }

    @Override
    public <S extends T> S saveAndFlush(S entity) {
        S saved = save(entity);
        flush();
        return saved;
    }

    // =====================================================================
    // Internal reflection helpers
    // =====================================================================
    private List<Field> persistableNonIdFields() {
        List<Field> fields = new ArrayList<>();
        for (Field f : entityClass.getDeclaredFields()) {
            f.setAccessible(true);
            if (f.equals(idField) || !EntityMetadataUtil.isPersistable(f)) {
                continue;
            }
            fields.add(f);
        }
        return fields;
    }

    private String columnFor(Field f) {
        if (f.isAnnotationPresent(ManyToOne.class)) {
            return f.getAnnotation(JoinColumn.class).name();
        }
        return EntityMetadataUtil.getColumnName(f);
    }

    private int bindParameters(PreparedStatement ps, T entity, List<Field> fields) throws Exception {
        int index = 1;
        for (Field f : fields) {
            Object value = f.get(entity);
            if (f.isAnnotationPresent(ManyToOne.class) && value != null) {
                // Store only the FK id of the related entity (e.g. Department.deptId)
                Field relatedId = EntityMetadataUtil.getIdField(value.getClass());
                ps.setObject(index++, relatedId.get(value));
            } else {
                ps.setObject(index++, value);
            }
        }
        return index;
    }

    /**
     * Maps one ResultSet row back into a fully populated entity instance using
     * reflection - the read-side counterpart of bindParameters(). Handles the
     * common column types used across this project (int, String, BigDecimal,
     * LocalDate, boolean) plus a minimal @ManyToOne resolution.
     */
    @SuppressWarnings("unchecked")
    protected T mapRow(ResultSet rs) throws Exception {
        T entity = entityClass.getDeclaredConstructor().newInstance();

        for (Field f : entityClass.getDeclaredFields()) {
            f.setAccessible(true);

            if (f.isAnnotationPresent(Id.class)) {
                f.set(entity, rs.getInt(EntityMetadataUtil.getColumnName(f)));
            } else if (f.isAnnotationPresent(Column.class)) {
                setSimpleColumn(rs, entity, f);
            } else if (f.isAnnotationPresent(ManyToOne.class)) {
                String joinCol = f.getAnnotation(JoinColumn.class).name();
                int relatedId = rs.getInt(joinCol);
                if (!rs.wasNull()) {
                    // Minimal lazy-load simulation: fetch just the id-populated
                    // related entity. A full implementation would delegate to
                    // that entity's own SimpleJpaRepository.
                    Object related = f.getType().getDeclaredConstructor().newInstance();
                    Field relatedIdField = EntityMetadataUtil.getIdField(f.getType());
                    relatedIdField.set(related, relatedId);
                    f.set(entity, related);
                }
            }
        }
        return entity;
    }

    private void setSimpleColumn(ResultSet rs, T entity, Field f) throws Exception {
        String col = EntityMetadataUtil.getColumnName(f);
        Class<?> type = f.getType();

        if (type == int.class || type == Integer.class) {
            f.set(entity, rs.getInt(col));
        } else if (type == boolean.class || type == Boolean.class) {
            f.set(entity, rs.getBoolean(col));
        } else if (type == BigDecimal.class) {
            f.set(entity, rs.getBigDecimal(col));
        } else if (type == LocalDate.class) {
            java.sql.Date d = rs.getDate(col);
            f.set(entity, d == null ? null : d.toLocalDate());
        } else if (type == String.class) {
            f.set(entity, rs.getString(col));
        } else if (type == long.class || type == Long.class) {
            f.set(entity, rs.getLong(col));
        } else {
            f.set(entity, rs.getObject(col));
        }
    }
}
