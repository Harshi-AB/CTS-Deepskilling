/**
 * Custom re-implementation of org.hibernate.SessionFactory.
 *
 * A heavyweight, thread-safe object built once per application (or per
 * database) that knows how to open lightweight Session objects. Internally
 * it holds the parsed connection settings (from hibernate.cfg.xml) and the
 * entity mapping (from Employee.hbm.xml).
 */
public class SessionFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(SessionFactory.class);

    private final ConnectionConfig connectionConfig;
    private final EntityMapping mapping;

    SessionFactory(ConnectionConfig connectionConfig, EntityMapping mapping) {
        this.connectionConfig = connectionConfig;
        this.mapping = mapping;
        LOGGER.info("Building SessionFactory for dialect={}", connectionConfig.getDialect());
        LOGGER.info("Connecting to url={} as user={}", connectionConfig.getUrl(), connectionConfig.getUsername());
        LOGGER.info("Mapped entity {} -> table {}", mapping.getClassName(), mapping.getTableName());
    }

    public Session openSession() {
        LOGGER.debug("Opening new Hibernate Session");
        return new Session(mapping);
    }

    public void close() {
        LOGGER.info("SessionFactory closed");
    }
}
