package io.avec.crud.department;

import io.avec.crud.employee.Employee;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Setter
@Getter
@Entity
public class Department {

    @Id
    @GeneratedValue
    private Integer id;

    @NotNull
    @Column(unique = true)
    private String departmentName;
    private String floor;

    @OneToMany(
            mappedBy = "department",
            cascade = CascadeType.ALL
    )
    private List<Employee> employees = new ArrayList<>();


    public void addEmployee(Employee employee) {
        employees.add(employee);
        employee.setDepartment(this);
    }

    public void removeEmployee(Employee employee) {
        employees.remove(employee);
        employee.setDepartment(null);
    }




    public Department(String departmentName, String floor) {
        this.departmentName = departmentName;
        this.floor = floor;
    }

//    @Override
//    public String toString() {
//        return departmentName;
//    }
}
