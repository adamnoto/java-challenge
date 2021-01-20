package jp.co.axa.apidemo.services;

import jp.co.axa.apidemo.dto.EmployeeCreateUpdateDTO;
import jp.co.axa.apidemo.entities.Employee;

import java.util.List;

public interface EmployeeService {

    public List<Employee> retrieveEmployees();

    public Employee getEmployee(Long employeeId);

    public Employee saveEmployee(EmployeeCreateUpdateDTO employeeDTO);

    public void deleteEmployee(Long employeeId);

    public void updateEmployee(Employee employee);
}