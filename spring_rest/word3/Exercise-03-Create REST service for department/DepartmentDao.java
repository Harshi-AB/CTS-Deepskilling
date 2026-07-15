import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;

/**
 * DepartmentDao.java
 *
 * Data Access Object layer for departments.
 *
 * Requirements covered:
 *  - getAllDepartments() method
 *  - Static variable DEPARTMENT_LIST, populated from spring xml
 *    configuration (department.xml), read using core Java's built in
 *    DOM parser (javax.xml.parsers / org.w3c.dom).
 */
@Component
public class DepartmentDao {

    // Static variable populated from spring xml configuration (department.xml)
    private static ArrayList<Department> DEPARTMENT_LIST = new ArrayList<>();

    /**
     * Constructor reads the department list from the xml configuration
     * file and populates DEPARTMENT_LIST.
     */
    public DepartmentDao(String xmlConfigPath) {
        try {
            loadFromXml(xmlConfigPath);
        } catch (Exception e) {
            throw new RuntimeException("Unable to load department configuration from "
                    + xmlConfigPath, e);
        }
    }

    /**
     * getAllDepartments() - returns the static DEPARTMENT_LIST.
     */
    public ArrayList<Department> getAllDepartments() {
        return DEPARTMENT_LIST;
    }

    private void loadFromXml(String xmlConfigPath) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(new File(xmlConfigPath));
        document.getDocumentElement().normalize();

        NodeList departmentNodes = document.getElementsByTagName("department");
        for (int i = 0; i < departmentNodes.getLength(); i++) {
            Element departmentElement = (Element) departmentNodes.item(i);
            int id = Integer.parseInt(getTagValue(departmentElement, "id"));
            String name = getTagValue(departmentElement, "name");
            DEPARTMENT_LIST.add(new Department(id, name));
        }
    }

    private String getTagValue(Element parent, String tagName) {
        NodeList nodeList = parent.getElementsByTagName(tagName);
        if (nodeList.getLength() == 0) {
            return null;
        }
        return nodeList.item(0).getTextContent() == null
                ? null
                : nodeList.item(0).getTextContent().trim();
    }
}
