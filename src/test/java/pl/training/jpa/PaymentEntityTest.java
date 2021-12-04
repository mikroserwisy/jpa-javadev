package pl.training.jpa;

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
