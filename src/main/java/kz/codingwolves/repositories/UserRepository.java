package kz.codingwolves.repositories;

import kz.codingwolves.models.User;
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
public class UserRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public List<User> findAll() {
        return entityManager.createQuery("Select u from User u where u.removed = false", User.class).getResultList();
    }

    public User findByEmail(String email) {
        try {
            return entityManager.createQuery("Select u from User u where u.email = ?1", User.class).setParameter(1, email).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Transactional
    public User merge(User user) {
        return entityManager.merge(user);
    }
}
