import java.util.List;
import java.util.Optional;

/**
 * Custom re-implementation of org.springframework.data.jpa.repository.JpaRepository.
 *
 * Any interface that "extends JpaRepository<T, ID>" gets these four methods
 * implemented automatically at runtime by JpaRepositoryProxyFactory, using a
 * JDK dynamic proxy - exactly the mechanism real Spring Data JPA uses to turn
 * repository interfaces into working beans without you writing an impl class.
 *
 * @param <T>  the entity type (must be annotated with @Entity/@Table)
 * @param <ID> the type of the entity's @Id field
 */
public interface JpaRepository<T, ID> {

    List<T> findAll();

    Optional<T> findById(ID id);

    T save(T entity);

    void deleteById(ID id);
}
