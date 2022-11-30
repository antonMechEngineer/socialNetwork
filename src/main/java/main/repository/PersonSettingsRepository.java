package main.repository;

import main.model.entities.PersonSettings;
import main.model.entities.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface PersonSettingsRepository extends JpaRepository<PersonSettings, Long> {
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM person_settings WHERE person_id = :id", nativeQuery = true)
    void persSetDelete(@Param("id") long id);
}
