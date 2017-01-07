package kz.codingwolves.uniquora.repositories;

import kz.codingwolves.uniquora.controllers.QuestionsController;
import kz.codingwolves.uniquora.models.Question;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by sagynysh on 12/30/16.
 */
@Repository
public class QuestionRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public Question findById(Long id) {
        try {
            return entityManager.createQuery("Select q from Question q where q.removed = false and q.id = ?1", Question.class).setParameter(1, id).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<Question> getAllQuestions() {
        return entityManager.createQuery("Select q from Question q where q.removed = false order by q.createdDate desc", Question.class).getResultList();
    }

    public List<Question> list(Integer page) {
        return entityManager.createQuery("Select q from Question q where q.removed = false order by q.createdDate desc", Question.class).setFirstResult((page - 1) * QuestionsController.pageSize).setMaxResults(QuestionsController.pageSize).getResultList();
    }

    public Long count() {
        return entityManager.createQuery("Select count(q) from Question q where q.removed = false", Long.class).getSingleResult();
    }

    @Transactional
    public Question merge(Question question) {
        return entityManager.merge(question);
    }
}
