import java.util.List;

/**
 * Query Methods for the country table.
 * Method names are parsed by the mini-ORM's dynamic proxy at runtime -
 * exactly like Spring Data JPA's "Query Creation from method names":
 * https://docs.spring.io/spring-data/jpa/docs/2.2.0.RELEASE/reference/html/#jpa.query-methods.query-creation
 */
public interface CountryRepository extends Repository<Country, String> {

    /**
     * Search-as-you-type: all countries whose name contains the given text.
     * e.g. findByCoNameContaining("ou")
     */
    List<Country> findByCoNameContaining(String text);

    /**
     * Same search, but results returned in ascending alphabetical order.
     */
    List<Country> findByCoNameContainingOrderByCoNameAsc(String text);

    /**
     * Alphabet index lookup: all countries whose name starts with the given letter.
     * e.g. findByCoNameStartingWith("Z")
     */
    List<Country> findByCoNameStartingWith(String prefix);
}
