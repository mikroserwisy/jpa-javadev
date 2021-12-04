package pl.training.jpa;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Slf4jReporter;
import org.hibernate.SessionFactory;
import org.hibernate.stat.Statistics;
import org.junit.jupiter.api.AfterEach;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.function.Consumer;

import static org.slf4j.LoggerFactory.getLogger;

class BaseTest {

    protected final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("training");
    protected final Statistics statistics = entityManagerFactory.unwrap(SessionFactory.class).getStatistics();
    protected final MetricRegistry metricRegistry = new MetricRegistry();
    protected final Slf4jReporter reporter = Slf4jReporter
            .forRegistry(metricRegistry)
            .outputTo(getLogger(getClass()))
            .build();

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
