package main.repository;

import main.model.entities.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Person, Integer> {

    Person findPersonByEmail(String email);
}
