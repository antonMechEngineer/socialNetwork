package main.repository;
import main.model.entities.Dialog;
import main.model.entities.Friendship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface DialogsRepository extends JpaRepository<Dialog, Long> {
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM dialogs WHERE (first_person_id = :id OR second_person_id = :id)",
            nativeQuery = true)
    void dialogsDelete(@Param("id") long id);
}
