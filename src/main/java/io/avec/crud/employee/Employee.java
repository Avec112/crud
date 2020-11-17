package io.avec.crud.employee;

import io.avec.crud.data.AbstractEntity;
import io.avec.crud.department.Department;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@NoArgsConstructor
@Setter
@Getter
@Entity
public class Employee extends AbstractEntity {
    private String firstName;

    @Column(unique = true)
    private String email;

    @ManyToOne
    private Department department;

    public Employee(String firstName, String email, Department department) {
        this.firstName = firstName;
        this.email = email;
        this.department = department;
    }
}
