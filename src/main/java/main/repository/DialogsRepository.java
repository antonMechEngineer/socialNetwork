package main.repository;
import main.model.entities.Dialog;
import main.model.entities.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DialogsRepository extends JpaRepository<Dialog, Long> {

    Optional<Dialog> findDialogByFirstPerson(Person person);
    Optional<Dialog> findDialogBySecondPerson(Person person);
}
