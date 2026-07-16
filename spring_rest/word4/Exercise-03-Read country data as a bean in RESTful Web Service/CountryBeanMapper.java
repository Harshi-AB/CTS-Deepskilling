import java.util.Map;

/**
 * CountryBeanMapper.java
 *
 * Responsible for converting raw request data (a flat String map parsed
 * from JSON) into a proper Country JavaBean, and back again.
 *
 * Keeping this mapping logic in its own class (rather than inline in the
 * HTTP handler) is a small application of the Single Responsibility
 * Principle: the handler deals with HTTP, the mapper deals with binding.
 */
public class CountryBeanMapper {

    /** Reads raw fields and binds them onto a new Country bean. */
    public Country toBean(Map<String, String> fields) {
        Country country = new Country();
        country.setCode(fields.getOrDefault("code", ""));
        country.setName(fields.getOrDefault("name", ""));
        country.setCapital(fields.getOrDefault("capital", ""));
        country.setRegion(fields.getOrDefault("region", ""));

        String populationStr = fields.getOrDefault("population", "0");
        try {
            country.setPopulation(Long.parseLong(populationStr));
        } catch (NumberFormatException e) {
            country.setPopulation(0L);
        }
        return country;
    }

    /** Converts the bean back into JSON for the HTTP response. */
    public String toJson(Country country) {
        return JsonUtil.toJson(country);
    }
}
