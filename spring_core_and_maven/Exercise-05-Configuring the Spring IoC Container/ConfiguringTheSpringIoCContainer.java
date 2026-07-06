/**
 * ConfiguringTheSpringIoCContainer.java
 *
 * Demonstrates configuring and using an IoC container from scratch
 * (since the real Spring container is not permitted here):
 *   1. Register a bean definition describing NotificationService's
 *      implementation and scope.
 *   2. Resolve the bean by name from the container.
 *   3. Confirm singleton behaviour by fetching it twice and comparing
 *      references.
 */
public class ConfiguringTheSpringIoCContainer {

    public static void main(String[] args) {
        IoCContainer container = new IoCContainer();

        // 1. Configure the container with a bean definition
        container.registerBeanDefinition(
                "notificationService",
                new BeanDefinition(EmailNotificationService.class, true) // singleton scope
        );

        // 2. Resolve the bean
        NotificationService service1 = (NotificationService) container.getBean("notificationService");
        service1.notify("Your Deep Skilling module has been submitted.");

        // 3. Resolve it again and prove it is the same singleton instance
        NotificationService service2 = (NotificationService) container.getBean("notificationService");
        System.out.println("Same singleton instance? " + (service1 == service2));
    }
}
