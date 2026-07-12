import java.util.Date;
import java.util.List;

/**
 * Query Methods for the stock table, matching each scenario in the
 * hands-on document. Method names are derived into SQL at runtime by
 * the mini-ORM's dynamic proxy - no query bodies are written by hand.
 */
public interface StockRepository extends Repository<Stock, Integer> {

    /** All rows for a given stock code within a date range (e.g. FB in Sept 2019). */
    List<Stock> findByCodeAndDateBetween(String code, Date startDate, Date endDate);

    /** All rows for a given stock code whose closing price is above a threshold. */
    List<Stock> findByCodeAndCloseGreaterThan(String code, double close);

    /** The N rows (across all stocks) with the highest trading volume. */
    List<Stock> findTop3ByOrderByVolumeDesc();

    /** The N rows for a given stock code with the lowest closing price. */
    List<Stock> findTop3ByCodeOrderByCloseAsc(String code);
}
