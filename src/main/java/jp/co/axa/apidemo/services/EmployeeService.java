package jp.co.axa.apidemo.services;

import jp.co.axa.apidemo.dto.EmployeeCreateUpdateDTO;
import jp.co.axa.apidemo.entities.Employee;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;
import java.util.Optional;

public interface EmployeeService {

    public List<Employee> retrieveEmployees();

    public Optional<Employee> getEmployee(Long employeeId);

    public Employee saveEmployee(EmployeeCreateUpdateDTO employeeDTO);

    public void deleteEmployee(Long employeeId);

    public Employee updateEmployee(Employee employee);
}