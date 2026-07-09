/**
 * Minimal re-implementation of org.slf4j.LoggerFactory.
 */
public class LoggerFactory {

    public static Logger getLogger(Class<?> clazz) {
        return new Logger(clazz.getSimpleName());
    }
}
