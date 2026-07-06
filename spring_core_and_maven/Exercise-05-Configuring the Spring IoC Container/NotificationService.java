/**
 * NotificationService.java
 *
 * Abstraction that the IoC container will manage instances of.
 */
public interface NotificationService {
    void notify(String message);
}
