/**
 * RemoteServiceException represents a failure returned by (or a timeout
 * while calling) a downstream microservice through the gateway.
 */
public class RemoteServiceException extends Exception {

    public RemoteServiceException(String message) {
        super(message);
    }

    public RemoteServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
