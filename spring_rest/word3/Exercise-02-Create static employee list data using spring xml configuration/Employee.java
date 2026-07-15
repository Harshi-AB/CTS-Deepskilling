import java.util.List;

/**
 * Employee.java
 *
 * Plain Old Java Object (POJO) representing an Employee.
 * Populated from employee.xml (spring xml configuration equivalent).
 */
public class Employee {

    private int id;
    private String name;
    private String email;
    private Department department;
    private List<String> skills;

    public Employee() {
    }

    public Employee(int id, String name, String email, Department department, List<String> skills) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.department = department;
        this.skills = skills;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public List<String> getSkills() {
        return skills;
    }

    public void setSkills(List<String> skills) {
        this.skills = skills;
    }

    /**
     * Builds a minimal JSON representation of this employee so that
     * the REST controller can send back a JSON HTTP response, the same
     * way a Spring @RestController would when serializing an Employee.
     */
    public String toJson() {
        StringBuilder skillsJson = new StringBuilder("[");
        if (skills != null) {
            for (int i = 0; i < skills.size(); i++) {
                skillsJson.append("\"").append(escape(skills.get(i))).append("\"");
                if (i < skills.size() - 1) {
                    skillsJson.append(",");
                }
            }
        }
        skillsJson.append("]");

        return "{"
                + "\"id\":" + id + ","
                + "\"name\":\"" + escape(name) + "\","
                + "\"email\":\"" + escape(email) + "\","
                + "\"department\":" + (department == null ? "null" : department.toJson()) + ","
                + "\"skills\":" + skillsJson
                + "}";
    }

    private String escape(String value) {
        return value == null ? "" : value.replace("\"", "\\\"");
    }

    @Override
    public String toString() {
        return String.format("%-5d %-15s %-30s %-25s %s",
                id, name, email,
                department == null ? "" : department.getName(),
                skills);
    }
}
