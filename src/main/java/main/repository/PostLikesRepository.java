package main.repository;
import main.model.entities.Post;
import main.model.entities.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostLikesRepository extends JpaRepository<PostLike, Long> {

    List<PostLike> findPostLikesByPost(Post post);
}
