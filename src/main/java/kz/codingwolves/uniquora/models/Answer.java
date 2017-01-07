package kz.codingwolves.uniquora.models;

import javax.persistence.*;

/**
 * Created by sagynysh on 1/7/17.
 */
@Entity
@Table(name="answers")
public class Answer extends PersistentUnit {

    @Column(name="text_", columnDefinition="TEXT")
    private String text;

    @Column(name="rating_")
    private Integer rating;

    @ManyToOne
    @JoinColumn(name="question_id")
    private Question question;

    @ManyToOne
    @JoinColumn(name="user_id_")
    private User creator;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }
}
