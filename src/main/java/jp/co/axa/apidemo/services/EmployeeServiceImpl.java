package jp.co.axa.apidemo.services;

import jp.co.axa.apidemo.dto.EmployeeCreateUpdateDTO;
import jp.co.axa.apidemo.entities.Employee;
import jp.co.axa.apidemo.repositories.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService{
    private final EmployeeRepository employeeRepository;

    public List<Employee> retrieveEmployees() {
        List<Employee> employees = employeeRepository.findAll();
        return employees;
    }

    @Cacheable(value = "employees", key = "#employeeId")
    public Optional<Employee> getEmployee(Long employeeId) {
        return employeeRepository.findById(employeeId);
    }

    public Employee saveEmployee(EmployeeCreateUpdateDTO dto){
        Employee employee = new Employee(dto);
        employee = employeeRepository.save(employee);
        return employee;
    }

    @CacheEvict(value = "employees", key = "#employeeId")
    public void deleteEmployee(Long employeeId){
        employeeRepository.deleteById(employeeId);
    }

    @CachePut(value = "employees", key="#employee.id")
    public Employee updateEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }
}