package main.repository;
import main.config.entities.FriendshipStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FriendshipStatusRepository extends JpaRepository <FriendshipStatus, Integer> {
}
