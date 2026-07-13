import java.util.List;

/**
 * JpaRepository.java
 *
 * Exercise 03 - Creating Repositories
 * -------------------------------------
 * Mirrors org.springframework.data.jpa.repository.JpaRepository<T, ID>.
 * Adds JPA-flavoured extras on top of CrudRepository. Pagination/Sorting
 * specific overloads are introduced formally in Exercise 06 - kept out
 * here so this exercise stays focused purely on repository *creation*.
 */
public interface JpaRepository<T, ID extends java.io.Serializable> extends CrudRepository<T, ID> {

    List<T> findAll(); // re-declared to allow covariant return of List (as real JpaRepository does)

    void flush();

    <S extends T> S saveAndFlush(S entity);

    void deleteAllInBatch();

    // ---- Exercise 06: Pagination and Sorting additions ----

    /** Returns all entities sorted according to the given Sort specification. */
    List<T> findAll(Sort sort);

    /** Returns one page of entities, honoring paging AND sorting from the Pageable. */
    Page<T> findAll(Pageable pageable);
}
