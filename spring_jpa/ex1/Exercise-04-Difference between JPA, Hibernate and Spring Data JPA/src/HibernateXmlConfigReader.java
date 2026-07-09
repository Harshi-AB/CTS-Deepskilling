import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

/**
 * HibernateXmlConfigReader
 * ------------------------
 * Reads hibernate.cfg.xml using plain DOM parsing (javax.xml.parsers, part
 * of core Java - no external XML library / jar required) and extracts the
 * &lt;property&gt; entries under &lt;session-factory&gt; plus the
 * &lt;mapping resource="..."/&gt; entry.
 */
public class HibernateXmlConfigReader {

    public static ConnectionConfig parse(File cfgFile) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        // Disable external DTD loading so parsing works fully offline
        factory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(cfgFile);
        document.getDocumentElement().normalize();

        ConnectionConfig config = new ConnectionConfig();

        NodeList properties = document.getElementsByTagName("property");
        for (int i = 0; i < properties.getLength(); i++) {
            Element property = (Element) properties.item(i);
            String name = property.getAttribute("name");
            String value = property.getTextContent().trim();
            switch (name) {
                case "connection.driver_class":
                    config.setDriverClass(value);
                    break;
                case "connection.url":
                    config.setUrl(value);
                    break;
                case "connection.username":
                    config.setUsername(value);
                    break;
                case "connection.password":
                    config.setPassword(value);
                    break;
                case "dialect":
                    config.setDialect(value);
                    break;
                case "show_sql":
                    config.setShowSql(Boolean.parseBoolean(value));
                    break;
                case "hbm2ddl.auto":
                    config.setDdlAuto(value);
                    break;
                default:
                    // ignore properties this simulation does not need
                    break;
            }
        }

        NodeList mappings = document.getElementsByTagName("mapping");
        if (mappings.getLength() > 0) {
            Element mapping = (Element) mappings.item(0);
            config.setMappingResource(mapping.getAttribute("resource"));
        }

        return config;
    }
}
