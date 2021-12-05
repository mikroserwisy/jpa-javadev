package pl.training.jpa;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.java.Log;
import org.javamoney.moneta.FastMoney;
import pl.training.jpa.payments.repository.PaymentEntity;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
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
    private final LockModeType lockModeType;

    @Override
    public void run() {
        var threadName = Thread.currentThread().getName();
        System.out.println("#### " + threadName + " started");
        try {
            var tx = entityManager.getTransaction();
            tx.begin();
            TimeUnit.SECONDS.sleep(firstSleepTime);
            System.out.println("#### " + threadName + " before load");
            var payment = entityManager.find(PaymentEntity.class, paymentId, lockModeType);
            System.out.println("#### " + threadName + " after load");
            TimeUnit.SECONDS.sleep(secondSleepTime);
            payment.setValue(newValue);
            System.out.println("#### " + threadName + " before commit");
            tx.commit();
            System.out.println("#### " + threadName + " after commit");
        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            entityManager.close();
            countDownLatch.countDown();
        }
    }

}
