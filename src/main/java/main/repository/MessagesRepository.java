package main.repository;
import main.model.entities.Message;
import main.model.entities.Person;
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

    List<Message> findAllByDialogId(Long dialogId);

    List<Message> findAllByRecipientAndIsDeletedFalse(Person recipient);
    List<Message> findAllByAuthorAndIsDeletedFalse(Person author);
    List<Message> findAllByDialogIdAndIsDeletedFalse(Long dialogId);
}
