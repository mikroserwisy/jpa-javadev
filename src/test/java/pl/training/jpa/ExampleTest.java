package pl.training.jpa;

import org.hibernate.Session;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class ExampleTest extends BaseTest {

    private final PaymentEntity TEST_PAYMENTEntity = PaymentEntity.builder()
            .value(LocalMoney.of(1_000))
            .timestamp(new Date())
            .build();

    @Test
    void should_connect_to_database() {
        withTransaction(entityManager -> entityManager.unwrap(Session.class));
    }

    @Test
    void should_persist_payment() {
        withTransaction(entityManager -> entityManager.persist(TEST_PAYMENTEntity));
        withTransaction(entityManager -> {
            var payment = entityManager.find(PaymentEntity.class, TEST_PAYMENTEntity.getId());
            assertNotNull(payment);
        });
    }

    @AfterEach
    void afterEach() {
        entityManagerFactory.close();
    }

}
