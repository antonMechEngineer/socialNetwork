package soialNetworkApp.repository;
import soialNetworkApp.model.entities.Dialog;
import soialNetworkApp.model.entities.Message;
import soialNetworkApp.model.entities.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import soialNetworkApp.model.enums.ReadStatusTypes;

import java.util.List;

@Repository
public interface MessagesRepository extends JpaRepository<Message, Long> {
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM messages WHERE (author_id = :id OR recipient_id = :id)",
            nativeQuery = true)
    void messagesDelete(@Param("id") long id);

    List<Message> findAllByRecipientAndIsDeletedFalse(Person recipient);

    List<Message> findAllByRecipientAndReadStatusAndIsDeletedFalse(Person person, ReadStatusTypes status);
    List<Message> findAllByDialogIdAndRecipientAndReadStatusAndIsDeletedFalse(Long dialogId, Person person, ReadStatusTypes status);

    List<Message> findAllByDialogIdAndIsDeletedFalse(Long dialogId);

    Long countAllByDialogId(long id);

    Long countAllByAuthorIdAndRecipientId(long authorId, long recipientId);
}
