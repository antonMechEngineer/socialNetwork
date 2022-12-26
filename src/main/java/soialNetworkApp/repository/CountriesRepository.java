package soialNetworkApp.repository;

import soialNetworkApp.model.entities.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CountriesRepository extends JpaRepository<Country, Long> {
    boolean existsCountryByName(String name);

    Optional<Country> findCountryByName(String country);

    Optional<Country> findCountryByCodeTwoSymbols(String code);
}
