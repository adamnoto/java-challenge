package jp.co.axa.apidemo.controllers;

import jp.co.axa.apidemo.dto.EmployeeCreateUpdateDTO;
import jp.co.axa.apidemo.entities.Employee;
import jp.co.axa.apidemo.services.EmployeeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/employees")
public class EmployeeController {
    private final EmployeeService employeeService;

    @GetMapping
    public List<Employee> getEmployees() {
        List<Employee> employees = employeeService.retrieveEmployees();
        return employees;
    }

    @GetMapping("/{employeeId}")
    public Employee getEmployee(@PathVariable(name="employeeId")Long employeeId) {
        log.info("Retrieve an employee with ID: " + employeeId);
        return employeeService.getEmployee(employeeId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<Employee> saveEmployee(@Valid @RequestBody EmployeeCreateUpdateDTO dto){
        Employee employee = employeeService.saveEmployee(dto);
        log.info("New employee created with ID: {}", employee.getId());
        return new ResponseEntity<>(employee, HttpStatus.CREATED);
    }

    @DeleteMapping("/{employeeId}")
    public ResponseEntity deleteEmployee(@PathVariable(name="employeeId")Long employeeId){
        employeeService.getEmployee(employeeId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        log.info("Delete an employee with ID: " + employeeId);
        employeeService.deleteEmployee(employeeId);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{employeeId}")
    public Employee updateEmployee(@RequestBody EmployeeCreateUpdateDTO dto,
                               @PathVariable(name="employeeId")Long employeeId){

        Employee emp = employeeService.getEmployee(employeeId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        log.info("Update an employee with ID: " + employeeId);
        emp.updateFrom(dto);
        return employeeService.updateEmployee(emp);
    }

}
