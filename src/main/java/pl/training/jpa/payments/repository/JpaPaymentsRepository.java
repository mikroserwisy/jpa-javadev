package pl.training.jpa.payments.repository;

import lombok.RequiredArgsConstructor;
import pl.training.jpa.commons.TransactionTemplate;

import java.util.Optional;

@RequiredArgsConstructor
public class JpaPaymentsRepository implements PaymentsRepository {

    private final TransactionTemplate transactionTemplate;

    @Override
    public PaymentEntity save(PaymentEntity payment) {
        return transactionTemplate.withTransaction(entityManager -> {
            entityManager.persist(payment);
            return payment;
        });
    }

    @Override
    public Optional<PaymentEntity> getById(Long id) {
        return transactionTemplate.withTransaction(entityManager -> Optional.ofNullable(entityManager.find(PaymentEntity.class, id)));
    }

}
