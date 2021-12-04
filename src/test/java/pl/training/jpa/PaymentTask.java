package pl.training.jpa;

import lombok.Setter;

import javax.persistence.EntityManager;
import java.util.concurrent.CountDownLatch;

@Setter
public class PaymentTask implements TestTask {

    private CountDownLatch countDownLatch;
    private EntityManager entityManager;

    @Override
    public void run() {
        countDownLatch.countDown();
    }

}
