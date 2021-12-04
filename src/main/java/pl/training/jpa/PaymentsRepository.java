package pl.training.jpa;

import java.util.Optional;

public interface PaymentsRepository {

    PaymentEntity save(PaymentEntity paymentEntity);

    Optional<PaymentEntity> getById(Long id);

}
