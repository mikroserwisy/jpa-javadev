package pl.training.jpa;

import pl.training.jpa.commons.LocalMoney;
import pl.training.jpa.payments.repository.PaymentEntity;

import java.util.Date;

class PaymentEntityTest extends EntityTest<PaymentEntity> {

    @Override
    protected PaymentEntity initializeEntity() {
        return PaymentEntity.builder()
                .value(LocalMoney.of(1_000))
                .timestamp(new Date())
                .build();
    }

}
