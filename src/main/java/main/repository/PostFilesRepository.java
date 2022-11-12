package main.repository;
import main.model.entities.PostFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostFilesRepository extends JpaRepository<PostFile, Long> {
}
