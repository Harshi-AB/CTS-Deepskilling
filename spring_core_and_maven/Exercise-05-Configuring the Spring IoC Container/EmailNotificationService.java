/**
 * EmailNotificationService.java
 *
 * Concrete bean class that will be instantiated and managed by our
 * hand-written IoCContainer, similar to how a Spring bean is managed
 * by the ApplicationContext.
 */
public class EmailNotificationService implements NotificationService {

    public EmailNotificationService() {
        System.out.println("   (EmailNotificationService instance created by container)");
    }

    @Override
    public void notify(String message) {
        System.out.println("[EMAIL NOTIFICATION] " + message);
    }
}
