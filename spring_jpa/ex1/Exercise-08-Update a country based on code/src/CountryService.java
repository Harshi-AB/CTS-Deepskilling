import java.util.List;
import java.util.Optional;

/**
 * CountryService
 * --------------
 * @Service   marks this as a Spring-managed service bean
 * @Autowired injects the CountryRepository bean from ApplicationContext
 * @Transactional on each method mirrors how Spring would wrap it in a
 *                Hibernate session/transaction.
 */
@Service
public class CountryService {

    @Autowired
    private CountryRepository countryRepository;

    @Transactional
    public List<Country> getAllCountries() {
        return countryRepository.findAll();
    }

    /**
     * Finds a country by its 2-letter code.
     *
     * @throws CountryNotFoundException if no country exists for the given code
     */
    @Transactional
    public Country findCountryByCode(String countryCode) throws CountryNotFoundException {
        Optional<Country> result = countryRepository.findById(countryCode);
        if (!result.isPresent()) {
            throw new CountryNotFoundException("No country found for code: " + countryCode);
        }
        Country country = result.get();
        return country;
    }

    /** Adds a new country row via the repository's save() method. */
    @Transactional
    public void addCountry(Country country) {
        countryRepository.save(country);
    }

    /**
     * Updates the name of an existing country, identified by its code.
     *
     * @throws CountryNotFoundException if no country exists for the given code
     */
    @Transactional
    public void updateCountry(String code, String name) throws CountryNotFoundException {
        Optional<Country> result = countryRepository.findById(code);
        if (!result.isPresent()) {
            throw new CountryNotFoundException("No country found for code: " + code);
        }
        Country country = result.get();
        country.setName(name);
        countryRepository.save(country);
    }
}
