package main.repository;
import main.config.entities.Post2Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Post2TagRepository extends JpaRepository<Post2Tag, Integer> {
}
