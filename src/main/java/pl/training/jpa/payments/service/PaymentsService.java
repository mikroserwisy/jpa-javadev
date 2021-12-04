package pl.training.jpa.payments.service;

import lombok.RequiredArgsConstructor;
import org.javamoney.moneta.FastMoney;
import pl.training.jpa.payments.repository.PaymentsRepository;

import static pl.training.jpa.payments.service.PaymentStatus.NOT_CONFIRMED;

@RequiredArgsConstructor
public class PaymentsService {

    private final PaymentsRepository paymentsRepository;
    private final TimeProvider timeProvider;
    private final PaymentsMapper mapper;

    public void process(FastMoney value) {
        var payment = Payment.builder()
                .value(value)
                .timestamp(timeProvider.getTimestamp())
                .status(NOT_CONFIRMED)
                .build();
        paymentsRepository.save(mapper.toEntity(payment));
    }

}
