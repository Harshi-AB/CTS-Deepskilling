import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Minimal re-implementation of org.slf4j.Logger, mimicking the log pattern
 * configured in application.properties:
 *   %d{dd-MM-yy} %d{HH:mm:ss.SSS} %-20.20thread %5p %-25.25logger{25} %25M %4L %m%n
 *
 * Only the pieces we need (info/debug/trace/error with {} placeholders) are
 * implemented, using plain java.time + System.out - no external logging jar.
 */
public class Logger {

    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("dd-MM-yy");
    private static final DateTimeFormatter TIME_FMT = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");

    private final String name;

    Logger(String name) {
        this.name = name;
    }

    public void info(String message, Object... args) {
        log("INFO", message, args);
    }

    public void debug(String message, Object... args) {
        log("DEBUG", message, args);
    }

    public void trace(String message, Object... args) {
        log("TRACE", message, args);
    }

    public void error(String message, Object... args) {
        log("ERROR", message, args);
    }

    private void log(String level, String message, Object... args) {
        LocalDateTime now = LocalDateTime.now();
        String formatted = format(message, args);
        String threadName = Thread.currentThread().getName();
        System.out.printf("%s %s %-20.20s %5s %-25.25s %s%n",
                now.format(DATE_FMT), now.format(TIME_FMT), threadName, level, name, formatted);
    }

    /** Replaces {} placeholders with args, same convention as SLF4J. */
    private String format(String message, Object... args) {
        String result = message;
        for (Object arg : args) {
            result = result.replaceFirst("\\{\\}", java.util.regex.Matcher.quoteReplacement(String.valueOf(arg)));
        }
        return result;
    }
}
