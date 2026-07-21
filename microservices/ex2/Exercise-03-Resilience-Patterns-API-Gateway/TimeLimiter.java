import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * TimeLimiter is the plain-Java equivalent of Resilience4j's TimeLimiter.
 * It runs a RemoteCall on a background thread and aborts (treats it as
 * a failure) if it does not complete within the configured timeout,
 * exactly like the "timeLimiterConfig(TimeLimiterConfig.ofDefaults())"
 * customizer shown in the exercise.
 */
public class TimeLimiter {

    private final TimeLimiterConfig config;
    private final ExecutorService executor;

    public TimeLimiter(TimeLimiterConfig config) {
        this.config = config;
        // A tiny daemon-thread pool used purely to enforce the timeout.
        this.executor = Executors.newCachedThreadPool(runnable -> {
            Thread t = new Thread(runnable);
            t.setDaemon(true);
            return t;
        });
    }

    /**
     * Executes the given call under a timeout. Throws
     * RemoteServiceException if the call fails or times out.
     */
    public <T> T executeWithTimeout(RemoteCall<T> call) throws RemoteServiceException {
        Callable<T> task = call::execute;
        Future<T> future = executor.submit(task);
        try {
            return future.get(config.getTimeoutMillis(), TimeUnit.MILLISECONDS);
        } catch (TimeoutException e) {
            future.cancel(true);
            throw new RemoteServiceException("Call timed out after " + config.getTimeoutMillis() + " ms", e);
        } catch (Exception e) {
            throw new RemoteServiceException("Call failed: " + e.getCause(), e);
        }
    }

    public void shutdown() {
        executor.shutdownNow();
    }
}
