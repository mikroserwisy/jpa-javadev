package pl.training.jpa;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.java.Log;
import org.javamoney.moneta.FastMoney;
import pl.training.jpa.payments.repository.PaymentEntity;

import javax.persistence.EntityManager;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@Setter
@Log
@RequiredArgsConstructor
public class UpdatePaymentTask implements TestTask {

    private EntityManager entityManager;
    private CountDownLatch countDownLatch;

    private final Long paymentId;
    private final FastMoney newValue;
    private final int firstSleepTime;
    private final int secondSleepTime;

    @Override
    public void run() {
        var threadName = Thread.currentThread().getName();
        log.info("#### " + threadName + " started");
        try {
            var tx = entityManager.getTransaction();
            tx.begin();
            TimeUnit.SECONDS.sleep(firstSleepTime);
            log.info("#### " + threadName + " before load");
            var payment = entityManager.find(PaymentEntity.class, paymentId);
            log.info("#### " + threadName + " after load");
            TimeUnit.SECONDS.sleep(secondSleepTime);
            payment.setValue(newValue);
            log.info("#### " + threadName + " before commit");
            tx.commit();
            log.info("#### " + threadName + " after commit");
        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            entityManager.close();
            countDownLatch.countDown();
        }
    }

}
