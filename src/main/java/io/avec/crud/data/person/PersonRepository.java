package io.avec.crud.data.person;


import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PersonRepository extends JpaRepository<Person, Integer> {

    List<Person> findByFirstNameContainingIgnoreCase(String name);
}