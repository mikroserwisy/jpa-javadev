package pl.training.jpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.function.Consumer;

class BaseTest {

    protected final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("training");

    protected void withTransaction(Consumer<EntityManager> task) {
        var entityManager = entityManagerFactory.createEntityManager();
        var transaction = entityManager.getTransaction();
        transaction.begin();
        try {
            task.accept(entityManager);
            transaction.commit();
        } catch (RuntimeException exception) {
            exception.printStackTrace();
            transaction.rollback();
        }
        entityManager.close();
    }

}
