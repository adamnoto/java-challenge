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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

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

    @Test
    void postEmployee_HappyPath_ShouldReturnEmployee() throws Exception {
        // When
        EmployeeCreateUpdateDTO empDTO = new EmployeeCreateUpdateDTO(
            "Albus Dumbledore",
            900_000,
            "Office of Wizarding Affairs");

        ResultActions resultActions = mockMvc.perform(
            post("/api/v1/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(employeeJsoner.write(empDTO).getJson()));

        // Then
        MockHttpServletResponse response = resultActions.andReturn().getResponse();
        then(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
    }

    @Test
    void postEmployee_BlankName_ShouldNotReturnEmployee() throws Exception {
        // When
        EmployeeCreateUpdateDTO empDTO = new EmployeeCreateUpdateDTO(
            "",
            900_000,
            "Office of Wizarding Affairs");

        ResultActions resultActions = mockMvc.perform(
            post("/api/v1/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(employeeJsoner.write(empDTO).getJson()));

        resultActions.andDo(MockMvcResultHandlers.print());

        // Then
        resultActions
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.errors.name", is("must not be blank")));
    }
}
