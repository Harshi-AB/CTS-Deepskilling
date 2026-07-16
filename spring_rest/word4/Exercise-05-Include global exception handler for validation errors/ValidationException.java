/**
 * ValidationException.java
 *
 * Custom checked-style runtime exception representing a validation failure.
 * Carrying the HTTP status code alongside the message lets the global
 * exception handler decide how to respond without re-inspecting the error.
 */
public class ValidationException extends RuntimeException {

    private final int statusCode;

    public ValidationException(String message) {
        super(message);
        this.statusCode = 400; // Bad Request by default
    }

    public ValidationException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
