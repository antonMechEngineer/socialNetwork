package main.repository;

import main.model.entities.BlockHistory;
import main.model.entities.Friendship;
import main.model.entities.Person;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FriendshipsRepository extends JpaRepository<Friendship, Long> {

    List<Friendship> findFriendshipBySrcPerson(Person srcPerson);

    @Query(value = "SELECT * FROM friendships WHERE (dst_person_id = :id OR src_person_id = :id)",
            nativeQuery = true)
    List<Friendship> findFriendsToDelete(@Param("id") long id);

    List<Friendship> findFriendshipsByDstPerson(Person dstPerson);
}
