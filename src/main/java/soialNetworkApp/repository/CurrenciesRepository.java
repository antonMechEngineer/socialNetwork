package soialNetworkApp.repository;
import soialNetworkApp.model.entities.Currency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CurrenciesRepository extends JpaRepository<Currency, Long> {
    Optional<Currency> findCurrenciesByName(String name);

    Optional<Currency> findTopByName(String name);
}
