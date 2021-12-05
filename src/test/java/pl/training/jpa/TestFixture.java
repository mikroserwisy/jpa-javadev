package pl.training.jpa;

import pl.training.jpa.commons.LocalMoney;
import pl.training.jpa.payments.repository.PaymentEntity;

import java.util.Date;

public class TestFixture {

    public static PaymentEntity createPayment(long value) {
        return PaymentEntity.builder()
                .value(LocalMoney.of(value))
                .timestamp(new Date())
                .state("CONFIRMED")
                .build();
    }

}
