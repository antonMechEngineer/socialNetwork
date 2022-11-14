package main.repository;

import main.model.entities.BlockHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlockHistoriesRepository extends JpaRepository<BlockHistory, Long> {
}
