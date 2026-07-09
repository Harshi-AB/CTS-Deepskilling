import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

/**
 * HibernateMappingReader
 * ----------------------
 * Reads an *.hbm.xml mapping file using plain DOM parsing and builds an
 * EntityMapping describing table name, id property/column, generator
 * strategy, and every other mapped property/column pair.
 */
public class HibernateMappingReader {

    public static EntityMapping parse(File hbmFile) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(hbmFile);
        document.getDocumentElement().normalize();

        EntityMapping mapping = new EntityMapping();

        Element classElement = (Element) document.getElementsByTagName("class").item(0);
        mapping.setClassName(classElement.getAttribute("name"));
        mapping.setTableName(classElement.getAttribute("table"));

        NodeList children = classElement.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node node = children.item(i);
            if (node.getNodeType() != Node.ELEMENT_NODE) continue;
            Element element = (Element) node;

            if ("id".equals(element.getTagName())) {
                mapping.setIdPropertyName(element.getAttribute("name"));
                mapping.setIdColumnName(element.getAttribute("column"));
                NodeList generators = element.getElementsByTagName("generator");
                if (generators.getLength() > 0) {
                    mapping.setGeneratorClass(((Element) generators.item(0)).getAttribute("class"));
                }
            } else if ("property".equals(element.getTagName())) {
                mapping.addProperty(element.getAttribute("name"), element.getAttribute("column"));
            }
        }

        return mapping;
    }
}
