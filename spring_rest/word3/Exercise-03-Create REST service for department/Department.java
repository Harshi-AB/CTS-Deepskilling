/**
 * Department.java
 *
 * Plain Old Java Object (POJO) representing a Department.
 * Populated from department.xml (spring xml configuration equivalent).
 */
public class Department {

    private int id;
    private String name;

    public Department() {
    }

    public Department(int id, String name) {
        this.id = id;
        this.name = name;
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

    /**
     * Builds a minimal JSON representation of this department so that
     * the REST controller can send back a JSON HTTP response.
     */
    public String toJson() {
        return "{"
                + "\"id\":" + id + ","
                + "\"name\":\"" + escape(name) + "\""
                + "}";
    }

    private String escape(String value) {
        return value == null ? "" : value.replace("\"", "\\\"");
    }

    @Override
    public String toString() {
        return String.format("%-5d %-25s", id, name);
    }
}
