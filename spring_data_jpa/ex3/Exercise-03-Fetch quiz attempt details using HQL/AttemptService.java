/*
 * AttemptService.java
 * -------------------
 * Service layer delegating to AttemptRepository.
 *
 * public Attempt getAttempt(int userId, int attemptId)
 */
public class AttemptService {

    private final AttemptRepository attemptRepository;

    public AttemptService(AttemptRepository attemptRepository) {
        this.attemptRepository = attemptRepository;
    }

    public Attempt getAttemptDetail(int userId, int attemptId) {
        return attemptRepository.getAttempt(userId, attemptId);
    }
}
