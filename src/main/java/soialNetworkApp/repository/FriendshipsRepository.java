package soialNetworkApp.repository;

import soialNetworkApp.model.entities.Friendship;
import soialNetworkApp.model.entities.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import soialNetworkApp.model.enums.FriendshipStatusTypes;

import java.util.List;

@Repository
public interface FriendshipsRepository extends JpaRepository<Friendship, Long> {

    List<Friendship> findFriendshipBySrcPerson(Person srcPerson);

    List<Friendship> findFriendshipById(Long id);

    @Query(value = "SELECT * FROM friendships WHERE (dst_person_id = :id OR src_person_id = :id)",
            nativeQuery = true)
    List<Friendship> findFriendsToDelete(@Param("id") long id);

    List<Friendship> findFriendshipsByDstPerson(Person dstPerson);

    Friendship findFriendshipBySrcPersonIdAndDstPersonId(Long srcPersonId, Long dstPersonId);

    List<Friendship> findFriendshipsByDstPersonIdAndFriendshipStatus(Long id, FriendshipStatusTypes status);

    List<Friendship> findFriendshipsBySrcPersonIdOrDstPersonIdAndFriendshipStatus(Long srcId, Long dstId, FriendshipStatusTypes status);
}
