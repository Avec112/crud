package io.avec.crud.employee;

import lombok.Getter;
import org.springframework.stereotype.Service;
import org.vaadin.artur.helpers.CrudService;

@Getter
@Service
public class EmployeeCrudService extends CrudService<Employee, Integer> {

    private EmployeeRepository repository;

    public EmployeeCrudService(EmployeeRepository repository) {
        this.repository = repository;
    }
}
