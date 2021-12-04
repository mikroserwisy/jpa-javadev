package pl.training.jpa.payments.service;

import org.mapstruct.Mapper;
import pl.training.jpa.commons.FastMoneyMapper;
import pl.training.jpa.payments.repository.PaymentEntity;

@Mapper(uses = FastMoneyMapper.class)
public interface PaymentsMapper {

    PaymentEntity toEntity(Payment payment);

}
