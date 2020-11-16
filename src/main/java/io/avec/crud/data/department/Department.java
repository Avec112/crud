package io.avec.crud.data.department;

import io.avec.crud.data.AbstractEntity;
import io.avec.crud.data.person.Person;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.List;

@NoArgsConstructor
@Setter
@Getter
@Entity
public class Department extends AbstractEntity {
    private String departmentName;
    private String floor;

    @OneToMany(targetEntity = Person.class, mappedBy = "department")
    private List<Person> person;

    public Department(String departmentName, String floor) {
        this.departmentName = departmentName;
        this.floor = floor;
    }

}
