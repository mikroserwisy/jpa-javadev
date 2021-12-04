package pl.training.jpa;

import lombok.*;
import lombok.extern.java.Log;
import org.javamoney.moneta.FastMoney;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;
import java.util.Objects;

//@ExcludeDefaultListeners
//@ExcludeSuperclassListeners
//@EntityListeners(PaymentListener.class)
@Entity
@Log
@Builder
//@EqualsAndHashCode(exclude = "id")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentEntity implements Identifiable<Long> {

    @GeneratedValue
    @Id
    private Long id;
    //@Convert(converter = FastMoneyConverter.class)
    private FastMoney value;
    private Date timestamp;

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

    @Override
    public boolean equals(Object otherPayment) {
        if (this == otherPayment) {
            return true;
        }
        if (otherPayment == null || getClass() != otherPayment.getClass()) {
            return false;
        }
        var payment = (PaymentEntity) otherPayment;
        return Objects.equals(id, payment.id);
    }

    @Override
    public int hashCode() {
        return 13; //Objects.hash(getClass().getName());
    }

}
