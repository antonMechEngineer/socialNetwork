package soialNetworkApp.repository;
import soialNetworkApp.model.entities.PostFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostFilesRepository extends JpaRepository<PostFile, Long> {
}
