/*
 * JPAUtil.java
 * ------------
 * Utility class responsible for bootstrapping a single, shared
 * EntityManagerFactory for the whole application.
 *
 * Design pattern: SINGLETON - exactly one EntityManagerFactory is created
 * (it is an expensive, thread-safe object meant to live for the lifetime
 * of the application) and reused by every repository class.
 */
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public final class JPAUtil {

    // Name must match the <persistence-unit name="..."> in META-INF/persistence.xml
    private static final String PERSISTENCE_UNIT_NAME = "ormlearn-unit";

    // Eagerly initialised single instance (thread-safe, simple singleton)
    private static final EntityManagerFactory ENTITY_MANAGER_FACTORY =
            Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);

    // Private constructor prevents instantiation - utility class
    private JPAUtil() {
    }

    public static EntityManager getEntityManager() {
        return ENTITY_MANAGER_FACTORY.createEntityManager();
    }

    public static void shutdown() {
        if (ENTITY_MANAGER_FACTORY.isOpen()) {
            ENTITY_MANAGER_FACTORY.close();
        }
    }
}
