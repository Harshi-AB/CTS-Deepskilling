/*
 * Department.java
 * ----------------
 * Entity class mapped to the 'department' MySQL table.
 * A Department has a one-to-many relationship with Employee
 * (one department can have many employees).
 *
 * The 'employeeList' side is LAZY and mappedBy 'department' - Department
 * does NOT own the foreign key (Employee.em_dp_id owns it), so this is the
 * inverse side of the relationship (standard bidirectional O/R mapping).
 */
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = "department")
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dp_id")
    private int id;

    @Column(name = "dp_name")
    private String name;

    // Inverse side - LAZY by default so it is only loaded when explicitly
    // asked for (e.g. via "join fetch" in an HQL query).
    @OneToMany(mappedBy = "department", fetch = FetchType.LAZY)
    private List<Employee> employeeList;

    public Department() {
    }

    public Department(String name) {
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

    public List<Employee> getEmployeeList() {
        return employeeList;
    }

    public void setEmployeeList(List<Employee> employeeList) {
        this.employeeList = employeeList;
    }

    @Override
    public String toString() {
        // Deliberately NOT printing employeeList to avoid a lazy-load
        // exception / infinite recursion with Employee.toString()
        return "Department{id=" + id + ", name='" + name + "'}";
    }
}
