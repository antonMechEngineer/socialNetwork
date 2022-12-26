package soialNetworkApp.repository;

import soialNetworkApp.model.entities.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagsRepository extends JpaRepository<Tag, Long> {

    Tag findByTagName(String tagName);
}

