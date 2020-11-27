package io.avec.crud.employee;

import lombok.Getter;
import org.springframework.stereotype.Service;
import org.vaadin.artur.helpers.CrudService;

@Getter
@Service
public class EmployeeService extends CrudService<Employee, Integer> {

    private EmployeeRepository repository;

    public EmployeeService(EmployeeRepository repository) {
        this.repository = repository;
    }
}
