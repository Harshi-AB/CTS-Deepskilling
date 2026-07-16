/**
 * Country.java
 *
 * Simple JavaBean representing a Country resource.
 * Follows standard JavaBean conventions: private fields, no-arg constructor,
 * public getters/setters, and a readable toString().
 */
public class Country {

    private String code;       // ISO-style country code, e.g. "IN"
    private String name;       // Country name, e.g. "India"
    private String capital;    // Capital city, e.g. "New Delhi"
    private String region;     // Region/continent, e.g. "Asia"
    private long population;   // Population count

    // No-argument constructor required for bean semantics
    public Country() {
    }

    public Country(String code, String name, String capital, String region, long population) {
        this.code = code;
        this.name = name;
        this.capital = capital;
        this.region = region;
        this.population = population;
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

    public String getCapital() {
        return capital;
    }

    public void setCapital(String capital) {
        this.capital = capital;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public long getPopulation() {
        return population;
    }

    public void setPopulation(long population) {
        this.population = population;
    }

    @Override
    public String toString() {
        return "Country{code='" + code + "', name='" + name + "', capital='" + capital
                + "', region='" + region + "', population=" + population + "}";
    }
}
