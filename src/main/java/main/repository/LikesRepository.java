package main.repository;

import main.model.entities.Like;
import main.model.entities.Person;
import main.model.enums.LikeTypes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LikesRepository extends JpaRepository<Like, Long> {

    @Query(value = "SELECT * FROM likes WHERE type = :type AND entity_id = :entity_id", nativeQuery = true)
    List<Like> findLikesByEntity(@Param("type") String type, @Param("entity_id") long entityId);

    @Query(value = "SELECT * FROM likes WHERE type = :type AND entity_id = :entity_id AND person_id = :person_id", nativeQuery = true)
    List<Like> findLikesByPersonAndEntity(@Param("type") String type, @Param("entity_id") long entityId, @Param("person_id") long personId);
}
