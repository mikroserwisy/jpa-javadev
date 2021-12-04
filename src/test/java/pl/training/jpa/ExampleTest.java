package pl.training.jpa;

import lombok.extern.java.Log;
import org.hibernate.LazyInitializationException;
import org.hibernate.Session;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.training.jpa.commons.LocalMoney;
import pl.training.jpa.payments.repository.PaymentEntity;
import pl.training.jpa.payments.repository.PropertyEntity;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static pl.training.jpa.TestFixture.createPayment;

@Log
class ExampleTest extends BaseTest {

    private static final long SAMPLES = 10_000;

    private final PaymentEntity TEST_PAYMENT = createPayment(1_000);

    @BeforeEach
    void beforeEach() {
        TEST_PAYMENT.setProperties(List.of(new PropertyEntity("cvv", "266")));
        withTransaction(entityManager -> entityManager.persist(TEST_PAYMENT));
        log.info("#################################################################################################");
    }

    @Test
    void should_connect_to_database() {
        withTransaction(entityManager -> entityManager.unwrap(Session.class));
    }

    @Test
    void should_persist_payment() {
        withTransaction(entityManager -> {
            var payment = entityManager.find(PaymentEntity.class, TEST_PAYMENT.getId());
            assertNotNull(payment);
        });
    }

    @Test
    void should_not_load_entity_until_properties_are_accessed() {
        withTransaction(entityManager -> {
            var result = entityManager.getReference(PaymentEntity.class, TEST_PAYMENT.getId());
            log.info("Id: " + result.getId());
            assertEquals(0, statistics.getEntityLoadCount());
            log.info("Value: " + result.getValue());
        });
        assertEquals(1, statistics.getEntityLoadCount());
    }

    @Test
    void should_not_load_payment_properties_when_payment_is_loaded() {
        withTransaction(entityManager -> entityManager.find(PaymentEntity.class, TEST_PAYMENT.getId()));
        assertEquals(1, statistics.getEntityLoadCount());
    }

    PaymentEntity result;

    @Test
    void should_throw_exception_when_loading_payment_properties_after_transaction_is_closed() {
        withTransaction(entityManager -> result = entityManager.find(PaymentEntity.class, TEST_PAYMENT.getId()));
        assertThrows(LazyInitializationException.class, () -> result.getProperties().forEach(propertyEntity -> log.info(propertyEntity.toString())));
    }

    @Test
    void should_load_payment_properties_after_payment_is_loaded() {
        withTransaction(entityManager -> {
            var payment = entityManager.find(PaymentEntity.class, TEST_PAYMENT.getId());
            assertEquals(1, statistics.getEntityLoadCount());
            payment.getProperties().forEach(propertyEntity -> log.info(propertyEntity.toString()));
        });
        assertEquals(2, statistics.getEntityLoadCount());
    }

    @Test
    void should_eagerly_load_payments_properties() {
        withTransaction(entityManager -> entityManager.createNamedQuery(PaymentEntity.GET_ALL, PaymentEntity.class).getResultList());
        //withTransaction(entityManager -> entityManager.createQuery("select p from PaymentEntity p join fetch p.properties pp", PaymentEntity.class).getResultList());
        assertEquals(2, statistics.getEntityLoadCount());
    }

    @Test
    void should_eagerly_load_payments_properties_using_entity_graph() {
        withTransaction(entityManager -> {
            // wszystko co jest wymienione jest ładowane zachłannie, pozostałe leniwie
            Map<String, Object> properties = Map.of("javax.persistence.fetchgraph", entityManager.getEntityGraph(PaymentEntity.WITH_PROPERTIES));
            //  wszystko co jest wymienione jest ładowane zachłannie, pozostałe zgonie z konfiguracją
            //Map<String, Object> properties = Map.of("javax.persistence.loadgraph", entityManager.getEntityGraph(PaymentEntity.WITH_PROPERTIES));
            var payment = entityManager.find(PaymentEntity.class, TEST_PAYMENT.getId(), properties);
        });
        assertEquals(2, statistics.getEntityLoadCount());
    }

    @Test
    void should_eagerly_load_payments_properties_using_dynamic_entity_graph() {
        withTransaction(entityManager -> {
            var graph = entityManager.createEntityGraph(PaymentEntity.class);
            graph.addAttributeNodes("properties");
            // wszystko co jest wymienione jest ładowane zachłannie, pozostałe leniwie
            Map<String, Object> properties = Map.of("javax.persistence.fetchgraph", graph);
            //  wszystko co jest wymienione jest ładowane zachłannie, pozostałe zgonie z konfiguracją
            //Map<String, Object> properties = Map.of("javax.persistence.loadgraph", graph);
            var payment = entityManager.find(PaymentEntity.class, TEST_PAYMENT.getId(), properties);
        });
        assertEquals(2, statistics.getEntityLoadCount());
    }

    @Test
    void should_return_metrics_for_adding_payments_in_single_transaction() {
        var timer = metricRegistry.timer(getClass().getName());
        var startTime = System.nanoTime();
        withTransaction(entityManager -> {
            for (long sample = 1; sample <= SAMPLES; sample++) {
                var payment = createPayment(1_000);
                entityManager.persist(payment);
            }
        });
        timer.update(System.nanoTime() - startTime, TimeUnit.NANOSECONDS);
        reporter.report();
    }

    @Test
    void should_return_metrics_for_adding_payments_in_many_transactions() {
        var timer = metricRegistry.timer(getClass().getName());
        var startTime = System.nanoTime();
        for (long sample = 1; sample <= SAMPLES; sample++) {
            withTransaction(entityManager -> {
                var payment = createPayment(1_000);
                entityManager.persist(payment);
            });
        }
        timer.update(System.nanoTime() - startTime, TimeUnit.NANOSECONDS);
        reporter.report();
    }

    @Test
    void should_return_metrics_for_updating_payments_in_single_transaction() {
        preparePayments();
        var timer = metricRegistry.timer(getClass().getName());
        var startTime = System.nanoTime();
        withTransaction(entityManager -> {
            var pageSize = 1_000;
            for (int page = 1; page < (SAMPLES / pageSize); page++) {
                entityManager.createQuery("select p from PaymentEntity p", PaymentEntity.class)
                        .setFirstResult(page * pageSize)
                        .setMaxResults(pageSize)
                        .getResultList()
                        .forEach(payment -> {
                            payment.getValue().add(LocalMoney.of(500));
                            entityManager.merge(payment);
                            entityManager.flush();
                        });
                entityManager.clear();
            }
        });
        timer.update(System.nanoTime() - startTime, TimeUnit.NANOSECONDS);
        reporter.report();
    }

    @Test
    void should_return_metrics_for_updating_payments_in_single_update() {
        preparePayments();
        var timer = metricRegistry.timer(getClass().getName());
        var startTime = System.nanoTime();
        withTransaction(entityManager -> {
            entityManager.createQuery("update PaymentEntity p set p.value = :value")
                    .setParameter("value", LocalMoney.of(500))
                    .executeUpdate();
        });
        timer.update(System.nanoTime() - startTime, TimeUnit.NANOSECONDS);
        reporter.report();
    }

    private void preparePayments() {
        for (long sample = 1; sample <= SAMPLES; sample++) {
            withTransaction(entityManager -> {
                var payment = createPayment(1_000);
                entityManager.persist(payment);
            });
        }
    }

}
