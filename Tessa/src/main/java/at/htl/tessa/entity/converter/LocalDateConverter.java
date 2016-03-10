package at.htl.tessa.entity.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.sql.Date;
import java.time.LocalDate;

/**
 * Created by Korti on 17.01.2016.
 */
@Converter(autoApply = true)
public class LocalDateConverter implements AttributeConverter<LocalDate, Date> {
    @Override
    public Date convertToDatabaseColumn(LocalDate date) {
        if (date != null) {
            return Date.valueOf(date);
        }
        return null;
    }

    @Override
    public LocalDate convertToEntityAttribute(Date date) {
        if (date != null) {
            return date.toLocalDate();
        }
        return null;
    }
}
