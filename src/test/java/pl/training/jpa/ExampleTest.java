package pl.training.jpa;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class ExampleTest extends BaseTest {

    private final Payment TEST_PAYMENT = Payment.builder()
            .id(UUID.randomUUID().toString())
            .value(LocalMoney.of(1_000))
            .timestamp(Instant.now())
            .build();

    @Test
    void should_connect_to_database() {
        withTransaction(entityManager -> {});
    }

    @Test
    void should_persist_payment() {
        withTransaction(entityManager -> entityManager.persist(TEST_PAYMENT));
        withTransaction(entityManager -> {
            var payment = entityManager.find(Payment.class, TEST_PAYMENT.getId());
            assertNotNull(payment);
        });
    }

}
