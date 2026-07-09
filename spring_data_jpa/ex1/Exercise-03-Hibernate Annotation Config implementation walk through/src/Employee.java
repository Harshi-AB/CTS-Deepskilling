/**
 * Employee
 * --------
 * Persistence class mapped via annotations instead of an external XML
 * mapping file, as covered in Hands-on 3 (contrast with Exercise 2's
 * Employee.hbm.xml approach).
 *
 * @Entity        marks this as a persistence entity
 * @Table         maps it to the EMPLOYEE table
 * @Id            marks "id" as the primary key
 * @GeneratedValue tells Hibernate the id is database-generated (AUTO_INCREMENT)
 * @Column        maps each field to its column name
 */
@Entity
@Table(name = "EMPLOYEE")
public class Employee {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private int id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "salary")
    private int salary;

    public Employee() {
    }

    public Employee(String firstName, String lastName, int salary) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.salary = salary;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    @Override
    public String toString() {
        return "Employee [id=" + id + ", firstName=" + firstName
                + ", lastName=" + lastName + ", salary=" + salary + "]";
    }
}
