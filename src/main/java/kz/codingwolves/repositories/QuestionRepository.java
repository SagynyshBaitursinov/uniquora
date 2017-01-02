package kz.codingwolves.repositories;

import kz.codingwolves.models.Question;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * Created by sagynysh on 12/30/16.
 */
@Repository
public class QuestionRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public List<Question> getAllQuestions() {
        return entityManager.createQuery("Select q from Question q where q.removed = false", Question.class).getResultList();
    }
}
