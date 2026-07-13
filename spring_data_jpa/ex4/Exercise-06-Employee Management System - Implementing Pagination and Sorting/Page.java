import java.util.List;

/**
 * Page.java
 *
 * Exercise 06 - Implementing Pagination and Sorting
 * -----------------------------------------------------
 * Custom stand-in for org.springframework.data.domain.Page<T>.
 * Wraps one page of results together with the metadata a UI needs to
 * render pagination controls (total elements, total pages, etc).
 */
public class Page<T> {

    private final List<T> content;
    private final long totalElements;
    private final int pageNumber;
    private final int pageSize;

    public Page(List<T> content, long totalElements, int pageNumber, int pageSize) {
        this.content = content;
        this.totalElements = totalElements;
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
    }

    public List<T> getContent() { return content; }
    public long getTotalElements() { return totalElements; }
    public int getTotalPages() { return pageSize == 0 ? 0 : (int) Math.ceil((double) totalElements / pageSize); }
    public int getNumber() { return pageNumber; }
    public int getSize() { return pageSize; }
    public boolean hasNext() { return (long) (pageNumber + 1) * pageSize < totalElements; }
    public boolean hasPrevious() { return pageNumber > 0; }
    public boolean isFirst() { return pageNumber == 0; }
    public boolean isLast() { return !hasNext(); }

    @Override
    public String toString() {
        return "Page " + (pageNumber + 1) + "/" + getTotalPages()
                + " (size=" + pageSize + ", totalElements=" + totalElements + ") -> " + content;
    }
}
