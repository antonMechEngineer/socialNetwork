package main.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostsRepository extends JpaRepository<Post, Long> {

    Page<Post> findAllOrderByPublicationDateDesc(Pageable pageable);
}
