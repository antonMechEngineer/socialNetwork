package main.repository;

import main.model.entities.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostsRepository extends JpaRepository<Post, Long> {

    Page<Post> findPostsByAuthorIDOrderByTimeDesc(Pageable pageable, long authorId);

}
