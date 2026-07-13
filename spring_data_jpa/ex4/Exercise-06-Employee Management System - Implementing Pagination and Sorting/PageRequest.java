/**
 * PageRequest.java
 *
 * Exercise 06 - Implementing Pagination and Sorting
 * -----------------------------------------------------
 * Custom stand-in for org.springframework.data.domain.PageRequest -
 * the concrete, immutable Pageable implementation application code
 * actually constructs (PageRequest.of(page, size[, sort])).
 */
public class PageRequest implements Pageable {

    private final int pageNumber;
    private final int pageSize;
    private final Sort sort;

    private PageRequest(int pageNumber, int pageSize, Sort sort) {
        if (pageNumber < 0) throw new IllegalArgumentException("Page number must not be negative");
        if (pageSize < 1) throw new IllegalArgumentException("Page size must be at least 1");
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.sort = sort;
    }

    public static PageRequest of(int pageNumber, int pageSize) {
        return new PageRequest(pageNumber, pageSize, Sort.unsorted());
    }

    public static PageRequest of(int pageNumber, int pageSize, Sort sort) {
        return new PageRequest(pageNumber, pageSize, sort);
    }

    @Override public int getPageNumber() { return pageNumber; }
    @Override public int getPageSize() { return pageSize; }
    @Override public long getOffset() { return (long) pageNumber * pageSize; }
    @Override public Sort getSort() { return sort; }
}
