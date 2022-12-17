package soialNetworkApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import soialNetworkApp.model.entities.City;

import java.util.List;
import java.util.Optional;

@Repository
public interface CitiesRepository extends JpaRepository<City, Long> {
    boolean existsCityByTitle(String title);

    Optional<City> findCityByTitle(String city);

    @Query("SELECT DISTINCT gismeteoId FROM City")
    List<Integer> findGismeteoIds();
}
