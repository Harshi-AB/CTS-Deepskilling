import java.io.File;

/**
 * Custom re-implementation of org.hibernate.cfg.AnnotationConfiguration
 * (the classic tutorialspoint.com API - see
 * https://www.tutorialspoint.com/hibernate/hibernate_annotations.htm).
 *
 * Real usage:
 *   AnnotationConfiguration configuration = new AnnotationConfiguration();
 *   configuration.configure();
 *   configuration.addAnnotatedClass(Employee.class);
 *   SessionFactory factory = configuration.buildSessionFactory();
 *
 * Unlike Exercise 2's Configuration, this reads ONLY the connection
 * settings (dialect/driver/url/username/password) from hibernate.cfg.xml -
 * there is no <mapping resource=".../> entry, because the mapping comes
 * from the @Entity/@Table/@Id/@Column annotations on the class passed to
 * addAnnotatedClass().
 */
public class AnnotationConfiguration {

    private ConnectionConfig connectionConfig;
    private EntityMapping mapping;

    public AnnotationConfiguration configure() throws Exception {
        return configure("config/hibernate.cfg.xml");
    }

    public AnnotationConfiguration configure(String cfgPath) throws Exception {
        this.connectionConfig = HibernateXmlConfigReader.parse(new File(cfgPath));
        return this;
    }

    public AnnotationConfiguration addAnnotatedClass(Class<?> entityClass) {
        this.mapping = AnnotationEntityScanner.scan(entityClass);
        return this;
    }

    public SessionFactory buildSessionFactory() {
        return new SessionFactory(connectionConfig, mapping);
    }
}
