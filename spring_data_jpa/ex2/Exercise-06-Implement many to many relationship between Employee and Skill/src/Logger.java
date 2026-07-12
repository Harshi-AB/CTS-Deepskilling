/**
 * Minimal logger utility mimicking the subset of the SLF4J Logger API
 * used across this project (info / debug with "{}" placeholders), so
 * the project does not require any external logging JAR.
 */
public class Logger {

    private final String name;

    private Logger(String name) {
        this.name = name;
    }

    public static Logger getLogger(Class<?> clazz) {
        return new Logger(clazz.getSimpleName());
    }

    public void info(String message) {
        System.out.println("[INFO ] " + name + " - " + message);
    }

    public void debug(String message, Object... args) {
        System.out.println("[DEBUG] " + name + " - " + format(message, args));
    }

    private String format(String message, Object... args) {
        StringBuilder sb = new StringBuilder();
        int argIndex = 0;
        int i = 0;
        while (i < message.length()) {
            if (i + 1 < message.length() && message.charAt(i) == '{' && message.charAt(i + 1) == '}') {
                sb.append(argIndex < args.length ? String.valueOf(args[argIndex++]) : "{}");
                i += 2;
            } else {
                sb.append(message.charAt(i));
                i++;
            }
        }
        return sb.toString();
    }
}
