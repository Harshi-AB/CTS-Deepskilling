import java.util.ArrayList;
import java.util.List;

/**
 * Department.java
 *
 * Exercise 02 - Creating Entities
 * ---------------------------------
 * The "one" side of a one-to-many relationship with Employee.
 * Mapped with our own @Entity/@Table/@Id/@Column annotations, exactly the
 * way a real Spring Data JPA entity would be annotated with
 * javax.persistence equivalents.
 */
@Entity
@Table(name = "department")
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dept_id")
    private int deptId;

    @Column(name = "dept_name", nullable = false, unique = true)
    private String deptName;

    @Column(name = "location")
    private String location;

    // Inverse side of the relationship - not persisted directly,
    // populated on demand by the repository layer.
    @Transient
    private List<Employee> employees = new ArrayList<>();

    public Department() {
        // No-arg constructor required so the ORM engine can instantiate
        // entities reflectively (Class.newInstance / getDeclaredConstructor).
    }

    public Department(String deptName, String location) {
        this.deptName = deptName;
        this.location = location;
    }

    // ---------------- Getters and Setters ----------------

    public int getDeptId() {
        return deptId;
    }

    public void setDeptId(int deptId) {
        this.deptId = deptId;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }

    @Override
    public String toString() {
        return "Department{deptId=" + deptId + ", deptName='" + deptName + "', location='" + location + "'}";
    }
}
