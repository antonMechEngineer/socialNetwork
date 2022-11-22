package main.repository;

import main.model.entities.Like;
import main.model.entities.Liked;
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

    @Query(value = "FROM Like WHERE type = :type AND entity = :entity")
    List<Like> findLikesByEntity(@Param("type") LikeTypes type, @Param("entity") Liked liked);

    @Query(value = "FROM Like WHERE type = :type AND entity = :entity AND person = :person")
    Optional<Like> findLikeByPersonAndEntity(@Param("type") LikeTypes type, @Param("entity") Liked liked, @Param("person") Person person);
}
