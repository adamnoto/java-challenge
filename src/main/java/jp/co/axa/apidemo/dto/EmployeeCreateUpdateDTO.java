package jp.co.axa.apidemo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

/**
 * A Data Transfer Object class for the Employee. It
 * only contains a subset of data, and is intended to
 * be used to create or update an Employee
 */
@Data
@AllArgsConstructor
public class EmployeeCreateUpdateDTO {
    @NotBlank
    private String name;

    @Min(300000) @Max(Integer.MAX_VALUE)
    private Integer salary;

    @NotBlank
    private String department;
}
