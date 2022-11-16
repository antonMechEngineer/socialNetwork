package main.repository;

import main.model.entities.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonsRepository extends JpaRepository<Person, Long> {

    boolean existsPersonByEmail(String email);
}
