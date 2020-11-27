package io.avec.crud.employee;

import io.avec.crud.department.Department;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@Setter
@Getter
@Entity
public class Employee {

    @Id
    @GeneratedValue
    private Integer id;

    @NotNull
    private String firstName;

    @Column(unique = true)
    private String email;

    @ManyToOne//(fetch = FetchType.LAZY)
    private Department department;

    public Employee(String firstName, String email) {
        this.firstName = firstName;
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Employee employee = (Employee) o;

        return new EqualsBuilder()
                .append(id, employee.id)
                .append(firstName, employee.firstName)
                .append(email, employee.email)
                .append(department, employee.department)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .append(firstName)
                .append(email)
                .append(department)
                .toHashCode();
    }

}
