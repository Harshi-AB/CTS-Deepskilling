/**
 * SmsMessageService.java
 *
 * A second concrete implementation of MessageService, simulating an
 * SMS-based notification channel. Having two implementations shows
 * how DI lets us swap dependencies without changing MessagePrinter.
 */
public class SmsMessageService implements MessageService {

    @Override
    public String getMessage(String name) {
        return "[SMS] Hi " + name + ", DI exercise complete.";
    }
}
