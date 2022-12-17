package soialNetworkApp.repository;

import soialNetworkApp.model.entities.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CitiesRepository extends JpaRepository<City, Long> {
    boolean existsCityByTitle(String title);
}
