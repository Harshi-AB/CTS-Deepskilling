/*
 * Question.java
 * -------------
 * Master data entity mapped to the 'question' table.
 * One Question has many QuizOptions (one-to-many, inverse side).
 */
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Set;

@Entity
@Table(name = "question")
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "question_id")
    private int id;

    @Column(name = "question_text")
    private String questionText;

    @OneToMany(mappedBy = "question", fetch = FetchType.LAZY)
    private Set<QuizOption> optionList;

    public Question() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public Set<QuizOption> getOptionList() {
        return optionList;
    }

    public void setOptionList(Set<QuizOption> optionList) {
        this.optionList = optionList;
    }

    @Override
    public String toString() {
        return "Question{id=" + id + ", text='" + questionText + "'}";
    }
}
