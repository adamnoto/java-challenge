package jp.co.axa.apidemo;

import jp.co.axa.apidemo.dto.EmployeeCreateUpdateDTO;
import jp.co.axa.apidemo.entities.App;
import jp.co.axa.apidemo.entities.AppRight;
import jp.co.axa.apidemo.repositories.AppRepository;
import jp.co.axa.apidemo.repositories.EmployeeRepository;
import jp.co.axa.apidemo.services.EmployeeService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Arrays;

/**
 * This class will load some initial data so that we can use
 * the app right away. On the real production environment, we should
 * not be doing this.
 */
@Component
@AllArgsConstructor
@Order(Ordered.LOWEST_PRECEDENCE)
@Slf4j
public class DataLoader {
    private final AppRepository appRepository;
    private final EmployeeService employeeService;

    @PostConstruct
    private void loadData() {
        long appCount = appRepository.count();

        log.info(String.format("%s instances of App found in the database", appCount));

        if (appCount == 0) {
            log.info("No App instances found. Some App intended for demo will be generated.");
            appRepository.saveAll(Arrays.asList(
                new App("AXA", "axa", "12345", AppRight.ALL),
                new App("SVC1", "svc1", "12345"),
                new App("SVC2", "svc2", "12345", Arrays.asList(AppRight.WRITE)),
                new App("SVC3")
            ));
        }

        if (employeeService.retrieveEmployees().isEmpty()) {
            log.info("No Employee instances found. Will generate some employees.");
            employeeService.saveEmployee(new EmployeeCreateUpdateDTO(
                "Adam Pahlevi Baihaqi",
                666666,
                "Software Engineering"));
        }
    }
}
