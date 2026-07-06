/**
 * MessagePrinter.java
 *
 * Consumer class that depends only on the MessageService abstraction.
 * The concrete implementation is "injected" from the outside (via the
 * constructor) rather than being created internally with "new". This
 * is classic Constructor-based Dependency Injection, the same pattern
 * Spring uses under the hood.
 */
public class MessagePrinter {

    private final MessageService messageService;

    // Dependency is injected through the constructor
    public MessagePrinter(MessageService messageService) {
        this.messageService = messageService;
    }

    public void printMessage(String name) {
        System.out.println(messageService.getMessage(name));
    }
}
