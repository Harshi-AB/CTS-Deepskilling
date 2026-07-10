/*
 * QuizOption.java
 * ---------------
 * Master data entity mapped to the 'options' table (named QuizOption in
 * Java to avoid clashing with java.util.Optional / the 'option' keyword
 * used by many build tools). Each option belongs to exactly one Question
 * and carries a 'score' - the points awarded if a user picks that option.
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
@Table(name = "options")
public class QuizOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "option_id")
    private int id;

    @Column(name = "option_text")
    private String optionText;

    @Column(name = "score")
    private double score;

    // Owning side - FK column question_id lives in the 'options' table
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id")
    private Question question;

    public QuizOption() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOptionText() {
        return optionText;
    }

    public void setOptionText(String optionText) {
        this.optionText = optionText;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    @Override
    public String toString() {
        return "QuizOption{id=" + id + ", text='" + optionText + "', score=" + score + "}";
    }
}
