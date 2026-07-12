import java.util.Set;

/**
 * Identical mapping to Department, except employeeList uses
 * FetchType.EAGER. Used by testGetDepartmentEmployeesEager() to show the
 * "fix" for the LazyInitializationException demonstrated by
 * testGetDepartmentEmployeesLazy() - exactly the two-step exercise
 * described in the hands-on document (observe the failure, then flip the
 * annotation and observe success).
 */
@Entity
@Table(name = "department")
public class DepartmentEager {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dp_id")
    private int id;

    @Column(name = "dp_name")
    private String name;

    @OneToMany(mappedBy = "department", fetch = FetchType.EAGER)
    private Set<Employee> employeeList;

    public DepartmentEager() {
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

    public Set<Employee> getEmployeeList() {
        return employeeList;
    }

    public void setEmployeeList(Set<Employee> employeeList) {
        this.employeeList = employeeList;
    }

    @Override
    public String toString() {
        return "Department{id=" + id + ", name='" + name + "'}";
    }
}
