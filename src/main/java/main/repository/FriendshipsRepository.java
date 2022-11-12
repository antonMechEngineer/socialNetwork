package main.repository;
import main.model.entities.Friendship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FriendshipsRepository extends JpaRepository<Friendship, Long> {
}
