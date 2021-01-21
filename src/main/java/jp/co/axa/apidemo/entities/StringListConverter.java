package jp.co.axa.apidemo.entities;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * This converter class converts from a comma-separated string value
 * into a List, and vice-versa
 */
@Converter
public class StringListConverter implements AttributeConverter<List<String>, String> {
    private static final String SPLIT_CHAR = ",";

    @Override
    public String convertToDatabaseColumn(List<String> strings) {
        return strings == null ?
            "" :
            String.join(SPLIT_CHAR, strings);
    }

    @Override
    public List<String> convertToEntityAttribute(String string) {
        return string == null ?
            Collections.emptyList() :
            Arrays.asList(string.split(SPLIT_CHAR));
    }
}
