package jp.co.axa.apidemo.entities;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.List;

/**
 * Representing the rights of an {@link App}
 */
@Getter
public enum AppRight {
    /**
     * This privilege let the subject to read data
     */
    READ,

    /**
     * This privilege let the subject to write data,
     * regardless if the data is already in the database,
     * or not.
     */
    WRITE,

    /**
     * This privilege let the subject to delete data
     */
    DELETE;

    /**
     * Representing all privileges
     */
    public static List<AppRight> ALL = Arrays.asList(READ, WRITE, DELETE);

    public static class ListConverter extends AbstractEnumListConverter<AppRight> {
        public ListConverter() {
            super(AppRight.class);
        }
    }
}
