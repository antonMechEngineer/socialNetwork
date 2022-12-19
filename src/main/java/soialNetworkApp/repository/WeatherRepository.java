package soialNetworkApp.repository;

import soialNetworkApp.model.entities.Weather;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WeatherRepository extends JpaRepository<Weather, Integer> {

    Optional<Weather> findTopByGismeteoId(int gesmeteoId);
}
