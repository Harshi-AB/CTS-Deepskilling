import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * EmployeeDao.java
 *
 * Data Access Object layer.
 *
 * Requirements covered:
 *  - Static variable EMPLOYEE_LIST of type ArrayList<Employee>
 *  - Constructor that reads the employee list from the xml config
 *    (employee.xml) and sets EMPLOYEE_LIST
 *  - getAllEmployees() method that returns EMPLOYEE_LIST
 *
 * The XML parsing uses only classes bundled with core Java
 * (javax.xml.parsers / org.w3c.dom), so no external / Spring
 * dependency is required to read "spring xml configuration" style data.
 */
@Component
public class EmployeeDao {

    // Static variable holding the employee list read from employee.xml
    private static ArrayList<Employee> EMPLOYEE_LIST = new ArrayList<>();

    // Static variable holding the department list read from employee.xml
    private static ArrayList<Department> DEPARTMENT_LIST = new ArrayList<>();

    /**
     * Constructor reads the employee list (and its departments) from
     * the xml configuration file and populates EMPLOYEE_LIST.
     */
    public EmployeeDao(String xmlConfigPath) {
        try {
            loadFromXml(xmlConfigPath);
        } catch (Exception e) {
            throw new RuntimeException("Unable to load employee configuration from "
                    + xmlConfigPath, e);
        }
    }

    /**
     * getAllEmployees() - returns the static EMPLOYEE_LIST.
     */
    public ArrayList<Employee> getAllEmployees() {
        return EMPLOYEE_LIST;
    }

    /**
     * Parses employee.xml using the core Java DOM parser and populates
     * both DEPARTMENT_LIST and EMPLOYEE_LIST.
     */
    private void loadFromXml(String xmlConfigPath) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(new File(xmlConfigPath));
        document.getDocumentElement().normalize();

        // --- Parse <departments> ---
        Map<Integer, Department> departmentById = new HashMap<>();
        NodeList departmentNodes = document.getElementsByTagName("department");
        for (int i = 0; i < departmentNodes.getLength(); i++) {
            Element departmentElement = (Element) departmentNodes.item(i);
            int id = Integer.parseInt(getTagValue(departmentElement, "id"));
            String name = getTagValue(departmentElement, "name");
            Department department = new Department(id, name);
            departmentById.put(id, department);
            DEPARTMENT_LIST.add(department);
        }

        // --- Parse <employeeList>/<employee> ---
        NodeList employeeNodes = document.getElementsByTagName("employee");
        for (int i = 0; i < employeeNodes.getLength(); i++) {
            Element employeeElement = (Element) employeeNodes.item(i);

            int id = Integer.parseInt(getTagValue(employeeElement, "id"));
            String name = getTagValue(employeeElement, "name");
            String email = getTagValue(employeeElement, "email");
            int departmentId = Integer.parseInt(getTagValue(employeeElement, "departmentId"));
            Department department = departmentById.get(departmentId);

            ArrayList<String> skills = new ArrayList<>();
            NodeList skillNodes = employeeElement.getElementsByTagName("skill");
            for (int s = 0; s < skillNodes.getLength(); s++) {
                skills.add(skillNodes.item(s).getTextContent().trim());
            }

            EMPLOYEE_LIST.add(new Employee(id, name, email, department, skills));
        }
    }

    /**
     * Small helper that reads the direct child element's text content.
     */
    private String getTagValue(Element parent, String tagName) {
        NodeList nodeList = parent.getElementsByTagName(tagName);
        if (nodeList.getLength() == 0) {
            return null;
        }
        Node node = nodeList.item(0);
        return node.getTextContent() == null ? null : node.getTextContent().trim();
    }
}
