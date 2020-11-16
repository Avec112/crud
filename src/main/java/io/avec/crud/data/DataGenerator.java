package io.avec.crud.data;

import io.avec.crud.data.department.Department;
import io.avec.crud.data.department.DepartmentRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class DataGenerator {

    @Bean
    CommandLineRunner createData(DepartmentRepository repository) {
        return args -> {
            repository.saveAll(Arrays.asList(
                    new Department("Sales", "1"),
                    new Department("Marketing", "2"),
                    new Department("Accounting", "3")
            ));
        };
    }
}
