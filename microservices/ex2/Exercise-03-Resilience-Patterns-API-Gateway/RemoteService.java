import java.util.Random;

/**
 * RemoteService simulates a flaky downstream microservice that the
 * gateway calls through the CircuitBreaker + TimeLimiter. Depending on
 * the injected failure/slow probabilities it may:
 *   - succeed immediately,
 *   - throw a RemoteServiceException (simulating a 5xx error), or
 *   - hang past the TimeLimiter's timeout (simulating a slow backend).
 *
 * This class plays the same role that "http://example.org" (the real
 * downstream service) plays in the original Spring Cloud Gateway
 * exercise - something the resilience layer protects calls to.
 */
public class RemoteService {

    private final String name;
    private final double failureProbability; // 0.0 - 1.0
    private final double slowProbability;    // 0.0 - 1.0
    private final long slowResponseMillis;
    private final Random random = new Random();

    public RemoteService(String name, double failureProbability, double slowProbability, long slowResponseMillis) {
        this.name = name;
        this.failureProbability = failureProbability;
        this.slowProbability = slowProbability;
        this.slowResponseMillis = slowResponseMillis;
    }

    /**
     * Simulates one downstream call. Called on a background thread by
     * the TimeLimiter, so a "hang" here genuinely blocks that thread
     * until the TimeLimiter's timeout fires.
     */
    public String call() throws RemoteServiceException {
        double roll = random.nextDouble();

        if (roll < failureProbability) {
            throw new RemoteServiceException(name + " returned an error (simulated 5xx response)");
        }

        if (roll < failureProbability + slowProbability) {
            try {
                Thread.sleep(slowResponseMillis);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RemoteServiceException(name + " call was interrupted", e);
            }
        }

        return name + " responded with 200 OK";
    }
}
