/**
 * ImplementingDependencyInjection.java
 *
 * Demonstrates Dependency Injection using a hand-written DIContainer
 * (since the real Spring framework is not permitted in this exercise).
 *
 * Steps:
 *   1. Register a MessageService implementation with the container.
 *   2. Resolve the MessageService dependency from the container.
 *   3. Inject the resolved dependency into MessagePrinter's constructor.
 *   4. Swap the implementation to prove loose coupling.
 */
public class ImplementingDependencyInjection {

    public static void main(String[] args) {
        DIContainer container = new DIContainer();

        // Register the Email implementation against the MessageService type
        container.registerBean(MessageService.class, new EmailMessageService());

        // Resolve and inject via constructor
        MessageService emailService = container.resolve(MessageService.class);
        MessagePrinter printer = new MessagePrinter(emailService);
        printer.printMessage("Harshitha");

        // Swap dependency at runtime to demonstrate loose coupling
        container.registerBean(MessageService.class, new SmsMessageService());
        MessageService smsService = container.resolve(MessageService.class);
        MessagePrinter printer2 = new MessagePrinter(smsService);
        printer2.printMessage("Harshitha");
    }
}
