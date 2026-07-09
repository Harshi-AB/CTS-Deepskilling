import java.util.List;

/**
 * CountryService
 * --------------
 * @Service   marks this as a Spring-managed service bean
 * @Autowired injects the CountryRepository bean from ApplicationContext
 * @Transactional on getAllCountries() mirrors how Spring would wrap this
 *                method in a Hibernate session/transaction.
 */
@Service
public class CountryService {

    @Autowired
    private CountryRepository countryRepository;

    @Transactional
    public List<Country> getAllCountries() {
        return countryRepository.findAll();
    }
}
