package io.avec.crud.department;

import lombok.Getter;
import org.springframework.stereotype.Service;
import org.vaadin.artur.helpers.CrudService;

@Getter
@Service
public class DepartmentCrudService extends CrudService<Department, Integer> {

    private final DepartmentRepository repository;

    public DepartmentCrudService(DepartmentRepository repository) {
        this.repository = repository;
    }
}
