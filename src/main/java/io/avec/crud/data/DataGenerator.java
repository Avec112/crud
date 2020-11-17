package io.avec.crud.data;

import io.avec.crud.department.Department;
import io.avec.crud.department.DepartmentRepository;
import io.avec.crud.employee.Employee;
import io.avec.crud.employee.EmployeeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Configuration
public class DataGenerator {

    @Bean
    CommandLineRunner createData(DepartmentRepository repository, EmployeeRepository employeeRepository) {
        return args -> {
            List<Department> departments = Arrays.asList(
                    new Department("Sales", "1"),
                    new Department("Marketing", "2"),
                    new Department("Accounting", "3"),
                    new Department("Management", "4")
            );
            log.debug("Created {} departments.", departments.size());
            repository.saveAll(departments);

            List<Employee> employees = Arrays.asList(
                    new Employee("John", departments.get(0)),  // Sales
                    new Employee("Susan", departments.get(1)), // Marketing
                    new Employee("Lori", departments.get(1)), // Marketing
                    new Employee("Paul", departments.get(2))   // Accounting
            );
            log.debug("Created {} employees.", employees.size());
            employeeRepository.saveAll(employees);

        };
    }
}
