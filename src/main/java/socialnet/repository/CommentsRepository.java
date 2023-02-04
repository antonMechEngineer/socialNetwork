package socialnet.repository;
import socialnet.model.entities.Comment;
import socialnet.model.entities.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface CommentsRepository extends JpaRepository<Comment, Long> {

    Page<Comment> findCommentsByPostOrderByTimeAsc(Pageable pageable, Post post);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM post_comments WHERE author_id = :id", nativeQuery = true)
    void commentsDelete(@Param("id") long id);

    @Query(value = "SELECT id FROM post_comments WHERE author_id = :id", nativeQuery = true)
    List<Long> findCommentsIdToDelete(@Param("id") long id);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM post_comments WHERE parent_id = :id", nativeQuery = true)
    void secondaryCommentsDelete(@Param("id") long id);

    Long countAllByPostId(long postId);
}
