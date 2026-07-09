/**
 * Employee
 * --------
 * Plain old Java object (POJO). Unlike Exercise 1's Country class, this
 * class carries NO persistence annotations at all - in true Hibernate XML
 * Configuration style, the object-to-relational mapping lives entirely in
 * the external config/Employee.hbm.xml mapping file, not in this class.
 */
public class Employee {

    private int id;
    private String firstName;
    private String lastName;
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
