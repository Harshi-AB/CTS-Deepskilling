import java.util.List;
import java.util.Optional;

/**
 * CrudRepository.java
 *
 * Exercise 03 - Creating Repositories
 * -------------------------------------
 * Mirrors org.springframework.data.repository.CrudRepository<T, ID>.
 * Declares the standard create/read/update/delete contract that every
 * concrete repository automatically inherits. Implementations are
 * supplied generically in Exercise 04 (Implementing CRUD Operations) -
 * this exercise is only about establishing the contract/hierarchy.
 */
public interface CrudRepository<T, ID extends java.io.Serializable> extends Repository<T, ID> {

    <S extends T> S save(S entity);

    Optional<T> findById(ID id);

    boolean existsById(ID id);

    List<T> findAll();

    List<T> findAllById(Iterable<ID> ids);

    long count();

    void deleteById(ID id);

    void delete(T entity);

    void deleteAll();
}
