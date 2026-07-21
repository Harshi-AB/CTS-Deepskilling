import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * FallbackMonitor is responsible for logging and monitoring every time
 * the system falls back instead of completing a call to the real
 * third-party payment gateway (satisfying the exercise's "Log and
 * monitor fallback events" requirement). Every fallback is recorded
 * with a timestamp and reason, and a summary report can be printed at
 * the end of the run - similar to what a real system would send to
 * Micrometer/Prometheus/Grafana or a logging pipeline.
 */
public class FallbackMonitor {

    private static final DateTimeFormatter TIMESTAMP_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    /** Immutable record of a single fallback event. */
    public static class FallbackEvent {
        final String timestamp;
        final int orderId;
        final String reason;

        FallbackEvent(String timestamp, int orderId, String reason) {
            this.timestamp = timestamp;
            this.orderId = orderId;
            this.reason = reason;
        }

        @Override
        public String toString() {
            return "[" + timestamp + "] Order #" + orderId + " -> FALLBACK triggered (" + reason + ")";
        }
    }

    private final List<FallbackEvent> events = new ArrayList<>();

    /** Records and immediately logs a fallback event to the console. */
    public void recordFallback(int orderId, String reason) {
        String timestamp = LocalDateTime.now().format(TIMESTAMP_FORMAT);
        FallbackEvent event = new FallbackEvent(timestamp, orderId, reason);
        events.add(event);
        System.out.println("[FallbackMonitor] " + event);
    }

    /** Returns how many fallback events have been recorded so far. */
    public int getFallbackCount() {
        return events.size();
    }

    /** Prints a full summary report of every fallback event recorded. */
    public void printSummary() {
        System.out.println("\n[FallbackMonitor] ===== Fallback Event Summary =====");
        if (events.isEmpty()) {
            System.out.println("No fallback events were recorded.");
            return;
        }
        for (FallbackEvent event : events) {
            System.out.println("   " + event);
        }
        System.out.println("Total fallback events: " + events.size());
    }
}
