package kz.codingwolves.repositories;

import kz.codingwolves.models.Confirmation;
import kz.codingwolves.models.User;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by sagynysh on 1/3/17.
 */
@Repository
public class ConfirmationRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public List<Confirmation> getAllConfimrations() {
        return entityManager.createQuery("Select c from Confirmation c where c.isActive = true", Confirmation.class).getResultList();
    }

    public Confirmation getById(Long id) {
        try {
            return entityManager.createQuery("Select c from Confirmation c where c.id = ?1", Confirmation.class).setParameter(1, id).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<Confirmation> getByUser(User user) {
        return entityManager.createQuery("Select c from Confirmation c where c.user = ?1", Confirmation.class).setParameter(1, user).getResultList();
    }

    public Confirmation getLastByUser(User user) {
        try {
            return entityManager.createQuery("Select c from Confirmation c where c.user = ?1 order by c.createdDate desc", Confirmation.class).setParameter(1, user).setMaxResults(1).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Transactional
    public Confirmation merge(Confirmation confirmation) {
        return entityManager.merge(confirmation);
    }
}
