/*
 * Skill.java
 * ----------
 * Entity class mapped to the 'skill' MySQL table.
 * Holds master data of skills (e.g. Java, SQL, Spring) that can be
 * associated with many employees (many-to-many relationship).
 *
 * Design principle used: simple POJO / JavaBean pattern - private fields
 * exposed only through public getters/setters (encapsulation).
 */
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "skill")
public class Skill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sk_id")
    private int id;

    @Column(name = "sk_name")
    private String name;

    // Required no-arg constructor - mandatory for JPA entities
    public Skill() {
    }

    public Skill(String name) {
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

    @Override
    public String toString() {
        // Only print scalar fields - avoids infinite loop with Employee.toString()
        return "Skill{id=" + id + ", name='" + name + "'}";
    }
}
