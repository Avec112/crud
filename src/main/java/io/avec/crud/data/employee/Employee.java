package io.avec.crud.data.employee;

import io.avec.crud.data.AbstractEntity;
import io.avec.crud.data.department.Department;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@NoArgsConstructor
@Setter
@Getter
@Entity
public class Employee extends AbstractEntity {
    private String firstName;

    @ManyToOne
    private Department department;

    public Employee(String firstName, Department department) {
        this.firstName = firstName;
        this.department = department;
    }
}
