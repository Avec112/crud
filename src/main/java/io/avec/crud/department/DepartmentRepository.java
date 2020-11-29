package io.avec.crud.department;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DepartmentRepository extends JpaRepository<Department, Integer> {

    List<Department> findByDepartmentNameContainingIgnoreCase(String departmentName);

    Optional<Department> findByDepartmentName(String departmentName);
}