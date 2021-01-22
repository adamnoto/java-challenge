package jp.co.axa.apidemo.controllers;

import jp.co.axa.apidemo.dto.EmployeeCreateUpdateDTO;
import jp.co.axa.apidemo.entities.Employee;
import jp.co.axa.apidemo.services.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.util.Base64Utils;

import javax.transaction.Transactional;

import static org.hamcrest.Matchers.*;
import static org.assertj.core.api.BDDAssertions.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureJsonTesters
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles(profiles = "test")
public class EmployeeControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JacksonTester<EmployeeCreateUpdateDTO> employeeJsoner;

    @Autowired
    private EmployeeService employeeService;

    private Employee employee1, employee2;

    private static String authHeader = "Basic " + Base64Utils.encodeToString("axa:12345".getBytes());

    @BeforeEach
    void setUp() {
        employee1 = employeeService.saveEmployee(new EmployeeCreateUpdateDTO(
            "William Smith",
            600_000,
            "Finance"
        ));

        employee2 = employeeService.saveEmployee(new EmployeeCreateUpdateDTO(
            "Quincy Adams",
            550_000,
            "Human Resources"
        ));
    }

    @Test
    void getEmployees_IncorrectCredentials_ShouldNotAuthorize() throws Exception {
        // When
        ResultActions resultActions = mockMvc.perform(
            get("/api/v1/employees")
                .header(HttpHeaders.AUTHORIZATION, "WRONG")
        );

        // Then
        resultActions.andExpect(status().isUnauthorized());
    }

    @Test
    void getEmployees_HappyPath_ShouldReturnEmployees() throws Exception {
        // When
        ResultActions resultActions = mockMvc.perform(
            get("/api/v1/employees")
                .header(HttpHeaders.AUTHORIZATION, authHeader)
        );

        // Then
        resultActions
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(3)))
            .andExpect(jsonPath("$[*].createdAt", is(notNullValue())))
            .andExpect(jsonPath("$[*].updatedAt", is(notNullValue())))
            .andExpect(jsonPath("$[*].id", hasItems(employee1.getId().intValue(), employee2.getId().intValue())))
            .andExpect(jsonPath("$[*].name", hasItems(employee1.getName(), employee2.getName())))
            .andExpect(jsonPath("$[*].salary", hasItems(employee1.getSalary(), employee2.getSalary())))
            .andExpect(jsonPath("$[*].department", hasItems(employee1.getDepartment(), employee2.getDepartment())));
    }

    @Test
    void getEmployee_HappyPath_ShouldReturnEmployee() throws Exception {
        // When
        Employee emp = employeeService.retrieveEmployees().get(0);
        ResultActions resultActions = mockMvc.perform(
            get("/api/v1/employees/" + emp.getId())
                .header(HttpHeaders.AUTHORIZATION, authHeader)
        );

        // Then
        resultActions.andExpect(status().isOk());
    }

    @Test
    void getEmployee_NotFound_ShouldNotFound() throws Exception {
        // When
        ResultActions resultActions = mockMvc.perform(
            get("/api/v1/employees/-1")
                .header(HttpHeaders.AUTHORIZATION, authHeader)
        );

        // Then
        resultActions.andExpect(status().isNotFound());
    }

    @Test
    void postEmployee_HappyPath_ShouldReturnEmployee() throws Exception {
        // When
        EmployeeCreateUpdateDTO empDTO = new EmployeeCreateUpdateDTO(
            "Albus Dumbledore",
            900_000,
            "Office of Wizarding Affairs");

        ResultActions resultActions = mockMvc.perform(
            post("/api/v1/employees")
                .header(HttpHeaders.AUTHORIZATION, authHeader)
                .contentType(MediaType.APPLICATION_JSON)
                .content(employeeJsoner.write(empDTO).getJson()));

        // Then
        MockHttpServletResponse response = resultActions.andReturn().getResponse();
        then(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
    }

    @Test
    void postEmployee_IncompleteData_ShouldNotReturnEmployee() throws Exception {
        // When
        EmployeeCreateUpdateDTO empDTO = new EmployeeCreateUpdateDTO(
            "",
            1,
            "");

        ResultActions resultActions = mockMvc.perform(
            post("/api/v1/employees")
                .header(HttpHeaders.AUTHORIZATION, authHeader)
                .contentType(MediaType.APPLICATION_JSON)
                .content(employeeJsoner.write(empDTO).getJson()));

        // Then
        resultActions
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.errors.name", is("must not be blank")))
            .andExpect(jsonPath("$.errors.department", is("must not be blank")))
            .andExpect(jsonPath("$.errors.salary", is("must be greater than or equal to 300000")));
    }

    @Test
    void putEmployee_HappyPath_ShouldBeUpdated() throws Exception {
        String newName = "Tanaka Taro";
        int newSalary = 699000;
        String newDepartment = "Finance";

        // When 1 - Updating the employee
        Employee existingEmployee = employeeService.saveEmployee(new EmployeeCreateUpdateDTO(
            "Tanaka Tara", 500000, "Financ"
        ));
        EmployeeCreateUpdateDTO updateDTO = new EmployeeCreateUpdateDTO(newName, newSalary, newDepartment);
        ResultActions resultActions = mockMvc.perform(
            put("/api/v1/employees/" + existingEmployee.getId())
                .header(HttpHeaders.AUTHORIZATION, authHeader)
                .contentType(MediaType.APPLICATION_JSON)
                .content(employeeJsoner.write(updateDTO).getJson()));

        // Then
        resultActions.andExpect(status().isOk())
            .andExpect(jsonPath("$.name", is(newName)));

        // When 2 - Query the employee
        resultActions = mockMvc.perform(
            get("/api/v1/employees/" + existingEmployee.getId())
                .header(HttpHeaders.AUTHORIZATION, authHeader)
        );

        // Then
        resultActions.andExpect(status().isOk())
            .andExpect(jsonPath("$.name", is(newName)))
            .andExpect(jsonPath("$.salary", is(newSalary)))
            .andExpect(jsonPath("$.department", is(newDepartment)));
    }

    @Test
    void putEmployee_NotFound_ShouldNotCreateOrUpdate() throws Exception {
        // When
        EmployeeCreateUpdateDTO updateDTO = new EmployeeCreateUpdateDTO("ABC", 0, "DEF");
        ResultActions resultActions = mockMvc.perform(
            put("/api/v1/employees/-1")
                .header(HttpHeaders.AUTHORIZATION, authHeader)
                .contentType(MediaType.APPLICATION_JSON)
                .content(employeeJsoner.write(updateDTO).getJson()));

        // Then
        resultActions.andExpect(status().isNotFound());
    }
}
