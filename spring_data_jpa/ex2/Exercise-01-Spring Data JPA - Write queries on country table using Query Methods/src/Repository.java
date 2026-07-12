import java.util.List;
import java.util.Optional;

/**
 * Base repository interface every generated repository extends.
 * Mirrors the subset of org.springframework.data.jpa.repository.JpaRepository
 * used in this project.
 *
 * @param <T>  the entity type
 * @param <ID> the type of the entity's primary key
 */
public interface Repository<T, ID> {

    T save(T entity);

    Optional<T> findById(ID id);

    List<T> findAll();

    void deleteById(ID id);

    long count();
}
