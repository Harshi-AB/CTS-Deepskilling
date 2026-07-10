/*
 * Attempt.java
 * ------------
 * Transactional entity mapped to the 'attempt' table - represents one
 * occasion where a User attempted the quiz. Holds who attempted it, when,
 * and (via AttemptQuestion) which questions/options were involved.
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "attempt")
public class Attempt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "attempt_id")
    private int id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "attempted_date")
    private Date attemptedDate;

    // Owning side - FK column user_id lives in the 'attempt' table
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "attempt", fetch = FetchType.LAZY)
    private Set<AttemptQuestion> attemptQuestionList;

    public Attempt() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getAttemptedDate() {
        return attemptedDate;
    }

    public void setAttemptedDate(Date attemptedDate) {
        this.attemptedDate = attemptedDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<AttemptQuestion> getAttemptQuestionList() {
        return attemptQuestionList;
    }

    public void setAttemptQuestionList(Set<AttemptQuestion> attemptQuestionList) {
        this.attemptQuestionList = attemptQuestionList;
    }

    @Override
    public String toString() {
        return "Attempt{id=" + id + ", attemptedDate=" + attemptedDate + "}";
    }
}
