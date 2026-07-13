/**
 * Pageable.java
 *
 * Exercise 06 - Implementing Pagination and Sorting
 * -----------------------------------------------------
 * Custom stand-in for org.springframework.data.domain.Pageable.
 * Bundles page number, page size and Sort into a single request object
 * passed down to the repository layer.
 */
public interface Pageable {
    int getPageNumber();
    int getPageSize();
    long getOffset();
    Sort getSort();
}
