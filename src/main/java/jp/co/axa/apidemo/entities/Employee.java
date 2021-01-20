package jp.co.axa.apidemo.entities;

import jp.co.axa.apidemo.dto.EmployeeCreateUpdateDTO;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name="EMPLOYEE")
@NoArgsConstructor
@AllArgsConstructor
@Data
@EntityListeners(AuditingEntityListener.class)
public class Employee {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(name="EMPLOYEE_NAME")
    private String name;

    @Column(name="EMPLOYEE_SALARY")
    private Integer salary;

    @Column(name="DEPARTMENT")
    private String department;

    @CreatedDate
    private Timestamp createdAt;

    @LastModifiedDate
    private Timestamp updatedAt;

    /**
     * Create a new Employee from an existing DTO class
     * @param dto is an {@link EmployeeCreateUpdateDTO}
     */
    public Employee(EmployeeCreateUpdateDTO dto) {
        this.name = dto.getName();
        this.department = dto.getDepartment();
        this.salary = dto.getSalary();
    }
}
