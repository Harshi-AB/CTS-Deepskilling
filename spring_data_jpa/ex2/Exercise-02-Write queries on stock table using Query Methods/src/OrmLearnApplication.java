import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Entry point for Exercise 02. Wires up StockRepository via
 * RepositoryFactory and exercises every Query Method scenario described
 * in the hands-on document.
 */
public class OrmLearnApplication {

    private static final Logger LOGGER = Logger.getLogger(OrmLearnApplication.class);
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    private static StockRepository stockRepository;

    public static void main(String[] args) throws Exception {
        stockRepository = RepositoryFactory.create(StockRepository.class, Stock.class);

        testFacebookSeptember2019();
        testGoogleAbove1250();
        testTop3HighestVolume();
        testNetflixLowest3();
    }

    /** All Facebook stock rows for September 2019. */
    private static void testFacebookSeptember2019() throws Exception {
        LOGGER.info("Start - testFacebookSeptember2019");
        Date start = DATE_FORMAT.parse("2019-09-01");
        Date end = DATE_FORMAT.parse("2019-09-30");
        List<Stock> rows = stockRepository.findByCodeAndDateBetween("FB", start, end);
        printRows(rows);
        LOGGER.info("End - testFacebookSeptember2019");
    }

    /** Google stock rows where the closing price was greater than 1250. */
    private static void testGoogleAbove1250() {
        LOGGER.info("Start - testGoogleAbove1250");
        List<Stock> rows = stockRepository.findByCodeAndCloseGreaterThan("GOOGL", 1250);
        printRows(rows);
        LOGGER.info("End - testGoogleAbove1250");
    }

    /** Top 3 dates (any stock) with the highest trading volume. */
    private static void testTop3HighestVolume() {
        LOGGER.info("Start - testTop3HighestVolume");
        List<Stock> rows = stockRepository.findTop3ByOrderByVolumeDesc();
        printRows(rows);
        LOGGER.info("End - testTop3HighestVolume");
    }

    /** 3 dates when Netflix stock closed at its lowest price. */
    private static void testNetflixLowest3() {
        LOGGER.info("Start - testNetflixLowest3");
        List<Stock> rows = stockRepository.findTop3ByCodeOrderByCloseAsc("NFLX");
        printRows(rows);
        LOGGER.info("End - testNetflixLowest3");
    }

    private static void printRows(List<Stock> rows) {
        System.out.println("+---------+------------+---------+----------+-----------+");
        System.out.printf("| %-7s | %-10s | %-7s | %-8s | %-9s |%n", "st_code", "st_date", "st_open", "st_close", "st_volume");
        System.out.println("+---------+------------+---------+----------+-----------+");
        for (Stock s : rows) {
            System.out.printf("| %-7s | %-10s | %7.2f | %8.2f | %9d |%n",
                    s.getCode(), DATE_FORMAT.format(s.getDate()), s.getOpen(), s.getClose(), s.getVolume());
        }
        System.out.println("+---------+------------+---------+----------+-----------+");
    }
}
