package pl.training.jpa;

import org.javamoney.moneta.FastMoney;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class FastMoneyConverter implements AttributeConverter<FastMoney, String> {

    @Override
    public String convertToDatabaseColumn(FastMoney value) {
        return value != null ? value.toString() : null;
    }

    @Override
    public FastMoney convertToEntityAttribute(String value) {
        return null != value ? FastMoney.parse(value) : LocalMoney.zero();
    }

}
