package main.repository;

import main.model.entities.City;
import main.model.entities.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CountriesRepository extends JpaRepository<Country, Long> {
    boolean existsCountryByTitle(String title);
}
