package jp.co.axa.apidemo.controllers;

import jp.co.axa.apidemo.entities.Employee;
import jp.co.axa.apidemo.services.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.sql.Timestamp;
import java.util.Arrays;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EmployeeController.class)
@ActiveProfiles(profiles = "test")
public class EmployeeControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;
    private Employee employee1, employee2;

    @BeforeEach
    void setUp() {
        employee1 = new Employee(
            1L,
            "William Smith",
            600_000,
            "Finance",
            new Timestamp(System.currentTimeMillis()),
            new Timestamp(System.currentTimeMillis())
        );

        employee2 = new Employee(
            2L,
            "Quincy Adams",
            550_000,
            "Human Resources",
            new Timestamp(System.currentTimeMillis()),
            new Timestamp(System.currentTimeMillis())
        );

        when(employeeService.retrieveEmployees())
            .thenReturn(Arrays.asList(employee1, employee2));
    }

    @Test
    void getEmployees_HappyPath_ShouldReturnEmployees() throws Exception {
        // When
        ResultActions resultActions = mockMvc.perform(
            get("/api/v1/employees")
        );

        // Then
        resultActions
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(2)))
            .andExpect(jsonPath("$[*].createdAt", is(notNullValue())))
            .andExpect(jsonPath("$[*].updatedAt", is(notNullValue())))
            .andExpect(jsonPath("$[*].id", contains(employee1.getId().intValue(), employee2.getId().intValue())))
            .andExpect(jsonPath("$[*].name", contains(employee1.getName(), employee2.getName())))
            .andExpect(jsonPath("$[*].salary", contains(employee1.getSalary(), employee2.getSalary())))
            .andExpect(jsonPath("$[*].department", contains(employee1.getDepartment(), employee2.getDepartment())));
    }
}
