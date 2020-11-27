package io.avec.crud.department;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DepartmentRepository extends JpaRepository<Department, Integer> {
    List<Department> findByDepartmentNameContainingIgnoreCase(String departmentName);
    List<Department> findDistinctByDepartmentName();

}