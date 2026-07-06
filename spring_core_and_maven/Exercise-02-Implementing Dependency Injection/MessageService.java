/**
 * MessageService.java
 *
 * Abstraction (interface) that decouples the consumer (MessagePrinter)
 * from any concrete message-sending mechanism. This is the cornerstone
 * of Dependency Injection: code depends on abstractions, not on
 * concrete implementations.
 */
public interface MessageService {
    String getMessage(String name);
}
