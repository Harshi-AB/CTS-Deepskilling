import java.util.Set;

/**
 * Entity mapping the "department" table (the "one" side of the one-to-many
 * relation). employeeList defaults to FetchType.LAZY per the JPA spec:
 * accessing getEmployeeList() on an employee-less-loaded Department throws
 * LazyInitializationException, exactly like real Hibernate would. Change
 * fetch to FetchType.EAGER below (and re-run) to fix it, as instructed in
 * the hands-on.
 */
@Entity
@Table(name = "department")
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dp_id")
    private int id;

    @Column(name = "dp_name")
    private String name;

    @OneToMany(mappedBy = "department", fetch = FetchType.LAZY)
    private Set<Employee> employeeList;

    public Department() {
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
        if (employeeList == null) {
            throw new LazyInitializationException(
                    "failed to lazily initialize a collection: department.employeeList, "
                    + "could not initialize proxy - no session");
        }
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
