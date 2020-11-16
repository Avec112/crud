package io.avec.crud.data.person;

import io.avec.crud.data.AbstractEntity;
import io.avec.crud.data.department.Department;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Setter
@Getter
@Entity
public class Person extends AbstractEntity {
    private String firstName;

    @ManyToOne
    private Department department;
}
