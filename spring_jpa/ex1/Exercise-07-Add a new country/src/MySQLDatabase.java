import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

/**
 * MySQLDatabase
 * --------------
 * A minimal in-memory stand-in for a real MySQL 8.0 database + JDBC driver.
 *
 * Since this project intentionally has no external jars (no mysql-connector-j,
 * no Maven), we cannot open a real java.sql.Connection here. Instead this
 * class plays the same role: it stores rows per "table" (keyed by table
 * name), and every operation prints the equivalent SQL statement to the
 * console - exactly like the real
 *   logging.level.org.hibernate.SQL=trace
 * configuration would do against a live MySQL instance.
 *
 * The accompanying sql/schema.sql (or sql/*.sql) files in each exercise show
 * the exact DDL/DML you would run in MySQL Workbench / mysql client to get
 * the same schema and data in a real database.
 */
public class MySQLDatabase {

    // tableName -> (primaryKey -> row(columnName -> value))
    private static final Map<String, LinkedHashMap<Object, Map<String, Object>>> TABLES = new ConcurrentHashMap<>();

    private static LinkedHashMap<Object, Map<String, Object>> table(String tableName) {
        return TABLES.computeIfAbsent(tableName, t -> new LinkedHashMap<>());
    }

    /** Simulates: INSERT INTO <table> (...) VALUES (...) / ON DUPLICATE KEY UPDATE */
    public static void save(String tableName, Object pkValue, Map<String, Object> row) {
        LinkedHashMap<Object, Map<String, Object>> t = table(tableName);
        boolean exists = t.containsKey(pkValue);
        System.out.println("Hibernate: " + (exists ? buildUpdateSql(tableName, row) : buildInsertSql(tableName, row)));
        t.put(pkValue, new LinkedHashMap<>(row));
    }

    /** Simulates: SELECT * FROM <table> */
    public static List<Map<String, Object>> findAll(String tableName) {
        System.out.println("Hibernate: select * from " + tableName);
        return new ArrayList<>(table(tableName).values());
    }

    /** Simulates: SELECT * FROM <table> WHERE <pk_column> = ? */
    public static Map<String, Object> findById(String tableName, Object pkValue) {
        System.out.println("Hibernate: select * from " + tableName + " where id=? [pk=" + pkValue + "]");
        Map<String, Object> row = table(tableName).get(pkValue);
        return row == null ? null : new LinkedHashMap<>(row);
    }

    /** Simulates: DELETE FROM <table> WHERE <pk_column> = ? */
    public static boolean deleteById(String tableName, Object pkValue) {
        System.out.println("Hibernate: delete from " + tableName + " where id=? [pk=" + pkValue + "]");
        return table(tableName).remove(pkValue) != null;
    }

    /** Clears a table - handy for tests / ddl-auto=create simulation. */
    public static void clearTable(String tableName) {
        table(tableName).clear();
    }

    private static String buildInsertSql(String tableName, Map<String, Object> row) {
        StringBuilder cols = new StringBuilder();
        StringBuilder vals = new StringBuilder();
        for (Map.Entry<String, Object> e : row.entrySet()) {
            if (cols.length() > 0) {
                cols.append(", ");
                vals.append(", ");
            }
            cols.append(e.getKey());
            vals.append("'").append(e.getValue()).append("'");
        }
        return "insert into " + tableName + " (" + cols + ") values (" + vals + ")";
    }

    private static String buildUpdateSql(String tableName, Map<String, Object> row) {
        StringBuilder sets = new StringBuilder();
        for (Map.Entry<String, Object> e : row.entrySet()) {
            if (sets.length() > 0) sets.append(", ");
            sets.append(e.getKey()).append("='").append(e.getValue()).append("'");
        }
        return "update " + tableName + " set " + sets + " where id=?";
    }
}
