package io.avec.crud.employee;


import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

//    List<Employee> findByFirstNameContainingIgnoreCase(String name);
//    List<Employee> findByDepartmentDepartmentNameContainingIgnoreCase(String departmentName);

}