/**
 * CountryRepository
 * -----------------
 * Spring Data JPA style repository. Notice there is NO implementation class -
 * at runtime, ApplicationContext.registerRepository() hands this interface to
 * JpaRepositoryProxyFactory, which builds a working proxy for it (see that
 * class for how findAll/findById/save/deleteById are actually implemented).
 */
@Repository
public interface CountryRepository extends JpaRepository<Country, String> {
}
