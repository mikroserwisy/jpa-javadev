package pl.training.jpa;

import org.hibernate.SessionFactory;
import org.hibernate.stat.Statistics;
import org.junit.jupiter.api.AfterEach;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.function.Consumer;

class BaseTest {

    protected final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("training");
    protected final Statistics statistics = entityManagerFactory.unwrap(SessionFactory.class).getStatistics();

    protected void withTransaction(Consumer<EntityManager> task) {
        var entityManager = entityManagerFactory.createEntityManager();
        var transaction = entityManager.getTransaction();
        transaction.begin();
        try {
            task.accept(entityManager);
            transaction.commit();
        } catch (RuntimeException exception) {
            transaction.rollback();
            throw exception;
        }
        entityManager.close();
    }

    @AfterEach
    void afterEach() {
        entityManagerFactory.close();
    }

}
