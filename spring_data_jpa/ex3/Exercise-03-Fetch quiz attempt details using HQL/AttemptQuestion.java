/*
 * AttemptQuestion.java
 * --------------------
 * Transactional entity mapped to the 'attempt_question' table - links one
 * Attempt to one Question (i.e. "this question was part of this attempt").
 * Also holds which options the user picked for this question via
 * AttemptOption (one-to-many).
 */
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Set;

@Entity
@Table(name = "attempt_question")
public class AttemptQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "attempt_question_id")
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attempt_id")
    private Attempt attempt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id")
    private Question question;

    @OneToMany(mappedBy = "attemptQuestion", fetch = FetchType.LAZY)
    private Set<AttemptOption> attemptOptionList;

    public AttemptQuestion() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Attempt getAttempt() {
        return attempt;
    }

    public void setAttempt(Attempt attempt) {
        this.attempt = attempt;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public Set<AttemptOption> getAttemptOptionList() {
        return attemptOptionList;
    }

    public void setAttemptOptionList(Set<AttemptOption> attemptOptionList) {
        this.attemptOptionList = attemptOptionList;
    }

    @Override
    public String toString() {
        return "AttemptQuestion{id=" + id + "}";
    }
}
