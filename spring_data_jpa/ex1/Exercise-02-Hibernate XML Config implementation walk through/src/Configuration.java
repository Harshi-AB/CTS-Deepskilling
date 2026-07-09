import java.io.File;

/**
 * Custom re-implementation of org.hibernate.cfg.Configuration.
 *
 * Real usage:  new Configuration().configure().buildSessionFactory();
 * configure() loads hibernate.cfg.xml (and the mapping file it references);
 * buildSessionFactory() hands back a ready-to-use SessionFactory.
 */
public class Configuration {

    private ConnectionConfig connectionConfig;
    private EntityMapping mapping;

    /** Loads config/hibernate.cfg.xml and the Employee.hbm.xml it references. */
    public Configuration configure() throws Exception {
        return configure("config/hibernate.cfg.xml");
    }

    public Configuration configure(String cfgPath) throws Exception {
        File cfgFile = new File(cfgPath);
        this.connectionConfig = HibernateXmlConfigReader.parse(cfgFile);

        File mappingFile = new File(cfgFile.getParentFile(), connectionConfig.getMappingResource());
        this.mapping = HibernateMappingReader.parse(mappingFile);
        return this;
    }

    public SessionFactory buildSessionFactory() {
        return new SessionFactory(connectionConfig, mapping);
    }
}
