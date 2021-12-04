package pl.training.jpa;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.java.Log;
import org.javamoney.moneta.FastMoney;

import javax.persistence.*;
import java.time.Instant;

//@ExcludeDefaultListeners
//@ExcludeSuperclassListeners
//@EntityListeners(PaymentListener.class)
@Entity
@Log
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Payment {

    @Id
    private String id;
    //@Convert(converter = FastMoneyConverter.class)
    private FastMoney value;
    private Instant timestamp;

    /*@PrePersist
    public void prePersist() {
        log.info("prePersist");
    }

    @PostPersist
    public void postPersist() {
        log.info("postPersist");
    }

    @PreRemove
    public void preRemove() {
        log.info("preRemove");
    }

    @PostRemove
    public void postRemove() {
        log.info("postRemove");
    }

    @PreUpdate
    public void preUpdate() {
        log.info("preUpdate");
    }

    @PostUpdate
    public void postUpdate() {
        log.info("postUpdate");
    }

    @PostLoad
    public void postLoad() {
        log.info("postLoad");
    }*/

}
