package soialNetworkApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import soialNetworkApp.model.entities.Currency;

import java.util.Optional;

@Repository
public interface CurrenciesRepository extends JpaRepository<Currency, Long> {

    Optional<Currency> findFirstByNameOrderByUpdateTimeDesc(String name);
}
