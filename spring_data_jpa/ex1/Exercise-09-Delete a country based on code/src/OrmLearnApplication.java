import java.util.List;

/**
 * OrmLearnApplication (Hands-on 5)
 * ---------------------------------
 * Continues the orm-learn application from Exercise 1. Per Hands-on 5:
 *  - ddl-auto=validate would mean Hibernate checks the table/columns exist
 *    and throws if not (see sql/schema.sql for the real DDL).
 *  - All sample rows are cleared and replaced with the full country list
 *    from sql/countries.sql (249 countries).
 */
@SpringBootApplication
public class OrmLearnApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrmLearnApplication.class);

    private static CountryService countryService;

    public static void main(String[] args) throws Exception {
        ApplicationContext context = SpringApplication.run(OrmLearnApplication.class, args);
        LOGGER.info("Inside main");

        CountryRepository countryRepository = context.registerRepository(CountryRepository.class);

        // ddl-auto=create/update behaviour: start with a clean table, then load
        // the full country list, exactly as sql/countries.sql defines it.
        int loaded = CountryDataLoader.loadFromSqlFile("sql/countries.sql", countryRepository);
        LOGGER.info("Loaded {} countries from sql/countries.sql", loaded);

        countryService = context.registerBean(new CountryService());

        testGetAllCountries();
        getAllCountriesTest();
        testAddCountry();
        testUpdateCountry();
        testDeleteCountry();
    }

    /**
     * Deletes the "ZZ" country added in testAddCountry() / modified in
     * testUpdateCountry(), then confirms it is gone from the database.
     */
    private static void testDeleteCountry() {
        LOGGER.info("Start");
        countryService.deleteCountry("ZZ");
        try {
            Country stillThere = countryService.findCountryByCode("ZZ");
            LOGGER.error("Country was NOT deleted, still found: {}", stillThere);
        } catch (CountryNotFoundException e) {
            LOGGER.debug("Confirmed deleted - lookup correctly failed: {}", e.getMessage());
        }
        LOGGER.info("End");
    }

    /**
     * Updates the country added in testAddCountry() with a different name,
     * then confirms the change is reflected in the database.
     */
    private static void testUpdateCountry() {
        LOGGER.info("Start");
        try {
            countryService.updateCountry("ZZ", "New Zephyrland");
            Country updated = countryService.findCountryByCode("ZZ");
            LOGGER.debug("Country after update: {}", updated);
        } catch (CountryNotFoundException e) {
            LOGGER.error("Update failed: {}", e.getMessage());
        }
        LOGGER.info("End");
    }

    /**
     * Adds a new country, then looks it up by code to confirm it was
     * persisted, exactly as Hands-on 7 describes.
     */
    private static void testAddCountry() {
        LOGGER.info("Start");
        Country newCountry = new Country("ZZ", "Zephyrland");
        countryService.addCountry(newCountry);
        try {
            Country found = countryService.findCountryByCode("ZZ");
            LOGGER.debug("Newly added country found in database: {}", found);
        } catch (CountryNotFoundException e) {
            LOGGER.error("Newly added country was NOT found: {}", e.getMessage());
        }
        LOGGER.info("End");
    }

    private static void getAllCountriesTest() {
        LOGGER.info("Start");
        try {
            Country country = countryService.findCountryByCode("IN");
            LOGGER.debug("Country:{}", country);
        } catch (CountryNotFoundException e) {
            LOGGER.error("Country lookup failed: {}", e.getMessage());
        }
        LOGGER.info("End");
    }

    private static void testGetAllCountries() {
        LOGGER.info("Start");
        List<Country> countries = countryService.getAllCountries();
        LOGGER.debug("countries.size()={}", countries.size());
        LOGGER.debug("first 3 countries={}", countries.subList(0, Math.min(3, countries.size())));
        LOGGER.debug("last 3 countries={}", countries.subList(Math.max(0, countries.size() - 3), countries.size()));
        LOGGER.info("End");
    }
}
