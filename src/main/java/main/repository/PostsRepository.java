package main.repository;

import main.model.entities.Person;
import main.model.entities.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface PostsRepository extends JpaRepository<Post, Long> {

    Page<Post> findPostsByAuthorOrderByTimeDesc(Pageable pageable, Person author);

    Page<Post> findPostsByTimeBeforeAndIsDeletedFalseOrderByTimeDesc(Pageable pageable, LocalDateTime time);

}
