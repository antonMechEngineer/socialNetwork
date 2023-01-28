package soialNetworkApp.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import soialNetworkApp.api.response.RegionStatisticRs;
import soialNetworkApp.model.entities.Person;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PersonsRepository extends JpaRepository<Person, Long>, JpaSpecificationExecutor<Person> {

    Optional<Person>findPersonById(Long id);

    Optional<Person> findPersonByEmail(String email);

    Page<Person> getPersonByCityAndIdNotIn(Pageable page, String city, List<Long> ids);

    Page<Person> findPersonByIdIn (List<Long> personIds, Pageable pageable);

    Page<Person> getPersonByIdNotInOrderByRegDateDesc(Pageable page, List<Long> ids);

    List<Person> findPersonByFirstNameContainsIgnoreCaseOrLastNameContainsIgnoreCase(String firstName, String lastName);

    @Query(value = "SELECT id FROM persons WHERE is_deleted = true AND (select(select extract(epoch from now()) - (extract(epoch from(deleted_time)))) * 1000 >  :timeToDel)", nativeQuery = true)
    List<Long> idToDelete(@Param("timeToDel") long timeToDel);

    @Query(value = "SELECT * FROM persons WHERE is_deleted = true AND (select(select extract(epoch from now()) - (extract(epoch from(deleted_time)))) * 1000 > :timeToDel)", nativeQuery = true)
    List<Person> findOldDeletes(@Param("timeToDel") long timeToDel);

    @Query(value = "SELECT * FROM persons WHERE change_password_token = :token", nativeQuery = true)
    Optional<Person> checkToken(@Param("token") String token);

    @Modifying
    @Transactional
    @Query(value = "UPDATE Person SET lastOnlineTime = :time WHERE email = :personEmail")
    void updateOnlineTime(@Param("personEmail") String personEmail, @Param("time") LocalDateTime time);

    @Query(value = "FROM Person WHERE MONTH(birthDate) = :month AND DAY(birthDate) = :day")
    List<Person> findPeopleByBirthDate(@Param("month") int month, @Param("day") int day);

    Long countAllByCountryIgnoreCase(String country);

    Long countAllByCityIgnoreCase(String city);

    @Query(value = "SELECT new soialNetworkApp.api.response.RegionStatisticRs(p.city, COUNT(p.city)) FROM Person AS p " +
            "WHERE p.city IS NOT NULL " +
            "GROUP BY p.city")
    List<RegionStatisticRs> getCityWithUsersCount();

    @Query(value = "SELECT new soialNetworkApp.api.response.RegionStatisticRs(p.country, COUNT(p.country)) FROM Person AS p " +
            "WHERE p.country IS NOT NULL " +
            "GROUP BY p.country")
    List<RegionStatisticRs> getCountryWithUsersCount();

    List<Person> findPersonByFirstNameContainsIgnoreCaseAndLastNameContainsIgnoreCase(String firstName, String lastName);

    @Query(value = "SELECT DISTINCT city FROM Person WHERE country = :country")
    List<String> getCitiesByCountry(@Param("country") String country);
}
