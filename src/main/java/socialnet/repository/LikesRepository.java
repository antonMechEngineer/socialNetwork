package socialnet.repository;

import socialnet.model.entities.Like;
import socialnet.model.entities.Person;
import socialnet.model.entities.interfaces.Liked;
import socialnet.model.enums.LikeTypes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface LikesRepository extends JpaRepository<Like, Long> {

    @Query(value = "FROM Like WHERE type = :type AND entity = :entity")
    List<Like> findLikesByEntity(@Param("type") LikeTypes type, @Param("entity") Liked liked);

    @Query(value = "FROM Like WHERE type = :type AND entity = :entity AND author = :person")
    Optional<Like> findLikeByPersonAndEntity(@Param("type") LikeTypes type, @Param("entity") Liked liked, @Param("person") Person person);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM likes WHERE person_id = :id", nativeQuery = true)
    void likeDelete(@Param("id") long id);

    @Query(value = "SELECT COUNT(l.entity_id) FROM likes AS l WHERE l.entity_id = :id", nativeQuery = true)
    Long countAllByEntityId(@Param("id") long id);
}
