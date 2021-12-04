package pl.training.jpa;

import org.junit.jupiter.api.Test;

class ExampleTest extends BaseTest {

    @Test
    void should_connect_to_database() {
        withTransaction(entityManager -> {});
    }

}
