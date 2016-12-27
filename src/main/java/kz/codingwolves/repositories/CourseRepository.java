package kz.codingwolves.repositories;

import kz.codingwolves.models.Course;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
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
        return entityManager.createQuery("Select c from Course c where c.removed = false").getResultList();
    }
}
