/*
 * AppLogger.java
 * --------------
 * Minimal logging helper used instead of an external SLF4J dependency
 * (keeps the project dependency-free apart from Hibernate + MySQL Connector).
 * Mimics the LOGGER.info(...) / LOGGER.debug(...) style used throughout
 * the hands-on document, including the "{}" placeholder convention.
 */
import java.time.LocalTime;

public final class AppLogger {

    private final String name;

    private AppLogger(String name) {
        this.name = name;
    }

    // Simple static factory - one logger instance per calling class name
    public static AppLogger getLogger(Class<?> clazz) {
        return new AppLogger(clazz.getSimpleName());
    }

    public void info(String message) {
        log("INFO", message);
    }

    // Supports a single "{}" placeholder, replaced with arg's toString()
    public void debug(String template, Object arg) {
        String message = template.replace("{}", String.valueOf(arg));
        log("DEBUG", message);
    }

    public void error(String message, Throwable t) {
        log("ERROR", message + " - " + t.getMessage());
    }

    private void log(String level, String message) {
        System.out.println("[" + LocalTime.now() + "] " + level + " " + name + " - " + message);
    }
}
