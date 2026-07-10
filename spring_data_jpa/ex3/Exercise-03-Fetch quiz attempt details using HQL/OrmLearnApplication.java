/*
 * OrmLearnApplication.java
 * -------------------------
 * Application entry point for Hands-on 3. Fetches attempt details for
 * userId=1, attemptId=1 (john_doe's attempt) and prints, for every
 * question: the question text, followed by every option with
 *   <option number>) <option text> <score> <true/false - was it selected?>
 * exactly matching the sample output format shown in the hands-on document.
 */
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class OrmLearnApplication {

    private static final AppLogger LOGGER = AppLogger.getLogger(OrmLearnApplication.class);

    private static final AttemptRepository attemptRepository = new AttemptRepository();
    private static final AttemptService attemptService = new AttemptService(attemptRepository);

    public static void main(String[] args) {
        try {
            testGetAttemptDetail();
        } finally {
            JPAUtil.shutdown();
        }
    }

    public static void testGetAttemptDetail() {
        LOGGER.info("Start");

        int userId = 1;
        int attemptId = 1;
        Attempt attempt = attemptService.getAttemptDetail(userId, attemptId);

        if (attempt == null) {
            LOGGER.info("No attempt found for userId=" + userId + ", attemptId=" + attemptId);
            return;
        }

        printAttempt(attempt);

        LOGGER.info("End");
    }

    /**
     * Renders the attempt exactly in the format demonstrated by the
     * hands-on document: question text on its own line, then each option
     * indented and numbered, showing (option text, score, selected?).
     */
    private static void printAttempt(Attempt attempt) {
        // Sort questions by attempt_question_id so the output order is deterministic
        // (Set collections do not preserve insertion order on their own).
        List<AttemptQuestion> attemptQuestions = new ArrayList<>(attempt.getAttemptQuestionList());
        attemptQuestions.sort(Comparator.comparingInt(AttemptQuestion::getId));

        for (AttemptQuestion aq : attemptQuestions) {
            Question question = aq.getQuestion();
            System.out.println(question.getQuestionText());

            // Determine which option ids were selected by the user for this question
            List<Integer> selectedOptionIds = new ArrayList<>();
            for (AttemptOption ao : aq.getAttemptOptionList()) {
                selectedOptionIds.add(ao.getOption().getId());
            }

            List<QuizOption> options = new ArrayList<>(question.getOptionList());
            options.sort(Comparator.comparingInt(QuizOption::getId));

            int number = 1;
            for (QuizOption option : options) {
                boolean selected = selectedOptionIds.contains(option.getId());
                System.out.printf(" %d) %-10s %-6s %s%n",
                        number, option.getOptionText(), option.getScore(), selected);
                number++;
            }
            System.out.println();
        }
    }
}
