/*
 * AttemptOption.java
 * ------------------
 * Transactional entity mapped to the 'attempt_option' table - records that
 * a particular QuizOption was the one SELECTED BY THE USER for a given
 * AttemptQuestion. Only the option(s) actually chosen have a row here;
 * un-chosen options simply have no matching AttemptOption row.
 */
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "attempt_option")
public class AttemptOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "attempt_option_id")
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attempt_question_id")
    private AttemptQuestion attemptQuestion;

    // The option that was selected by the user for this attempt_question
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "option_id")
    private QuizOption option;

    public AttemptOption() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public AttemptQuestion getAttemptQuestion() {
        return attemptQuestion;
    }

    public void setAttemptQuestion(AttemptQuestion attemptQuestion) {
        this.attemptQuestion = attemptQuestion;
    }

    public QuizOption getOption() {
        return option;
    }

    public void setOption(QuizOption option) {
        this.option = option;
    }

    @Override
    public String toString() {
        return "AttemptOption{id=" + id + ", optionId=" + (option == null ? "null" : option.getId()) + "}";
    }
}
