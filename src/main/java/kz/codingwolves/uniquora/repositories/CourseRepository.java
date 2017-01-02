package kz.codingwolves.uniquora.repositories;

import kz.codingwolves.uniquora.models.Course;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by sagynysh on 12/17/16.
 */
@Repository
public class CourseRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void merge(Course course) {
        entityManager.merge(course);
    }

    public List<Course> findAll() {
        return entityManager.createQuery("Select c from Course c where c.removed = false", Course.class).getResultList();
    }

    public Course findByCode(String code) {
        try {
            return entityManager.createQuery("Select c from Course c where c.code = ?1", Course.class).setParameter(1, code).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}
