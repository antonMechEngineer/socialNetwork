package socialnet.repository;

import socialnet.model.entities.Weather;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WeatherRepository extends JpaRepository<Weather, Integer> {

    Optional<Weather> findFirstByGismeteoIdOrderByTimeDesc(int gesmeteoId);
}
