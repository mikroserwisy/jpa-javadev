package pl.training.jpa;

import org.hibernate.Session;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

// https://vladmihalcea.com/hibernate-facts-equals-and-hashcode
// https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier
abstract class EntityTest<T extends Identifiable<?>> extends BaseTest {

    private final Set<T> entities = new HashSet<>();
    protected final EntityManager entityManager = entityManagerFactory.createEntityManager();
    protected EntityTransaction transaction = entityManager.getTransaction();

    protected T entity;

    protected abstract T initializeEntity();

    @BeforeEach
    void init() {
        entity = initializeEntity();
        entities.add(entity);
        transaction.begin();
    }

    @Test
    void should_confirm_entity_in_set() {
        assertTrue(entities.contains(entity));
    }

    @Test
    void should_confirm_entity_in_set_after_persist() {
        entityManager.persist(entity);
        assertTrue(entities.contains(entity));
    }

    @Test
    void should_confirm_entity_in_set_after_merge() {
        entityManager.persist(entity);
        var mergedEntity = entityManager.merge(entity);
        entityManager.flush();
        assertTrue(entities.contains(mergedEntity));
    }

    @Test
    void should_confirm_entity_in_set_after_remove() {
        entityManager.persist(entity);
        var proxy = entityManager.getReference(entity.getClass(), entity.getId());
        entityManager.remove(proxy);
        assertTrue(entities.contains(proxy));
    }

    @Test
    void entity_should_equals_proxy() {
        entityManager.persist(entity);
        var proxy = entityManager.getReference(entity.getClass(), entity.getId());
        assertEquals(entity, proxy);
    }

    @Test
    void proxy_should_equals_entity() {
        entityManager.persist(entity);
        var proxy = entityManager.getReference(entity.getClass(), entity.getId());
        assertEquals(proxy, entity);
    }

    @Test
    void should_confirm_entity_in_set_after_update() {
        entityManager.persist(entity);
        entityManager.unwrap(Session.class).update(entity);
        assertTrue(entities.contains(entity));
    }

    @Test
    void should_confirm_entity_in_set_after_find() {
        entityManager.persist(entity);
        var loadedEntity = entityManager.find(entity.getClass(), entity.getId());
        assertTrue(entities.contains(loadedEntity));
    }

    @Test
    void should_confirm_entity_in_set_after_get_reference() {
        entityManager.persist(entity);
        var proxy = entityManager.getReference(entity.getClass(), entity.getId());
        assertTrue(entities.contains(proxy));
    }

    @AfterEach
    void afterEach() {
        transaction.commit();
        entityManager.close();
        entityManagerFactory.close();
    }

}
