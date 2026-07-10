/*
 * Employee.java
 * -------------
 * Entity class mapped to the 'employee' MySQL table.
 *
 * Relationships:
 *   - Many-to-One with Department (owning side, holds the em_dp_id FK)
 *   - Many-to-Many with Skill through the join table 'employee_skill'
 *     (owning side, defines the @JoinTable)
 *
 * Both associations are LAZY by default. Hands-on 2 explains why: eagerly
 * fetching everything causes N+1 SELECT problems. Instead, the repository
 * query explicitly uses "left join fetch" in HQL whenever the department
 * and skill details are actually required by the caller.
 */
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "employee")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "em_id")
    private int id;

    @Column(name = "em_name")
    private String name;

    @Temporal(TemporalType.DATE)
    @Column(name = "em_date_of_birth")
    private Date dateOfBirth;

    @Column(name = "em_salary")
    private double salary;

    @Column(name = "em_permanent")
    private boolean permanent;

    // Owning side of many-to-one: FK column em_dp_id lives in 'employee' table
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "em_dp_id")
    private Department department;

    // Owning side of many-to-many via join table employee_skill
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "employee_skill",
            joinColumns = @JoinColumn(name = "es_em_id"),
            inverseJoinColumns = @JoinColumn(name = "es_sk_id")
    )
    private List<Skill> skillList;

    public Employee() {
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

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public boolean isPermanent() {
        return permanent;
    }

    public void setPermanent(boolean permanent) {
        this.permanent = permanent;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public List<Skill> getSkillList() {
        return skillList;
    }

    public void setSkillList(List<Skill> skillList) {
        this.skillList = skillList;
    }

    @Override
    public String toString() {
        String deptName = (department == null) ? "null" : department.getName();
        return "Employee{id=" + id + ", name='" + name + "', salary=" + salary
                + ", permanent=" + permanent + ", department=" + deptName + "}";
    }
}
