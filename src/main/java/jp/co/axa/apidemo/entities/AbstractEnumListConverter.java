package jp.co.axa.apidemo.entities;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This converter class converts from a comma-separated string value
 * into a List, and vice-versa
 */
@Converter
public abstract class AbstractEnumListConverter
    <T extends Enum<T>>
    implements AttributeConverter<List<T>, String> {

    /**
     * Character used to separate the values
     */
    private static final String DELIMITER = ",";

    private final Class<T> cls;

    public AbstractEnumListConverter(Class<T> cls) {
        this.cls = cls;
    }

    @Override
    public String convertToDatabaseColumn(List<T> values) {
        return values == null ?
            null :
            values
                .stream()
                .map(T::name)
                .collect(Collectors.joining(DELIMITER));

    }

    @Override
    public List<T> convertToEntityAttribute(String serialized) {
        return (serialized == null || serialized.isEmpty()) ?
            Collections.emptyList() :
            Arrays.stream(serialized.split(DELIMITER))
                .map(strRight -> Enum.valueOf(cls, strRight))
                .collect(Collectors.toList());
    }
}
