package main.repository;
import main.model.entities.Dialog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DialogsRepository extends JpaRepository<Dialog, Long> {
}
