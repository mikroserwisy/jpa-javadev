package pl.training.jpa.payments.repository;

import java.util.Optional;

public interface PaymentsRepository {

    PaymentEntity save(PaymentEntity paymentEntity);

    Optional<PaymentEntity> getById(Long id);

}
