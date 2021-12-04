package pl.training.jpa;

import lombok.extern.java.Log;

import javax.persistence.*;

@Log
public class PaymentListener {

    @PrePersist
    public void prePersist(Payment payment) {
        log.info("prePersist");
    }

    @PostPersist
    public void postPersist(Payment payment) {
        log.info("postPersist");
    }

    @PreRemove
    public void preRemove(Payment payment) {
        log.info("preRemove");
    }

    @PostRemove
    public void postRemove(Payment payment) {
        log.info("postRemove");
    }

    @PreUpdate
    public void preUpdate(Payment payment) {
        log.info("preUpdate");
    }

    @PostUpdate
    public void postUpdate(Payment payment) {
        log.info("postUpdate");
    }

    @PostLoad
    public void postLoad(Payment payment) {
        log.info("postLoad");
    }

}
