package main.repository;

import main.model.entities.Captcha;
import main.model.entities.City;
import main.model.entities.Friendship;
import main.model.entities.Person;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PersonsRepository extends JpaRepository<Person, Long> {

    Optional<Person>findPersonById(Long id);

    boolean existsPersonByEmail(String email);

    Optional<Person> findPersonByEmail(String email);

    Page<Person> findAllByCity(String city, Pageable page);

    @Query("FROM Person AS p " +
            "ORDER BY p.regDate DESC")
    Page<Person> findPageOrderByRegDate(Pageable page);

    Person findPersonByFirstNameContainsIgnoreCaseOrLastNameContainsIgnoreCase(String firstName, String lastName);

    @Query(value = "SELECT id FROM persons WHERE is_deleted = true AND deleted_time < (NOW() - INTERVAL '1' minute)", nativeQuery = true)
    List<Long> findIdtoDelete();

    @Query(value = "SELECT * FROM persons WHERE is_deleted = true AND deleted_time < (NOW() - INTERVAL '1' minute)", nativeQuery = true)
    List<Person> findOldDeletes();
}
