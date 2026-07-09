/**
 * Country
 * -------
 * Persistence class mapped to the "country" MySQL table:
 *
 *   create table country(co_code varchar(2) primary key, co_name varchar(50));
 *
 * @Entity   marks this as a JPA-managed entity
 * @Table    maps it to the "country" table
 * @Id       marks "code" as the primary key (co_code column)
 * @Column   maps each field to its database column
 */
@Entity
@Table(name = "country")
public class Country {

    @Id
    @Column(name = "co_code")
    private String code;

    @Column(name = "co_name")
    private String name;

    /** No-arg constructor required so the persistence layer can hydrate rows via reflection. */
    public Country() {
    }

    public Country(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Country [code=" + code + ", name=" + name + "]";
    }
}
