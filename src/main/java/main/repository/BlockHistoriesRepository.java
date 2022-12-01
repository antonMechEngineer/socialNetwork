package main.repository;

import main.model.entities.BlockHistory;
import main.model.entities.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BlockHistoriesRepository extends JpaRepository<BlockHistory, Long> {
    @Query(value = "SELECT * FROM block_history WHERE person_id = :id", nativeQuery = true)
    List<BlockHistory> findBHtoDelete(@Param("id") long id);
}
