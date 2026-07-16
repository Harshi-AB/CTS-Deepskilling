import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * CountryRepository.java
 *
 * In-memory data store for Country resources.
 * Encapsulates data access so the HTTP handler stays focused only on
 * request/response concerns (separation of concerns).
 */
public class CountryRepository {

    // Keyed by country code for fast lookup - map preserves insertion order
    private final Map<String, Country> countries = new LinkedHashMap<>();

    public CountryRepository() {
        seedData();
    }

    private void seedData() {
        save(new Country("IN", "India", "New Delhi", "Asia", 1_428_000_000L));
        save(new Country("US", "United States", "Washington, D.C.", "North America", 335_000_000L));
        save(new Country("FR", "France", "Paris", "Europe", 68_000_000L));
    }

    public List<Country> findAll() {
        return new ArrayList<>(countries.values());
    }

    /** Supports the resource-naming guideline of filtering a collection via query params. */
    public List<Country> findByRegion(String region) {
        List<Country> result = new ArrayList<>();
        for (Country c : countries.values()) {
            if (c.getRegion().equalsIgnoreCase(region)) {
                result.add(c);
            }
        }
        return result;
    }

    public Optional<Country> findByCode(String code) {
        return Optional.ofNullable(countries.get(code.toUpperCase()));
    }

    public Country save(Country country) {
        countries.put(country.getCode().toUpperCase(), country);
        return country;
    }

    public boolean existsByCode(String code) {
        return countries.containsKey(code.toUpperCase());
    }
}
