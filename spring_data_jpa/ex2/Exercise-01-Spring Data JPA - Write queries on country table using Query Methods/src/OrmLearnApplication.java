import java.util.List;

/**
 * Entry point for Exercise 01. Wires up CountryRepository via the
 * RepositoryFactory (mirrors @Autowired CountryRepository in Spring Data
 * JPA) and exercises each Query Method described in the hands-on.
 */
public class OrmLearnApplication {

    private static final Logger LOGGER = Logger.getLogger(OrmLearnApplication.class);
    private static CountryRepository countryRepository;

    public static void main(String[] args) throws Exception {
        countryRepository = RepositoryFactory.create(CountryRepository.class, Country.class);

        testSearchContaining();
        testSearchContainingOrdered();
        testAlphabetIndex();
    }

    /** Matches typing 'ou' into a country search text box. */
    private static void testSearchContaining() {
        LOGGER.info("Start - testSearchContaining");
        List<Country> countries = countryRepository.findByCoNameContaining("ou");
        for (Country c : countries) {
            System.out.println(c.getCoCode() + "\t" + c.getCoName());
        }
        LOGGER.info("End - testSearchContaining");
    }

    /** Same as above, but ascending alphabetical order. */
    private static void testSearchContainingOrdered() {
        LOGGER.info("Start - testSearchContainingOrdered");
        List<Country> countries = countryRepository.findByCoNameContainingOrderByCoNameAsc("ou");
        for (Country c : countries) {
            System.out.println(c.getCoCode() + "\t" + c.getCoName());
        }
        LOGGER.info("End - testSearchContainingOrdered");
    }

    /** Alphabet index: user clicks 'Z' -> all countries starting with Z. */
    private static void testAlphabetIndex() {
        LOGGER.info("Start - testAlphabetIndex");
        List<Country> countries = countryRepository.findByCoNameStartingWith("Z");
        for (Country c : countries) {
            System.out.println(c.getCoCode() + "\t" + c.getCoName());
        }
        LOGGER.info("End - testAlphabetIndex");
    }
}
