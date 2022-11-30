package main.repository;

import main.model.entities.Friendship;
import main.model.entities.Person;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FriendshipsRepository extends JpaRepository<Friendship, Long> {

    Friendship findFriendshipBySrcPersonAndDstPerson(Person srcPerson, Person dstPerson);
    List<Friendship> findFriendshipBySrcPerson(Person srcPerson);



}
