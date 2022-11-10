package main.repository;

import main.config.entities.PersonSettings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonSettingsRepository extends JpaRepository<PersonSettings, Integer> {
}
