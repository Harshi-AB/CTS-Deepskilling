/**
 * Entity mapping the "country" table.
 * co_code is the ISO alpha-2 country code and doubles as the primary key.
 */
@Entity
@Table(name = "country")
public class Country {

    @Id
    @Column(name = "co_code")
    private String coCode;

    @Column(name = "co_name")
    private String coName;

    public Country() {
    }

    public Country(String coCode, String coName) {
        this.coCode = coCode;
        this.coName = coName;
    }

    public String getCoCode() {
        return coCode;
    }

    public void setCoCode(String coCode) {
        this.coCode = coCode;
    }

    public String getCoName() {
        return coName;
    }

    public void setCoName(String coName) {
        this.coName = coName;
    }

    @Override
    public String toString() {
        return "Country{coCode='" + coCode + "', coName='" + coName + "'}";
    }
}
