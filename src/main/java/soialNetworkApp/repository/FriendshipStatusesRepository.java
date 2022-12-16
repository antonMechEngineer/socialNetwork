package soialNetworkApp.repository;
import soialNetworkApp.model.entities.FriendshipStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FriendshipStatusesRepository extends JpaRepository <FriendshipStatus, Long> {

}
