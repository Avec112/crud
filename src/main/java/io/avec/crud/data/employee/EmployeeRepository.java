package io.avec.crud.data.employee;


import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

    List<Employee> findByFirstNameContainingIgnoreCase(String name);
    List<Employee> findByDepartmentDepartmentNameContainingIgnoreCase(String departmentName);

}