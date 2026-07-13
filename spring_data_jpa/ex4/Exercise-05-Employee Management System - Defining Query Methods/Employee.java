import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Employee.java
 *
 * Exercise 02 - Creating Entities
 * ---------------------------------
 * The core entity of the Employee Management System. Every field is
 * mapped through our custom annotations so the reflection-based ORM
 * engine (Exercises 04-10) can build SQL automatically instead of the
 * developer hand-writing INSERT/UPDATE statements for every field.
 */
@Entity
@Table(name = "employee")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "emp_id")
    private int empId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "salary")
    private BigDecimal salary;

    @Column(name = "date_of_joining")
    private LocalDate dateOfJoining;

    @Column(name = "active")
    private boolean active;

    // Owning side of the relationship: Employee -> Department
    @ManyToOne
    @JoinColumn(name = "dept_id")
    private Department department;

    public Employee() {
        // Required no-arg constructor for reflective instantiation
    }

    public Employee(String name, String email, BigDecimal salary, LocalDate dateOfJoining,
                     boolean active, Department department) {
        this.name = name;
        this.email = email;
        this.salary = salary;
        this.dateOfJoining = dateOfJoining;
        this.active = active;
        this.department = department;
    }

    // ---------------- Getters and Setters ----------------

    public int getEmpId() {
        return empId;
    }

    public void setEmpId(int empId) {
        this.empId = empId;
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

    public BigDecimal getSalary() {
        return salary;
    }

    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }

    public LocalDate getDateOfJoining() {
        return dateOfJoining;
    }

    public void setDateOfJoining(LocalDate dateOfJoining) {
        this.dateOfJoining = dateOfJoining;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    @Override
    public String toString() {
        return "Employee{empId=" + empId + ", name='" + name + "', email='" + email
                + "', salary=" + salary + ", dateOfJoining=" + dateOfJoining
                + ", active=" + active
                + ", department=" + (department != null ? department.getDeptName() : "null") + "}";
    }
}
