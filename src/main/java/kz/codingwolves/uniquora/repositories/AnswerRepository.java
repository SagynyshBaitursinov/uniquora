package kz.codingwolves.uniquora.repositories;

import kz.codingwolves.uniquora.models.Answer;
import kz.codingwolves.uniquora.models.Question;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by sagynysh on 1/7/17.
 */
@Repository
public class AnswerRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public Answer findById(Long id) {
        try {
            return entityManager.createQuery("Select a from Answer a where a.removed = false and a.id = ?1", Answer.class).setParameter(1, id).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<Answer> getAnswersByQuestion(Question question) {
        return entityManager.createQuery("Select a from Answer a where a.removed = false and a.question = ?1 order by a.createdDate desc", Answer.class).setParameter(1, question).getResultList();
    }

    @Transactional
    public Answer merge(Answer answer) {
        return entityManager.merge(answer);
    }

    public Long getAnswersNumber(Question question) {
        return entityManager.createQuery("Select count(a) from Answer a where a.removed = false and a.question = ?1", Long.class).setParameter(1, question).getSingleResult();
    }
}
