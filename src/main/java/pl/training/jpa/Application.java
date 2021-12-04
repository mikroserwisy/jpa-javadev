package pl.training.jpa;

import lombok.extern.java.Log;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import pl.training.jpa.commons.LocalMoney;
import pl.training.jpa.commons.TransactionTemplate;
import pl.training.jpa.payments.repository.JpaPaymentsRepository;
import pl.training.jpa.payments.repository.PaymentEntity;
import pl.training.jpa.payments.repository.PaymentsRepository;
import pl.training.jpa.payments.service.PaymentsMapper;
import pl.training.jpa.payments.service.PaymentsService;
import pl.training.jpa.payments.service.SystemTimeProvider;

import javax.persistence.Persistence;
import java.util.Date;

@Log
public class Application {

    private static final PaymentEntity PAYMENT = PaymentEntity.builder()
            .value(LocalMoney.of(1_000))
            .timestamp(new Date())
            .build();

    public static void main(String[] args) {
        var entityManagerFactory = Persistence.createEntityManagerFactory("training");
        var entityManager = entityManagerFactory.createEntityManager();
        var transactionTemplate = new TransactionTemplate(entityManager);
        var paymentRepository = new JpaPaymentsRepository(transactionTemplate);
        var timeProvider = new SystemTimeProvider();
        var paymentsService = new PaymentsService(paymentRepository, timeProvider, Mappers.getMapper(PaymentsMapper.class));
        //--------------------------
        paymentsService.process(LocalMoney.of(1_000));
    }

}
