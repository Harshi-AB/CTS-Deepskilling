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
}
