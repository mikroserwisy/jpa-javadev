package pl.training.jpa;

import pl.training.jpa.payments.repository.PaymentEntity;

import static pl.training.jpa.TestFixture.createPayment;

class PaymentEntityTest extends EntityTest<PaymentEntity> {

    @Override
    protected PaymentEntity initializeEntity() {
        return createPayment(1_000);
    }

}
