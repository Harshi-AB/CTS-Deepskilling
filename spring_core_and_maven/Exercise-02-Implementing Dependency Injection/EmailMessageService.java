/**
 * EmailMessageService.java
 *
 * One concrete implementation of MessageService, simulating an
 * email-based notification channel.
 */
public class EmailMessageService implements MessageService {

    @Override
    public String getMessage(String name) {
        return "[EMAIL] Dear " + name + ", your Spring DI exercise is ready.";
    }
}
