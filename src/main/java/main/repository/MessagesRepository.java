package main.repository;
import main.model.entities.Dialog;
import main.model.entities.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface MessagesRepository extends JpaRepository<Message, Long> {
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM messages WHERE (author_id = :id OR recipient_id = :id)",
            nativeQuery = true)
    void messagesDelete(@Param("id") long id);
}
